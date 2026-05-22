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
        int strapsTotalLength = ((carport.getLength() + carport.getWidth()) * 2);
        int shedStrapsTotalLength = (carport.getShedLength() + carport.getShedWidth() * 2);
        int gutterTotalLength = (carport.getLength());

        Part currentStraps = getCurrentPart(strapsTotalLength, listOfStraps);
        Part currentShedStraps = getCurrentPart(shedStrapsTotalLength, listOfStraps);
        Part currentGutter = getCurrentPart(gutterTotalLength, listOfGutters);

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
        int gutterTotalLength = (carport.getLength());
        if (carport.isHasGutter()) {
            Part gutter = matchingParts.get(4);
            int guttersAmount = ((gutterTotalLength + gutter.getLength() - 1) / gutter.getLength());
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

    public static Part getCurrentPart(int length, ArrayList<Part> listOfParts) {

        int partBestTotalLength = Integer.MAX_VALUE;
        Part currentPart = null;
        if (currentPart == null) {
            for (Part p : listOfParts) {
                int partAmount = getAmount(length, p);
                int totalLength = (partAmount * p.getLength());
                if (totalLength < partBestTotalLength) {
                    partBestTotalLength = totalLength;
                    currentPart = p;
                }
            }
        }
        return currentPart;
    }

    public static int getAmount(int length, Part part) {
        return (length + part.getLength() - 1) / part.getLength();
    }
}