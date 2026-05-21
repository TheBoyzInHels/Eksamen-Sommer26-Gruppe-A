package app.service;

import app.entities.Carport;
import app.entities.Part;
import app.entities.PartsList;

import java.util.ArrayList;
import java.util.HashMap;

public class CarportService {

    public void carportToSVGString(Carport carport, String SVGType) {

    }

    public static PartsList generatePartsList(Carport carport, ArrayList<Part> matchingParts) {
        ArrayList<Part> listOfParts = new ArrayList<>();
        Part raft = matchingParts.get(0);
        Part straps = matchingParts.get(1);
        Part post = matchingParts.get(2);

        int strapsTotalLength = ((carport.getLength() * 2 + carport.getWidth() * 2));
        int strapsAmount = ((strapsTotalLength + straps.getLength() - 1) / straps.getLength());

        int postAmount = (carport.getLength() / 310 + 1) * 2 + 1;
        int raftsAmount = carport.getLength() / 55 + 1;
        if (carport.isHasShed()) {
            postAmount += 3;
        }
        for (int i = 0; i < postAmount; i++) {
            listOfParts.add(post);
        }

        for (int i = 0; i < raftsAmount; i++) {
            listOfParts.add(raft);
        }

        for (int i = 0; i < strapsAmount; i++) {
            listOfParts.add(straps);
        }

        HashMap<Part,Integer> partCounts = new HashMap<>();
        PartsList partsList = new PartsList(partCounts);

        for (Part p : listOfParts){
            partsList.addPart(p);
        }
        return partsList;
    }

    public static ArrayList<Part> findMatchingParts(Carport carport, ArrayList<Part> allParts) {
        ArrayList<Part> listOfRafts = new ArrayList<>();
        ArrayList<Part> listOfStraps = new ArrayList<>();
        ArrayList<Part> listOfPost = new ArrayList<>();

        ArrayList<Part>matchingParts = new ArrayList<>();

        for (Part p : allParts) {
            if (p.getName().equals("Spær")) {
                listOfRafts.add(p);
            }
            if (p.getName().equals("Remme")) {
                listOfStraps.add(p);
            }
            if (p.getName().equals("Stolpe")) {
                listOfPost.add(p);
            }
        }

        Part currentRaft = null;
        for(Part p : listOfRafts){
            if (p.getLength() >= carport.getWidth() ) {
                if(currentRaft == null || p.getLength() < currentRaft.getLength()){
                    currentRaft = p;
                }
            }
        }

        int raftBestTotalLength = Integer.MAX_VALUE;
        if (currentRaft == null) {
            for(Part p : listOfRafts) {
                int raftAmount = ((carport.getWidth()+ p.getLength() - 1) / p.getLength());
                int totalLength = (raftAmount * p.getLength());
                if (totalLength < raftBestTotalLength ) {
                    raftBestTotalLength = totalLength;
                    currentRaft = p;
                }
            }
        }


        Part currentStraps = null;
        for(Part p : listOfStraps) {
            if (p.getLength() >= carport.getLength()) {
                if (currentStraps == null || p.getLength() < currentStraps.getLength()) {
                    currentStraps = p;
                }
            }
        }

        int strapsTotalLength = ((carport.getLength() * 2 + carport.getWidth() * 2));
        int strapsBestTotalLength = Integer.MAX_VALUE;
        if (currentStraps == null) {
            for(Part p : listOfStraps) {

                int strapsAmount = ((strapsTotalLength + p.getLength() - 1) / p.getLength());
                int totalLength = (strapsAmount * p.getLength());
                if (totalLength < strapsBestTotalLength ) {
                    strapsBestTotalLength = totalLength;
                    currentStraps = p;
                }
            }
        }
        matchingParts.add(currentRaft);
        matchingParts.add(currentStraps);
        matchingParts.add(listOfPost.get(0));
        return matchingParts;
    }


    public double generatePrice(Carport carport) {
        return 0.0;
    }

    public void generateInvoice(Carport carport) {

    }
}