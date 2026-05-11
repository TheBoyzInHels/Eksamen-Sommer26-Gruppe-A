package app.service;

import app.entities.User;

public class LoginService {

    public boolean existingUser(User user) {
        return user != null;
    }
}
