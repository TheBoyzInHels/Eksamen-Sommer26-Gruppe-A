package app.service;

import app.entities.Part;
import app.entities.PartsList;

public class PartService {
    public static void printPartsList (PartsList partsList){
        System.out.println("Antal: " + " Mål " + " Materialetyper ");
        for (Part p : partsList.getParts().keySet()){
            int count = partsList.getParts().get(p);

            System.out.println(count + " x " + p.getName() + " " + p.getDescription());
        }
    }
}
