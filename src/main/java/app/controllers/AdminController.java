package app.controllers;

import app.entities.Carport;
import app.entities.Inquiry;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.InquiryMapper;
import app.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/admin/inquiries", ctx -> seeAllInquiries(ctx, connectionPool));
        app.post("/admin/seeCustomerInfo", ctx -> seeCustomerInfo(ctx, connectionPool));
        app.post("/admin/setInquiryStatus", ctx -> setInquiryStatus(ctx, connectionPool));
        app.post("/admin/deleteInquiry", ctx -> deleteInquiry(ctx, connectionPool));
        app.post("/admin/seeCarportUnderInquiries", ctx -> seeCarportUnderInquiry(ctx, connectionPool));
    }

    public static void seeAllInquiries(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        InquiryController.seeInquiries(ctx, connectionPool, true);

        UserService.resetAttributes(ctx);
    }

    public static void seeCarportUnderInquiry(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int carportId = Integer.parseInt(ctx.formParam("selectedCarportId"));
        Carport carport = CarportMapper.findCarport(connectionPool, carportId);

        UserService.resetAttributes(ctx);
        ctx.sessionAttribute("selectedCarport", carport);
        ctx.redirect("/admin/inquiries");
    }

    public static void setInquiryStatus(Context ctx, ConnectionPool connectionPool) {
        String inquiryStatus = ctx.formParam("setInquiryStatus");
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        UserService.resetAttributes(ctx);
        try {
            InquiryMapper.setInquiryStatus(connectionPool, inquiryId, inquiryStatus);

            ctx.redirect("/admin/inquiries");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void seeCustomerInfo(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        String userId = InquiryMapper.getCustomerInfo(connectionPool, inquiryId, "user_id");
        String email = InquiryMapper.getCustomerInfo(connectionPool, inquiryId, "email");
        String firstName = InquiryMapper.getCustomerInfo(connectionPool, inquiryId, "first_name");
        String lastName = InquiryMapper.getCustomerInfo(connectionPool, inquiryId, "last_name");
        String phoneNumber = InquiryMapper.getCustomerInfo(connectionPool, inquiryId, "phone_number");

        UserService.resetAttributes(ctx);
        ctx.sessionAttribute("customerId", userId);
        ctx.sessionAttribute("customerEmail", email);
        ctx.sessionAttribute("customerFirstName", firstName);
        ctx.sessionAttribute("customerLastName", lastName);
        ctx.sessionAttribute("customerPhoneNumber", phoneNumber);
        ctx.redirect("/admin/inquiries");
    }


    public static void deleteInquiry(Context ctx, ConnectionPool connectionPool) {
        int inquiryId = Integer.parseInt(ctx.formParam("selectedInquiryId"));
        try {
            Inquiry inquiry = InquiryMapper.findInquiry(connectionPool, inquiryId);

            InquiryMapper.deleteInquiry(connectionPool, inquiryId);
            CarportMapper.deleteCarport(connectionPool, inquiry.getCarportId());

            ctx.redirect("/admin/inquiries");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}