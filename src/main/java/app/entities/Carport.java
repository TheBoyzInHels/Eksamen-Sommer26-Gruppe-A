package app.entities;

public class Carport {

    /* v Variables v */
    int carportId;
    int amountOfCars;
    int length;
    int width;
    boolean hasShed;
    int shedLength;
    int shedWidth;
    boolean hasGutter;
    int userId;

    /* v Constructors v */
    public Carport(int carportId, int amountOfCars, int length, int width, boolean hasShed, int shedLength, int shedWidth, boolean hasGutter, int userId) {
        this.carportId = carportId;
        this.amountOfCars = amountOfCars;
        this.length = length;
        this.width = width;
        this.hasShed = hasShed;
        this.shedWidth = shedWidth;
        this.shedLength = shedLength;
        this.hasGutter = hasGutter;
        this.userId = userId;
    }

    public Carport(int carportId, int amountOfCars, int length, int width, boolean hasShed, int shedLength, int shedWidth, boolean hasGutter) {
        this.carportId = carportId;
        this.amountOfCars = amountOfCars;
        this.length = length;
        this.width = width;
        this.hasShed = hasShed;
        this.shedWidth = shedWidth;
        this.shedLength = shedLength;
        this.hasGutter = hasGutter;
    }

    public Carport(int amountOfCars, int length, int width, boolean hasShed, int shedLength, int shedWidth, boolean hasGutter) {
        this.amountOfCars = amountOfCars;
        this.length = length;
        this.width = width;
        this.hasShed = hasShed;
        this.shedWidth = shedWidth;
        this.shedLength = shedLength;
        this.hasGutter = hasGutter;
    }

    /* v Getters & Setters v */
    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }

    public int getAmountOfCars() {
        return amountOfCars;
    }

    public void setAmountOfCars(int amountOfCars) {
        this.amountOfCars = amountOfCars;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isHasShed() {
        return hasShed;
    }

    public void setHasShed(boolean hasShed) {
        this.hasShed = hasShed;
    }

    public int getShedWidth() {
        return shedWidth;
    }

    public void setShedWidth(int shedWidth) {
        this.shedWidth = shedWidth;
    }

    public int getShedLength() {
        return shedLength;
    }

    public void setShedLength(int shedLength) {
        this.shedLength = shedLength;
    }

    public boolean isHasGutter() {
        return hasGutter;
    }

    public void setHasGutter(boolean hasGutter) {
        this.hasGutter = hasGutter;
    }
}
