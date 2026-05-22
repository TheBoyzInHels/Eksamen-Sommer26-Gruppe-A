package app.service;

import app.entities.Carport;
import app.entities.Part;
import app.entities.PartsList;

import java.util.ArrayList;
import java.util.HashMap;

public class CarportService {

    public void carportToSVGString(Carport carport, String SVGType) {

    }

    public static ArrayList<Part> findMatchingParts(Carport carport, ArrayList<Part> allParts) {
        ArrayList<Part> listOfRafts = new ArrayList<>();
        ArrayList<Part> listOfStraps = new ArrayList<>();
        ArrayList<Part> listOfPost = new ArrayList<>();
        ArrayList<Part> listOfGutters = new ArrayList<>();

        ArrayList<Part> matchingParts = new ArrayList<>();

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
            if (p.getName().equals("Tagrende")) {
                listOfGutters.add(p);
            }
        }

        Part currentRaft = null;
        for (Part p : listOfRafts) {
            if (p.getLength() >= carport.getWidth()) {
                if (currentRaft == null || p.getLength() < currentRaft.getLength()) {
                    currentRaft = p;
                }
            }
        }

        int raftBestTotalLength = Integer.MAX_VALUE;
        if (currentRaft == null) {
            for (Part p : listOfRafts) {
                int raftAmount = ((carport.getWidth() + p.getLength() - 1) / p.getLength());
                int totalLength = (raftAmount * p.getLength());
                if (totalLength < raftBestTotalLength) {
                    raftBestTotalLength = totalLength;
                    currentRaft = p;
                }
            }
        }


        Part currentStraps = null;
        for (Part p : listOfStraps) {
            if (p.getLength() >= carport.getLength()) {
                if (currentStraps == null || p.getLength() < currentStraps.getLength()) {
                    currentStraps = p;
                }
            }
        }

        int strapsTotalLength = ((carport.getLength() + carport.getWidth()) * 2);
        int strapsBestTotalLength = Integer.MAX_VALUE;
        if (currentStraps == null) {
            for (Part p : listOfStraps) {

                int strapsAmount = ((strapsTotalLength + p.getLength() - 1) / p.getLength());
                int totalLength = (strapsAmount * p.getLength());
                if (totalLength < strapsBestTotalLength) {
                    strapsBestTotalLength = totalLength;
                    currentStraps = p;
                }
            }
        }
        int shedStrapsTotalLength = (carport.getShedLength() + carport.getShedWidth() * 2);
        int shedStrapsBestTotalLength = Integer.MAX_VALUE;
        Part currentShedStraps = null;
        if (carport.isHasShed()) {
            if (currentShedStraps == null) {
                for (Part p : listOfStraps) {

                    int strapsAmount = ((shedStrapsTotalLength + p.getLength() - 1) / p.getLength());
                    int totalLength = (strapsAmount * p.getLength());
                    if (totalLength < shedStrapsBestTotalLength) {
                        shedStrapsBestTotalLength = totalLength;
                        currentShedStraps = p;
                    }
                }
            }
        }
        int gutterTotalLength = (carport.getLength());
        int guttersBestTotalLength = Integer.MAX_VALUE;
        Part currentGutter = null;
        if (carport.isHasGutter()) {
            if (currentGutter == null) {
                for (Part p : listOfGutters) {
                    int gutterAmount = (gutterTotalLength + p.getLength() -1 / p.getLength());
                    int totalLength = (gutterAmount + p.getLength());
                    if(totalLength < guttersBestTotalLength){
                        guttersBestTotalLength = totalLength;
                        currentGutter = p;
                    }

                }
            }
        }
        matchingParts.add(currentRaft);
        matchingParts.add(currentStraps);
        matchingParts.add(currentShedStraps);
        matchingParts.add(listOfPost.get(0));
        matchingParts.add(currentGutter);

        return matchingParts;
    }

    public static PartsList generatePartsList(Carport carport, ArrayList<Part> matchingParts) {
        ArrayList<Part> listOfParts = new ArrayList<>();

        Part raft = matchingParts.get(0);
        Part straps = matchingParts.get(1);

        Part post = matchingParts.get(3);

        int raftsAmount = carport.getLength() / 55 + 1;
        int strapsTotalLength = ((carport.getLength() + carport.getWidth()) * 2);
        if (carport.isHasShed()) {
            Part shedStraps = matchingParts.get(2);
            int shedStrapsTotalLength = ((carport.getShedLength() + carport.getShedWidth()) * 2);
            int shedStrapsAmount = ((shedStrapsTotalLength + shedStraps.getLength() - 1) / shedStraps.getLength());
            for (int i = 0; i < shedStrapsAmount; i++) {
                listOfParts.add(shedStraps);
            }
        }
        int strapsAmount = ((strapsTotalLength + straps.getLength() - 1) / straps.getLength());
        if (carport.isHasGutter()) {
            Part gutter = matchingParts.get(4);
            int guttersAmount = ((strapsTotalLength + gutter.getLength() - 1) / gutter.getLength());
            for (int i = 0; i < guttersAmount; i++) {
                listOfParts.add(gutter);
            }
        }

        int postAmount = (carport.getLength() / 310 + 1) * 2 + 1;

        if (carport.isHasShed()) {
            postAmount += 3;
        }

        for (int i = 0; i < raftsAmount; i++) {
            listOfParts.add(raft);
        }

        for (int i = 0; i < strapsAmount; i++) {
            listOfParts.add(straps);
        }

        for (int i = 0; i < postAmount; i++) {
            listOfParts.add(post);
        }


        HashMap<Part, Integer> partCounts = new HashMap<>();
        PartsList partsList = new PartsList(partCounts);

        for (Part p : listOfParts) {
            partsList.addPart(p);
        }
        return partsList;
    }


    public double generatePrice(Carport carport) {
        return 0.0;
    }

    public void generateInvoice(Carport carport) {

    }
}