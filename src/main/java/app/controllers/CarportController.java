package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

        app.get("/saved", ctx -> seeSavedCarports(ctx, connectionPool));

    }

    public void connectToMapper(Context ctx, ConnectionPool connectionPool) {

    }

    public static void seeSavedCarports(Context ctx, ConnectionPool connectionPool) {
        ctx.render("/carports/saved.html");

    }

    public void seeAllCarports(ConnectionPool connectionPool) {

    }

    public void createCarport(Context ctx, ConnectionPool connectionPool) {

    }

    public void editCarport(Context ctx, ConnectionPool connectionPool) {

    }

    public void deleteCarport(Context ctx, ConnectionPool connectionPool) {

    }
}
