package app.service;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.ConnectionPool;
import app.persistence.PartMapper;
import io.javalin.http.Context;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class InquiryService {
    public static int generateInquiryPrice(Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        ArrayList<Part> availableParts = PartMapper.getAvailableParts(connectionPool);
        ArrayList<Part> matchingParts = CarportService.findMatchingParts(carport, availableParts);
        PartsList partsList = CarportService.generatePartsList(carport, matchingParts);
        int inquiryPrice = 0;
        for (Map.Entry<Part, Integer> entry : partsList.getParts().entrySet()) {
            Part part = entry.getKey();
            int amount = entry.getValue();
            inquiryPrice += part.getPrice() * amount;
        }
        return inquiryPrice;
    }

    public static Inquiry createInquiryObject(Context ctx, Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        int price = (InquiryService.generateInquiryPrice(carport, connectionPool) * 2);
        CarportMapper.makeCarportFinal(connectionPool, carport);
        String status = "Venter";
        User user = ctx.sessionAttribute("currentUser");
        LocalDate today = LocalDate.now();
        Date sqlDate = Date.valueOf(today);

        return new Inquiry(status, user, carport, sqlDate, price);
    }
}