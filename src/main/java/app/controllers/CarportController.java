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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/user/carport", ctx -> ctx.render("/carports/carport.html"));
        app.get("/user/saved", ctx -> myCarports(ctx, connectionPool));
        app.post("/user/saved", ctx -> saveCarport(ctx, connectionPool));
        app.post("/user/deleteCarport", ctx -> deleteCarport(ctx, connectionPool));
        app.post("/user/editCarport", ctx -> editCarport(ctx, connectionPool));
        app.post("/user/generatePartsList", ctx -> generatePartsList(ctx, connectionPool));
        app.get("/user/partsList", ctx -> viewPartsList(ctx));
    }

    public static void myCarports(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Carport> carports = CarportMapper.listCarports(connectionPool, UserService.currentUser(ctx));

        ctx.sessionAttribute("carportList", carports);
        ctx.render("carports/saved.html");
    }

    public static void saveCarport(Context ctx, ConnectionPool connectionPool) {
        try {
            Carport carport = CarportService.CarportFromParameters(ctx);

            CarportMapper.saveCarport(connectionPool, carport, ctx);
            Carport newestCarport = CarportMapper.findNewestCarport(connectionPool);

            ctx.sessionAttribute("newestCarport", newestCarport);
            ctx.redirect("/user/saved");
        } catch (NumberFormatException | DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editCarport(Context ctx, ConnectionPool connectionPool) {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        try {
            Carport carport = CarportMapper.findCarport(connectionPool, carportId);

            ctx.sessionAttribute("selectedCarport", carport);
            CarportService.EditWithParameters(ctx, carport);

            CarportMapper.editCarport(connectionPool, carport);
            UserService.resetAttributes(ctx);

            ctx.redirect("/user/saved");
        } catch (DatabaseException | NumberFormatException e) {
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

    public static void generatePartsList(Context ctx, ConnectionPool connectionPool) throws Exception {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        try {
            Carport carport = CarportMapper.findCarport(connectionPool, carportId);

            ArrayList<Part> availableParts = PartMapper.getAvailableParts(connectionPool);
            ArrayList<Part> matchingParts = CarportService.findMatchingParts(carport, availableParts);

            PartsList partsList = CarportService.generatePartsList(carport, matchingParts);
            PdfGenerator.generatePartsListPdf(partsList);
            ctx.redirect("/user/partsList");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void viewPartsList(Context ctx) throws IOException {
        ctx.header("Cache-Control", "no-cache, no-store, must-revalidate");
        ctx.result(Files.readAllBytes(Paths.get("src/main/resources/public/pdf/parts_list.pdf")));
        ctx.contentType("application/pdf");
    }
}

