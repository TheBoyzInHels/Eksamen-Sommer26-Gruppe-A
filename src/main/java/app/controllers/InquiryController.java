package app.controllers;

import app.entities.Carport;
import app.entities.Inquiry;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import app.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class InquiryController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
    app.get("/inquiries", ctx -> ctx.render("/inquiries/inquiry.html"));
    app.post("submit", ctx -> submitInquiry(ctx, connectionPool));
    app.get("/inquiry", ctx -> myInquiries(ctx, connectionPool));
    app.post("/deleteInquiry", ctx -> deleteInquiry(ctx, connectionPool));
    }

    public void connectToMapper(Context ctx, ConnectionPool connectionPool, String action) {

    }

    public static void submitInquiry(Context ctx, ConnectionPool connectionPool) {

    }

    public static void seeSubmittedInquiries(Context ctx, ConnectionPool connectionPool) {
        ctx.render("inquiries/inquiry");
    }

    public static void myInquiries (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Inquiry> inquiries = InquiryMapper.listInquiry(connectionPool, UserService.currentUser(ctx));

        ctx.sessionAttribute("inquiryList", inquiries);
        ctx.render("inquiries/inquiry.html");
    }

    public void createInquiry(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        int amountOfCars = Integer.parseInt(ctx.formParam("amountOfCars"));
        int width = Integer.parseInt(ctx.formParam("width"));
        int length = Integer.parseInt(ctx.formParam("length"));
        boolean hasShed = Boolean.parseBoolean(ctx.formParam("hasShed"));
        int shedWidth = Integer.parseInt(ctx.formParam("shedWidth"));
        int shedLength = Integer.parseInt(ctx.formParam("shedLength"));
        boolean hasGutter = Boolean.parseBoolean(ctx.formParam("hasGutter"));

        Carport carport = new Carport(amountOfCars, length, width, hasShed, shedWidth, shedLength, hasGutter);

        String status = "Udkast";

        User user = ctx.sessionAttribute("currentUser");

        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);
        int price = 0;

        try {
            Inquiry inquiry = new Inquiry(status, user, carport, sqlDate, price);
            InquiryMapper.createInquiry(connectionPool, inquiry, carport, user);

            ctx.redirect("/inquiry");
        } catch (RuntimeException e) {
            throw new DatabaseException("Fejl ved createInquiry eller Databse" + e.getMessage());
        }
    }

    public static void deleteInquiry(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        try {
            InquiryMapper.deleteInquiry(connectionPool, inquiryId);

            ctx.redirect("/inquiry");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
