package app.entities;

import java.sql.Date;

public class Inquiry {
    private int inquiryId;
    private String status;
    private int userId;
    private User user;
    private int carportId;
    private Carport carport;
    private Date creationDate;
    private int price;


    public Inquiry(int id, String status, int userId, int carportId, Date creationDate, int price){
        this.inquiryId = id;
        this.status = status;
        this.userId = userId;

        this.carportId = carportId;
        this.creationDate = creationDate;
        this.price = price;
    }

    public Inquiry(String status, User user, Carport carport, Date creationDate, int price) {
        this.status = status;
        this.user = user;
        this.carport = carport;
        this.creationDate = creationDate;
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Carport getCarport() {
        return carport;
    }

    public void setCarport(Carport carport) {
        this.carport = carport;
    }

    public Date getDate() {return creationDate;}

    public int getPrice() {
        return price;
    }

    public int getInquiryId() { return inquiryId; }

    public int getCarportId() { return carportId; }
}
