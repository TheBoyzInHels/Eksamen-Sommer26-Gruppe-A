package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import app.service.InquiryService;
import app.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class InquiryController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/user/inquiry", ctx -> createInquiry(ctx, connectionPool));
        app.get("/user/inquiries", ctx -> seeInquiries(ctx, connectionPool, false));
        app.post("/user/deleteInquiry", ctx -> deleteInquiry(ctx, connectionPool));
        app.post("/user/completeInquiryPayment", ctx -> completeInquiryPayment(ctx, connectionPool));
        app.post("/user/createInvoice", ctx -> createInvoice(ctx, connectionPool));
        app.post("/user/createInquiryFromSaved", ctx -> createInquiryFromSaved(ctx, connectionPool));
    }

    public static void seeInquiries(Context ctx, ConnectionPool connectionPool, boolean isAdminView) throws DatabaseException {
        User user = UserService.currentUser(ctx);
        String html = "inquiries/inquiries.html";
        if(isAdminView) {
            user = null;
            html = "inquiries/allInquiries.html";
        }
        List<Inquiry> inquiries = InquiryMapper.listInquiry(connectionPool, user);

        ctx.sessionAttribute("inquiryList", inquiries);
        ctx.render(html);
    }

    public static void createInquiry(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        CarportController.saveCarport(ctx, connectionPool);
        Carport carport = ctx.sessionAttribute("newestCarport");
        try {
            Inquiry inquiry = InquiryService.createInquiryObject(ctx, carport, connectionPool);

            InquiryMapper.createInquiry(connectionPool, inquiry);

            seeInquiries(ctx, connectionPool, false);
            ctx.redirect("/user/inquiries");
        } catch (RuntimeException e) {
            throw new DatabaseException("Fejl ved createInquiry eller Database" + e.getMessage());
        }
    }

    public static void createInquiryFromSaved(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        try {
            Carport carport = CarportMapper.findCarport(connectionPool, carportId);

            Inquiry inquiry = InquiryService.createInquiryObject(ctx, carport, connectionPool);

            InquiryMapper.createInquiry(connectionPool, inquiry);
            CarportMapper.makeCarportFinal(connectionPool, carport);

            ctx.redirect("/user/inquiries");
        } catch (Exception e) {
            throw new DatabaseException("Fejl i createInquiryFromSaved: " + e.getMessage());
        }
    }

    public static void deleteInquiry(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        try {
            Inquiry inquiry = InquiryMapper.findInquiry(connectionPool, inquiryId);

            InquiryMapper.deleteInquiry(connectionPool, inquiryId);
            CarportMapper.deleteCarport(connectionPool, inquiry.getCarportId());

            ctx.redirect("/user/inquiries");
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
