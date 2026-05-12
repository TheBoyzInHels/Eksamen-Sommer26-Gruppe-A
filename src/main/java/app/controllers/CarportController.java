package app.controllers;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/carport", ctx -> ctx.render("/carports/carport.html"));
        app.post("/submit", ctx -> submitCarport(ctx, connectionPool));
        app.post("/saved", ctx -> saveCarport(ctx, connectionPool));
    }


    public void connectToMapper(Context ctx, ConnectionPool connectionPool) {

    }

    public void seeAllCarports(ConnectionPool connectionPool) {

    }

    public static void submitCarport(Context ctx, ConnectionPool connectionPool) {

    }

    public static void saveCarport(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        try {
            int amountOfCars = Integer.parseInt(ctx.formParam("amountOfCars"));
            int width = Integer.parseInt(ctx.formParam("width"));
            int length = Integer.parseInt(ctx.formParam("length"));
            boolean hasShed = Boolean.parseBoolean(ctx.formParam("hasShed"));
            int shedWidth = Integer.parseInt(ctx.formParam("shedWidth"));
            int shedLength = Integer.parseInt(ctx.formParam("shedLength"));
            boolean hasGutter = Boolean.parseBoolean(ctx.formParam("hasGutter"));

            Carport carport = new Carport(amountOfCars, length, width, hasShed, shedWidth, shedLength, hasGutter);

            CarportMapper.saveCarport(connectionPool, carport, ctx);

            ctx.render("carports/saved.html");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public void editCarport(Context ctx, ConnectionPool connectionPool) {

    }

    public void deleteCarport(Context ctx, ConnectionPool connectionPool) {

    }
}
