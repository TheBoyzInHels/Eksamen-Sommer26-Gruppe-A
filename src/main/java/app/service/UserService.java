package app.service;

import app.entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserService {

    public boolean existingUser(User user) {
        return user != null;
    }

    public static User currentUser(Context ctx) {
        return ctx.sessionAttribute("currentUser");
    }

    public static void userAuthentication(Javalin app, String controllerName) {
        app.before("//*", ctx -> {
            if (ctx.sessionAttribute("currentUser") == null) {
                ctx.redirect("/");
            }
        });
        app.before("/" + controllerName + "/*", ctx -> {
            if (ctx.sessionAttribute("currentUser") == null) {
                ctx.redirect("/");
            }
        });
        app.before("/" + controllerName + "/*", ctx -> {
            if (ctx.sessionAttribute("currentUser") == null) {
                ctx.redirect("/");
            }
        });
    }

    public static void adminAuthentication(Javalin app) {
        app.before("/admin/*", ctx -> {
            if (currentUser(ctx) == null || !currentUser(ctx).isAdmin()) {
                ctx.redirect("/");
            }
        });
    }

    public static void isLoggedIn(Context ctx) {
        User currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.redirect("/");
        }
    }
}

