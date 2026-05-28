package app.entities;

import java.util.HashMap;

public class PartsList {
    private final HashMap<Part, Integer> parts;

    public PartsList(HashMap<Part, Integer> parts) {
        this.parts = parts;
    }

    public void addPart(Part part) {
        if (parts.containsKey(part)) {
            parts.put(part, parts.get(part) + 1);
        } else {
            parts.put(part, 1);
        }
    }

    public HashMap<Part, Integer> getParts() {
        return parts;
    }
}
