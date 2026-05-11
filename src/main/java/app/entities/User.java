package app.entities;

public class User {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isAdmin;


    public User(int id, String email, String password, String firstName, String lastName, String phoneNumber, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }

    public User (int id, String password) {
        this.id = id;
        this.password = password;
    }

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

}
