package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class PartController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/part", ctx -> getPart(ctx, connectionPool));
    }
    public static void getPart(Context ctx, ConnectionPool connectionPool){

    }
}
