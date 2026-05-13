package app.service;

import app.entities.Carport;

public class InquiryService {

    public void generatePartsList(Carport carport) {
        boolean gutter;
        int length = carport.getLength();
        int width = carport.getWidth();

        if (carport.isHasShed()){
            int shedLength = carport.getShedLength();
            int shedWidth = carport.getShedWidth();
        }
        if (carport.isHasGutter()){
            gutter = true;
        }

        int antalSpær = length/55;
        int totalSpærLængde = antalSpær*width;
        int totalMængdeSpær = totalSpærLængde*600;

    }

}
