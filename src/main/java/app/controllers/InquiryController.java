package app.controllers;

import app.entities.Carport;
import app.entities.Inquiry;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import app.service.InquiryService;
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
    app.get("/user/inquiries", ctx -> myInquiries(ctx, connectionPool));
    app.post("/user/inquiry", ctx -> createInquiry(ctx, connectionPool));
    app.get("/user/inquiry", ctx -> myInquiries(ctx, connectionPool));
    app.post("/user/deleteInquiry", ctx -> deleteInquiry(ctx, connectionPool));
    app.post("/completeInquiryPayment", ctx -> completeInquiryPayment(ctx, connectionPool));
    }


    public static void seeSubmittedInquiries(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Inquiry> inquiries = InquiryMapper.listInquiry(connectionPool, null);

        ctx.sessionAttribute("inquiryList", inquiries);
        ctx.render("inquiries/allinquiries");
    }

    public static void myInquiries (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        List<Inquiry> inquiries = InquiryMapper.listInquiry(connectionPool, UserService.currentUser(ctx));

        ctx.sessionAttribute("inquiryList", inquiries);
        ctx.render("inquiries/inquiry.html");
    }

    public static void createInquiry(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        CarportController.saveCarport(ctx, connectionPool);
        Carport carport = ctx.sessionAttribute("newestCarport");

        String status = "venter";

        User user = ctx.sessionAttribute("currentUser");

        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);
        int price = 0;

        try {
            Inquiry inquiry = new Inquiry(status, user, carport, sqlDate, price);
            InquiryMapper.createInquiry(connectionPool, inquiry, carport, user);

            myInquiries(ctx,connectionPool);
            ctx.redirect("/user/inquiry");
        } catch (RuntimeException e) {
            throw new DatabaseException("Fejl ved createInquiry eller Databse" + e.getMessage());
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

    /*public static void createInvoice(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        User user = ctx.sessionAttribute("currentUser");

        try {
            InquiryService.createInvoice(inquiryId, user);
        }

    }*/
}
