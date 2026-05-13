package app.controllers;

import app.entities.Carport;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.service.InquiryService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.Date;


public class InquiryController {

    public void addRoutes(Javalin javalin, ConnectionPool connectionPool) {

    }

    public void connectToMapper(Context ctx, ConnectionPool connectionPool, String action) {

    }

    public void createInquiry(Context ctx, ConnectionPool connectionPool) {
        String status = ctx.formParam("status");
        User user = ctx.sessionAttribute("currentUser");
        Carport carport = ctx.sessionAttribute();
        LocalDate date = LocalDate.now();
        int price = InquiryService.generatePrice(carport);
    }
}
