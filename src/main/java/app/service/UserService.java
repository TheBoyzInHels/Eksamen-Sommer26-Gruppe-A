package app.service;

import app.entities.User;
import io.javalin.http.Context;

public class UserService {

    public static User currentUser(Context ctx) {
        return ctx.sessionAttribute("currentUser");
    }

    public static void resetAttributes(Context ctx) {
        ctx.sessionAttribute("customerEmail", null);
        ctx.sessionAttribute("selectedCarport", null);
    }
}

