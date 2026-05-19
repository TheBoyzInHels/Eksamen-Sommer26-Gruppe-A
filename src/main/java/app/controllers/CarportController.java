package app.controllers;

import app.entities.Carport;
import app.entities.Part;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.service.CarportService;
import app.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CarportController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/carport", ctx -> ctx.render("/carports/carport.html"));
        app.post("/submit", ctx -> submitCarport(ctx, connectionPool));
        app.get("/saved", ctx -> myCarports(ctx, connectionPool));
        app.post("/saved", ctx -> saveCarport(ctx, connectionPool));
        app.post("/deleteCarport", ctx -> deleteCarport(ctx, connectionPool));
        app.post("/editCarport", ctx -> editCarport(ctx, connectionPool));
        app.post("/generatePartsList", ctx -> generatePartsList(ctx, connectionPool));
    }


    public void connectToMapper(Context ctx, ConnectionPool connectionPool) {

    }

    public static void seeSavedCarports(Context ctx, ConnectionPool connectionPool) {
        ctx.render("carports/saved.html");

    }

    public void allCarports(ConnectionPool connectionPool) {


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

            ctx.redirect("/saved");
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

            carport.setLength(length);
            carport.setWidth(width);

            CarportMapper.editCarport(connectionPool, carport);

            ctx.redirect("/saved");

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

            ctx.redirect("/saved");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generatePartsList(Context ctx, ConnectionPool connectionPool) {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        try {
            Carport carport2 = CarportMapper.findCarport(connectionPool, carportId);
            //ArrayList<Part> partsList = CarportService.generatePartsList(carport); //Vi kommer nok til at bruge denne
            Carport carport = new Carport(4, 2, 780, 600, true, 220, 530, true);
            ArrayList<Part> listOfParts = new ArrayList<>();

            Part raft = new Part(1, "Spær", 479.70);
            Part straps = new Part(2, "Remme", 479.70);
            Part post= new Part(3, "Stolpe", 221.85);

            listOfParts.add(raft);
            listOfParts.add(straps);
            listOfParts.add(post);

            ArrayList<Part> partsList = CarportService.generatePartsList(carport, listOfParts); //Tester med en oprette carport

        }catch(DatabaseException e){
            throw new RuntimeException(e);
        }
    }
    public static void testGeneratePartsList() {



            //ArrayList<Part> partsList = CarportService.generatePartsList(carport); //Vi kommer nok til at bruge denne
            Carport carport = new Carport(4, 2, 780, 600, true, 220, 530, true);
            ArrayList<Part> listOfParts = new ArrayList<>();

            Part raft = new Part(1, "Spær", 479.70);
            Part straps = new Part(2, "Remme", 479.70);
            Part post= new Part(3, "Stolpe", 221.85);

            listOfParts.add(raft);
            listOfParts.add(straps);
            listOfParts.add(post);

            ArrayList<Part> partsList = CarportService.generatePartsList(carport, listOfParts); //Tester med en oprette carport
    }
}

