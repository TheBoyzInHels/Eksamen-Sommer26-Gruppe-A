package app.service;


import app.entities.User;
import io.javalin.Javalin;

public class AccessManager {

    public static void configureAccess(Javalin app) {

        app.before("/user/*", ctx -> {

            User user = ctx.sessionAttribute("currentUser");

            if (user == null) {
                ctx.redirect("/");
            }
        });

        app.before("/admin/*", ctx -> {

            User user = ctx.sessionAttribute("currentUser");

            if (user == null || !user.isAdmin()) {
                ctx.redirect("/");
            }
        });
    }
}
