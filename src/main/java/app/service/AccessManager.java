package app.service;


import app.entities.User;
import io.javalin.Javalin;

public class AccessManager {

    public static void configureAccess(Javalin app) {
        // /user/ path
        app.before("/user/*", ctx -> {
            User user = ctx.sessionAttribute("currentUser");
            if (user == null) {
                ctx.redirect("/");
            }
        });
        // /admin/ path
        app.before("/admin/*", ctx -> {
            User user = ctx.sessionAttribute("currentUser");
            if (user == null || !user.isAdmin()) {
                ctx.redirect("/");
            }
        });
        // Path is unknown
        configure404(app);
    }

    public static void configure404(Javalin app) {
        app.error(404, ctx -> {
            ctx.status(404);
            ctx.render("404/404.html");
        });
    }
}
