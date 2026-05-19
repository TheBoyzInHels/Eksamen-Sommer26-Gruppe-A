package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;


public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx ->  ctx.render("login/login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.get("/register", ctx -> ctx.render("login/register.html"));
        app.post("/register", ctx -> register(ctx, connectionPool));
        app.get("/user/profile", ctx -> profile(ctx, connectionPool));

    }

    private static void profile(Context ctx, ConnectionPool connectionPool) {

        ctx.render("/user/profile.html");
    }

    public static void connectToMapper(Context ctx, ConnectionPool connectionPool, String action) {

    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.redirect("/user/carport");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.attribute("error", "Forkert email eller adgangskode.");
            ctx.render("/login/login.html");
        }
    }

    public static void register(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String phoneNumber = ctx.formParam("phoneNumber");

        User user = new User(email,password,firstName,lastName,phoneNumber);
        UserMapper.createUser(user, connectionPool);
        login(ctx, connectionPool);
    }
}
