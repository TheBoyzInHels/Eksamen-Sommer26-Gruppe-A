package app.service;

import app.entities.Carport;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

public class UserService {

    public boolean existingUser(User user) {
        return user != null;
    }

    public static User currentUser(Context ctx) {
        return ctx.sessionAttribute("currentUser");
    }
    public static void generatePartsList(Carport carport){
        

    }
}
