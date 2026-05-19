package app.service;

import app.entities.Carport;
import app.entities.Part;

import java.util.ArrayList;

public class CarportService {

   public void carportToSVGString (Carport carport, String SVGType) {

   }

   public static ArrayList<Part> generatePartsList (Carport carport, ArrayList<Part> listOfParts) {
       ArrayList<Part> partsList = new ArrayList<>();
       Part raft = listOfParts.get(0);
       Part straps = listOfParts.get(1);
       Part post = listOfParts.get(2);

       int postAmount = (carport.getLength()/310 + 1)*2 +1;
       int raftsAmount = carport.getLength()/55 + 1;
       if (carport.isHasShed()){
           postAmount += 3;
       }
       for (int i = 0; i < raftsAmount; i++){
           partsList.add(raft);
       }
       for (int i = 0; i < postAmount; i++){
           partsList.add(post);
       }
       System.out.println(partsList);
       return partsList;
   }

   public double generatePrice (Carport carport) {
       return 0.0;
   }

   public void generateInvoice (Carport carport) {

   }
}