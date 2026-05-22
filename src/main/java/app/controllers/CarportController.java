package app.controllers;

import app.entities.Carport;
import app.entities.Part;
import app.entities.PartsList;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.PartMapper;
import app.service.CarportService;
import app.service.PdfGenerator;
import app.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/user/carport", ctx -> ctx.render("/carports/carport.html"));
        app.post("/user/submit", ctx -> submitCarport(ctx, connectionPool));
        app.get("/user/saved", ctx -> myCarports(ctx, connectionPool));
        app.post("/user/saved", ctx -> saveCarport(ctx, connectionPool));
        app.post("/user/deleteCarport", ctx -> deleteCarport(ctx, connectionPool));
        app.post("/user/editCarport", ctx -> editCarport(ctx, connectionPool));
        app.post("/user/generatePartsList", ctx -> generatePartsList(ctx, connectionPool));
        app.get("/user/partslist", ctx -> {ctx.header("Cache-Control", "no-cache, no-store, must-revalidate");
            ctx.result(Files.readAllBytes(Paths.get("src/main/resources/public/pdf/partslist.pdf")));
            ctx.contentType("application/pdf");
        });
    }


    public static void myCarports(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Carport> carports = CarportMapper.listCarports(connectionPool, UserService.currentUser(ctx));

        ctx.sessionAttribute("carportList", carports);
        ctx.render("carports/saved.html");
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
            Carport newestCarport = CarportMapper.findNewestCarport(connectionPool, ctx);
            ctx.sessionAttribute("newestCarport", newestCarport);

            ctx.redirect("/user/saved");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editCarport(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int carportId = Integer.parseInt(ctx.formParam("carportId"));

        try {
            Carport carport = CarportMapper.findCarport(connectionPool, carportId);
            ctx.sessionAttribute("selectedCarport", carport);

            int length = Integer.parseInt(ctx.formParam("length"));
            int width = Integer.parseInt(ctx.formParam("width"));
            boolean hasShed = Boolean.parseBoolean(ctx.formParam("hasShed"));
            int shedLength = Integer.parseInt(ctx.formParam("shedLength"));
            int shedWidth = Integer.parseInt(ctx.formParam("shedWidth"));
            boolean hasGutter = Boolean.parseBoolean(ctx.formParam("hasGutter"));

            carport.setLength(length);
            carport.setWidth(width);
            carport.setHasShed(hasShed);
            carport.setShedLength(shedLength);
            carport.setShedWidth(shedWidth);
            carport.setHasGutter(hasGutter);

            CarportMapper.editCarport(connectionPool, carport);

            ctx.redirect("/user/saved");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteCarport(Context ctx, ConnectionPool connectionPool) {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        try {
            CarportMapper.deleteCarport(connectionPool, carportId);

            ctx.redirect("/user/saved");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generatePartsList(Context ctx, ConnectionPool connectionPool) {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        try {
            Carport carport = CarportMapper.findCarport(connectionPool, carportId);

            ArrayList<Part> availableParts = PartMapper.getAvailableParts(connectionPool);
            ArrayList<Part> matchingParts = CarportService.findMatchingParts(carport, availableParts);

            PartsList partsList = CarportService.generatePartsList(carport, matchingParts);
            try {
                PdfGenerator.generatePartsListPdf(partsList);
            }catch(Exception e){
                e.getMessage();
            }
            ctx.redirect("/user/partslist");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}

