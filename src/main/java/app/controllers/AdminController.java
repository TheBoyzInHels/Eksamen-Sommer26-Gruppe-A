package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    public void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/admin/seeCustomerEmail", ctx -> seeCustomerEmail(ctx, connectionPool));
        app.post("/admin/setInquiryStatus", ctx -> setInquiryStatus(ctx, connectionPool));
    }

    public void connectToInquiryMapper(Context ctx, ConnectionPool connectionPool, String actiom) {

    }

    public void seeAllInquiries(ConnectionPool connectionPool) {

    }

    public static void setInquiryStatus(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String inquiryStatus = ctx.formParam("setInquiryStatus");
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));

        try {
            InquiryMapper.setInquiryStatus(connectionPool, inquiryId, inquiryStatus);

            ctx.redirect("/inquiry");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void seeCustomerEmail(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));

        String email = InquiryMapper.getCustomerEmail(connectionPool, inquiryId);

        ctx.attribute("customerEmail", email);
        ctx.render("/inquiry");
        }


    public void deleteInquiry(Context ctx, ConnectionPool connectionPool) {

    }
}