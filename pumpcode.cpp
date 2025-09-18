/**
 * @file PumpNode_ESP8266_AlwaysOn_Debug.ino
 * @brief ESP8266 pump node (always-on) controlled via MQTT, with diagnostics.
 *
 * Control  : irrigation/<ZONE>/control -> {"command":"ON","duration":3} / {"command":"OFF"}
 * Status   : irrigation/<ZONE>/status  -> "online"/"offline" (retained)
 * Runtime  : irrigation/<ZONE>/runtime -> {"running":true/false,"remaining_s":N}
 * Debug    : irrigation/<ZONE>/debug   -> human-readable diagnostics
 *
 * Hardware:
 *  - ESP8266 (e.g., Wemos D1 mini)
 *  - IRLZ44N gate on D2 (GPIO4) via ~220 Ω; 5–10 kΩ gate pulldown to GND
 *  - Flyback diode across 6 V pump (cathode to +6 V, anode to pump−/MOSFET drain)
 *  - MT3608 boost to 6.0 V for the pump
 *  - COMMON GND: ESP8266, MT3608 OUT−, MOSFET source
 */

#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

// =============================== USER CONFIG ===============================

/** @brief Wi-Fi SSID to connect to. */
const char* WIFI_SSID = "Roma_di-mesh";

/** @brief Wi-Fi password for the SSID. */
const char* WIFI_PASS = "Arina2011#";

/** @brief MQTT broker host/IP (your Raspberry Pi). */
const char* MQTT_HOST = "192.168.68.50";

/** @brief MQTT broker TCP port (default Mosquitto is 1883). */
const uint16_t MQTT_PORT = 1883;

/**
 * @def ZONE
 * @brief Logical irrigation zone identifier.
 * Change per device to make topic namespace unique (e.g., "zone2", "zone3").
 */
#define ZONE               "zone1"

/**
 * @def PUMP_PIN
 * @brief ESP8266 GPIO driving the MOSFET gate.
 * D2 maps to GPIO4 on Wemos D1 mini; safe (not a boot-strap pin).
 */
#define PUMP_PIN           D2

/**
 * @def USE_QOS1
 * @brief Subscribe/publish control/status at QoS 1 (at least once delivery).
 */
#define USE_QOS1           1

/**
 * @def SELF_TEST_SEC
 * @brief Optional pump self-test duration at boot (seconds). Use 0 to disable.
 */
#define SELF_TEST_SEC      0

/**
 * @def HEARTBEAT_RUN_MS
 * @brief Runtime publish cadence while the pump is running (milliseconds).
 */
#define HEARTBEAT_RUN_MS   1000UL

/**
 * @def ENABLE_IDLE_HEARTBEAT
 * @brief Enable (1) or disable (0) periodic runtime publishes while idle.
 */
#define ENABLE_IDLE_HEARTBEAT 0

/**
 * @def HEARTBEAT_IDLE_MS
 * @brief Idle publish cadence (milliseconds) when ENABLE_IDLE_HEARTBEAT == 1.
 */
#define HEARTBEAT_IDLE_MS  (10UL * 60UL * 1000UL)  // 10 minutes

// ================================ TOPIC MAP =================================

/** @brief Topic to receive control commands (ON/OFF). */
String T_CONTROL = String("irrigation/") + ZONE + "/control";

/** @brief Topic to publish retained availability ("online"/"offline"). */
String T_STATUS  = String("irrigation/") + ZONE + "/status";

/** @brief Topic to publish JSON runtime snapshots. */
String T_RUNTIME = String("irrigation/") + ZONE + "/runtime";

/** @brief Topic to publish human-readable debug lines. */
String T_DEBUG   = String("irrigation/") + ZONE + "/debug";

// ================================ GLOBALS ===================================

/** @brief TCP client used by PubSubClient for MQTT transport. */
WiFiClient wifi;

/** @brief MQTT client for publish/subscribe. */
PubSubClient mqtt(wifi);

/** @brief True while MOSFET gate is asserted (pump ON). */
bool pumpRunning = false;

/** @brief Absolute millis() timestamp when the pump should be turned OFF. */
unsigned long pumpOffAt = 0;

/** @brief Last millis() when a runtime message was published. */
unsigned long lastRuntimePub = 0;

// =============================== MQTT HELPERS ===============================

/**
 * @brief Publish retained availability status.
 * @param s Null-terminated C-string: "online" or "offline".
 */
void publish_status(const char* s) {
  mqtt.publish(T_STATUS.c_str(), s, true); // retained availability
}

/**
 * @brief Publish a human-readable debug line to MQTT and Serial.
 * @param s Debug message string.
 */
void publish_debug(const String& s) {
  mqtt.publish(T_DEBUG.c_str(), s.c_str(), false);
  Serial.println(s);
}

/**
 * @brief Publish current runtime state as JSON.
 *
 * Structure:
 * @code
 * {"running":true|false,"remaining_s":N}
 * @endcode
 *
 * - remaining_s is ceil()’d to the next whole second while running.
 */
void publish_runtime() {
  StaticJsonDocument<128> doc;
  doc["running"] = pumpRunning;

  unsigned long remaining = 0;
  if (pumpRunning) {
    unsigned long now = millis();
    unsigned long diff = (pumpOffAt > now) ? (pumpOffAt - now) : 0;
    remaining = (diff + 999UL) / 1000UL; // ceil to seconds
  }
  doc["remaining_s"] = remaining;

  char buf[128];
  serializeJson(doc, buf);
  mqtt.publish(T_RUNTIME.c_str(), buf, false);
}

/**
 * @brief Immediately switch the pump OFF and publish runtime/debug.
 */
void pump_off_now() {
  digitalWrite(PUMP_PIN, LOW);
  pumpRunning = false;
  pumpOffAt = 0;
  publish_debug("pump: OFF");
  publish_runtime();
}

/**
 * @brief Start/extend a non-blocking pump run for @p sec seconds.
 *
 * Uses a millis() deadline; the main loop handles cut-off and periodic updates.
 * @param sec Requested duration (seconds); ignored if zero.
 */
void pump_on_for_sec(uint32_t sec) {
  if (!sec) return;
  digitalWrite(PUMP_PIN, HIGH);
  pumpRunning = true;
  pumpOffAt = millis() + (sec * 1000UL);
  publish_debug(String("pump: ON for ") + sec + "s");
  publish_runtime(); // immediate feedback
}

/**
 * @brief MQTT message callback: parses control JSON on @ref T_CONTROL.
 *
 * Expected payloads:
 * - `{"command":"ON","duration":N}` : start/extend a run
 * - `{"command":"OFF"}`             : stop immediately
 *
 * @param topic   Incoming topic (C-string).
 * @param payload Pointer to raw payload bytes.
 * @param len     Payload length in bytes.
 */
void on_mqtt(char* topic, byte* payload, unsigned int len) {
  // Log raw payload for certainty
  Serial.printf("CB topic=%s len=%u payload=", topic, len);
  for (unsigned int i = 0; i < len; i++) Serial.write(payload[i]);
  Serial.println();

  if (strcmp(topic, T_CONTROL.c_str()) != 0) return; // ignore other topics

  StaticJsonDocument<256> doc;
  DeserializationError err = deserializeJson(doc, payload, len);
  if (err) { publish_debug(String("json err: ") + err.c_str()); return; }

  const char* cmd = doc["command"] | "";
  uint32_t duration = doc["duration"] | 0;

  publish_debug(String("ctrl: cmd=") + cmd + " dur=" + duration);

  if (strcmp(cmd, "ON") == 0) {
    unsigned long now = millis();
    unsigned long requestedEnd = now + (duration * 1000UL);
    if (pumpRunning) {
      // Extend if requested end is later than current deadline
      if ((long)(requestedEnd - pumpOffAt) > 0) pumpOffAt = requestedEnd;
      publish_runtime();
    } else {
      pump_on_for_sec(duration);
    }
  } else if (strcmp(cmd, "OFF") == 0) {
    pump_off_now();
  } else {
    publish_debug("ctrl: unknown command");
  }
}

// ============================ CONNECTIVITY ==================================

/**
 * @brief Connect to Wi-Fi (blocking with retries). Prints IP on success.
 *
 * Tip: keep external loads disconnected while debugging connectivity.
 */
void wifi_connect_blocking() {
  WiFi.mode(WIFI_STA);
  // WiFi.setSleep(true); // enable later if you want lower idle current
  WiFi.begin(WIFI_SSID, WIFI_PASS);

  unsigned long t0 = millis();
  while (WiFi.status() != WL_CONNECTED) {
    delay(300);
    if (millis() - t0 > 20000UL) {
      WiFi.disconnect(true);
      delay(300);
      WiFi.begin(WIFI_SSID, WIFI_PASS);
      t0 = millis();
    }
  }
  Serial.println("\nWiFi connected");
  Serial.print("IP: "); Serial.println(WiFi.localIP());
}

/**
 * @brief Connect to MQTT, set LWT ("offline"), subscribe to control,
 *        and publish initial status/runtime/debug.
 * @return true on success, false on failure.
 */
bool mqtt_connect() {
  mqtt.setServer(MQTT_HOST, MQTT_PORT);
  mqtt.setCallback(on_mqtt);
  mqtt.setBufferSize(512);

  String cid = String("pump-") + ZONE + "-" + String(ESP.getChipId(), HEX);
  bool ok = mqtt.connect(cid.c_str(), T_STATUS.c_str(), USE_QOS1, true, "offline");
  if (!ok) {
    Serial.printf("MQTT connect failed, state=%d\n", mqtt.state());
    return false;
  }

  mqtt.subscribe(T_CONTROL.c_str(), USE_QOS1);
  publish_status("online");
  publish_runtime();
  publish_debug(String("hello; subscribed to: ") + T_CONTROL);
  return true;
}

/**
 * @brief Ensure MQTT stays connected, retrying for up to ~8 seconds.
 */
void ensure_mqtt() {
  if (mqtt.connected()) return;
  unsigned long start = millis();
  while (!mqtt.connected() && millis() - start < 8000UL) {
    mqtt_connect();
    if (!mqtt.connected()) delay(500);
  }
}

// ================================ ARDUINO ===================================

/**
 * @brief Arduino setup: GPIO init, optional self-test, Wi-Fi + MQTT connect.
 */
void setup() {
  Serial.begin(115200);
  delay(50);

  pinMode(PUMP_PIN, OUTPUT);
  digitalWrite(PUMP_PIN, LOW);

  // Optional 2s self-test spin (kept OFF by default)
  if (SELF_TEST_SEC > 0) {
    digitalWrite(PUMP_PIN, HIGH);
    delay(SELF_TEST_SEC * 1000UL);
    digitalWrite(PUMP_PIN, LOW);
  }

  wifi_connect_blocking();
  mqtt_connect();
}

/**
 * @brief Arduino loop: keep MQTT alive, handle non-blocking pump timeout,
 *        and publish periodic runtime updates.
 */
void loop() {
  ensure_mqtt();
  mqtt.loop();

  // Stop pump at deadline (non-blocking)
  if (pumpRunning && (long)(millis() - pumpOffAt) >= 0) {
    pump_off_now();
  }

  // Runtime publishes: every 1 s while running; idle cadence optional/disabled
  unsigned long now = millis();
  unsigned long interval = pumpRunning ? HEARTBEAT_RUN_MS : HEARTBEAT_IDLE_MS;

#if ENABLE_IDLE_HEARTBEAT
  if (now - lastRuntimePub >= interval) {
    publish_runtime();
    lastRuntimePub = now;
  }
#else
  if (pumpRunning && (now - lastRuntimePub >= HEARTBEAT_RUN_MS)) {
    publish_runtime();
    lastRuntimePub = now;
  }
#endif
}