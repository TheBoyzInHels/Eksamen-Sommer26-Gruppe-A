package app.service;


import app.entities.Part;
import app.entities.PartsList;

import java.util.HashMap;
import java.util.Map;


public class InquiryService {
    public static int generateInquiryPrice(PartsList partsList) {
        int inquiryPrice = 0;
        for (Map.Entry<Part, Integer> entry : partsList.getParts().entrySet()) {
            Part part = entry.getKey();
            int amount = entry.getValue();
            inquiryPrice += part.getPrice() * amount;
        }
        return inquiryPrice;
    }
}