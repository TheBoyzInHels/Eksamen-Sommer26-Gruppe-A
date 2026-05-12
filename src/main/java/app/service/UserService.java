package app.service;

import app.entities.User;
import io.javalin.http.Context;

public class UserService {

    public boolean existingUser(User user) {
        return user != null;
    }

    public static User currentUser(Context ctx) {
        return ctx.sessionAttribute("currentUser");
    }
}
