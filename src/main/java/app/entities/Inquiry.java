package app.entities;

import java.util.Date;

public class Inquiry {
    private int id;
    private String status;
    private User user;
    private Carport carport;
    private Date date;
    private int price;


    public Inquiry(int id, String status, User user, Carport carport, Date date, int price){
        this.id = id;
        this.status = status;
        this.user = user;
        this.carport = carport;
        this.date = date;
        this.price = price;
    }

    public Inquiry(String status, User user, Carport carport, Date date, int price) {
        this.status = status;
        this.user = user;
        this.carport = carport;
        this.date = date;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
