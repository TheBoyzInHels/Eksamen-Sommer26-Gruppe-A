package app.controllers;
import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/admin/inquiries", ctx -> seeAllInquiries(ctx, connectionPool));
        app.post("/admin/seeCustomerEmail", ctx -> seeCustomerEmail(ctx, connectionPool));
        app.post("/admin/setInquiryStatus", ctx -> setInquiryStatus(ctx, connectionPool));
        app.post("/admin/deleteInquiry", ctx -> deleteInquiry(ctx, connectionPool));
        app.post("/admin/seeCarportUnderInquiries", ctx -> seeCarportUnderInquiries(ctx, connectionPool));
    }


    public static void seeAllInquiries(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        InquiryController.seeSubmittedInquiries(ctx, connectionPool);

        ctx.sessionAttribute("customerEmail", null);
        ctx.sessionAttribute("selectedCarport", null);
    }

    public static void seeCarportUnderInquiries(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));

       Carport carport = CarportMapper.findCarport(connectionPool, carportId);

        ctx.sessionAttribute("customerEmail", null);

        ctx.sessionAttribute("selectedCarport", carport);
        ctx.redirect("/admin/inquiries");

    }

    public static void setInquiryStatus(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String inquiryStatus = ctx.formParam("setInquiryStatus");
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));

        ctx.sessionAttribute("customerEmail", null);
        ctx.sessionAttribute("selectedCarport", null);

        try {
            InquiryMapper.setInquiryStatus(connectionPool, inquiryId, inquiryStatus);

            ctx.redirect("/admin/inquiries");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void seeCustomerEmail(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));

        String email = InquiryMapper.getCustomerEmail(connectionPool, inquiryId);

        ctx.sessionAttribute("selectedCarport", null);

        ctx.sessionAttribute("customerEmail", email);
        ctx.redirect("/admin/inquiries");
        }


    public static void deleteInquiry(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        try {
            InquiryMapper.deleteInquiry(connectionPool, inquiryId);

            ctx.redirect("/admin/inquiries");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}