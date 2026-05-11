package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class UserController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx ->  ctx.render("login/login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));

        app.get("/register", ctx -> ctx.render("login/register.html"));
        app.post("/register", ctx -> register(ctx, connectionPool));

    }

    public static void connectToMapper(Context ctx, ConnectionPool connectionPool, String action) {

    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            ctx.render("/carports/carport.html");
        } catch (DatabaseException e) {
            ctx.attribute("msg", e.getMessage());
            ctx.render("login.html");
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
        ctx.sessionAttribute("currentUser", user);
        ctx.render("/carports/carport.html");
    }
}
