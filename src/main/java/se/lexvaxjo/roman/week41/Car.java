package se.lexvaxjo.roman.week41;

import java.util.Objects;

/**
 * Represents a car with brand, registration number, owner, max speed and mileage..
 */

public class Car {
    private final String brand;
    private final String regNumber;
    private final short maxSpeed;
    private final String nameOfOwner;
    private final int mileage;

    /**
     * Constructs a new Car object with the given parameters.
     *
     * @param brand   The brand of the car.
     * @param regNumber   The registration number of the car.
     * @param owner  The name of the car owner.
     * @param maxSpeed   The maximum speed of the car.
     * @param mileage  The mileage of the car.
     */
    public Car(String brand, String regNumber, String owner, short maxSpeed, int mileage){
        this.brand = Objects.requireNonNull(brand, "Brand can not be nul").trim();
        this.regNumber = Objects.requireNonNull(regNumber,"Reg number can not be empty").trim();
        this.nameOfOwner = Objects.requireNonNull(owner,"Owner name can not be null");
        if(maxSpeed<=0)
            throw new  IllegalArgumentException("maxSpeed must be > 0");
        if(mileage<0)
            throw new  IllegalArgumentException("maxSpeed must be >= 0");
        this.mileage = mileage;
        this.maxSpeed = maxSpeed;

    }
    public String carInfo(){
        return String.format("Car info:%n"+
                "brand:      %s%n" +
                "reg number: %s%n" +
                "Owner:      %s%n" +
                "max speed:  %s%n"+
                "mileage:    %d%n",
                brand, regNumber, nameOfOwner, maxSpeed, mileage);
    }
}
