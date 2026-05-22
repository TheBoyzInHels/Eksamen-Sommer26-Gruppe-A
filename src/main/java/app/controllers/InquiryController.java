package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import app.persistence.PartMapper;
import app.service.CarportService;
import app.service.InquiryService;
import app.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InquiryController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/user/inquiries", ctx -> myInquiries(ctx, connectionPool));
        app.post("/user/inquiry", ctx -> createInquiry(ctx, connectionPool));
        app.get("/user/inquiry", ctx -> myInquiries(ctx, connectionPool));
        app.post("/user/deleteInquiry", ctx -> deleteInquiry(ctx, connectionPool));
        app.post("/user/completeInquiryPayment", ctx -> completeInquiryPayment(ctx, connectionPool));
        app.post("/user/createInvoice", ctx -> createInvoice(ctx, connectionPool));
    }


    public static void seeSubmittedInquiries(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Inquiry> inquiries = InquiryMapper.listInquiry(connectionPool, null);

        ctx.sessionAttribute("inquiryList", inquiries);
        ctx.render("inquiries/allinquiries");
    }

    public static void myInquiries(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Inquiry> inquiries = InquiryMapper.listInquiry(connectionPool, UserService.currentUser(ctx));

        ctx.sessionAttribute("inquiryList", inquiries);
        ctx.render("inquiries/inquiry.html");
    }

    public static void createInquiry(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        Carport carport;

        String selectedCarportId = ctx.formParam("selectedCarportId");

        try {

            if (selectedCarportId != null && !selectedCarportId.isEmpty()) {

                int carportId = Integer.parseInt(selectedCarportId);

                carport = CarportMapper.findCarport(connectionPool, carportId);

            } else {

                int amountOfCars = Integer.parseInt(ctx.formParam("amountOfCars"));
                int width = Integer.parseInt(ctx.formParam("width"));
                int length = Integer.parseInt(ctx.formParam("length"));
                boolean hasShed = Boolean.parseBoolean(ctx.formParam("hasShed"));
                int shedWidth = Integer.parseInt(ctx.formParam("shedWidth"));
                int shedLength = Integer.parseInt(ctx.formParam("shedLength"));
                boolean hasGutter = Boolean.parseBoolean(ctx.formParam("hasGutter"));
                String notes = ctx.formParam("notes");

                carport = new Carport(amountOfCars, length, width, hasShed, shedWidth, shedLength, hasGutter, notes);

                CarportMapper.saveCarport(connectionPool, carport, ctx);

                carport = CarportMapper.findNewestCarport(connectionPool, ctx);
            }

            ArrayList<Part> availableParts = PartMapper.getAvailableParts(connectionPool);
            ArrayList<Part> matchingParts = CarportService.findMatchingParts(carport, availableParts);
            PartsList partsList = CarportService.generatePartsList(carport, matchingParts);

            int price = InquiryService.generateInquiryPrice(partsList);

            CarportMapper.makeCarportFinal(ctx, connectionPool, carport);

            User user = ctx.sessionAttribute("currentUser");

            Inquiry inquiry = new Inquiry("Venter", user, carport, Date.valueOf(LocalDate.now()), price);

            InquiryMapper.createInquiry(connectionPool, inquiry, carport, user);

            ctx.redirect("/user/inquiry");

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteInquiry(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        try {
            InquiryMapper.deleteInquiry(connectionPool, inquiryId);

            ctx.redirect("/user/inquiry");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void completeInquiryPayment(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        String status = ctx.formParam("status");
        try {
            InquiryMapper.changeInquiryStatus(connectionPool, inquiryId, status);

            ctx.render("inquiries/paymentComplete");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

    }

    public static void createInvoice(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        User user = ctx.sessionAttribute("currentUser");

        try {
            assert user != null;
            Inquiry chosenInquiry = InquiryMapper.findInquiry(connectionPool, inquiryId);

            ctx.attribute("user", user);

            ctx.attribute("inquiry", chosenInquiry);

            ctx.attribute("price", chosenInquiry.getPrice());

            ctx.render("invoice/invoice.html");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

    }

}
