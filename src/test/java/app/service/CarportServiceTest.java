package app.service;

import app.entities.Carport;
import app.entities.Part;
import app.entities.PartsList;
import app.service.CarportService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CarportServiceTest {

    @Test
    void generatePartsListWithoutShedAndGutter() {
        // Arrange
        Carport carport = new Carport(1, 2, 600, 300, true, 210, 210, true, 5, "Carport med redskabsskur og tagrender");

        Part raft = new Part(1, "Spær", "45x 195", 500.00, 400);
        Part straps = new Part(2, "Remme", "45x 195", 500.00, 400);
        Part shedStraps = new Part(3, "Remme", "45x 195", 500.00, 400);
        Part post = new Part(4, "Stolpe", "træ", 200.00, 300);
        Part gutter = new Part(5, "Tagrende", "45x 195", 300.00, 600);

        ArrayList<Part> matchingParts = new ArrayList<>();
        matchingParts.add(raft);       // index 0
        matchingParts.add(straps);     // index 1
        matchingParts.add(shedStraps); // index 2
        matchingParts.add(post);       // index 3
        matchingParts.add(gutter);     // index 4

        // Act
        PartsList result = CarportService.generatePartsList(carport, matchingParts);

        // Assert
        assertNotNull(result);
        assertEquals(11, result.getParts().get(raft));
        assertEquals(5, result.getParts().get(straps));
        assertEquals(3, result.getParts().get(shedStraps));
        assertEquals(8, result.getParts().get(post));
        assertEquals(1, result.getParts().get(gutter));
    }
    @Test
    void findMatchingPartsShouldReturnBestMatchingParts() {
        // Arrange
        Carport carport = new Carport(1, 2, 600, 300, true, 210, 210, true, 5, "Carport med redskabsskur og tagrender");

        Part raftTooShort = new Part(1, "Spær", "for kort", 100.00, 240);
        Part raftJustTooShort = new Part(2, "Spær", "passer næsten, er lidt for kort", 150.00, 299);
        Part raftCorrect = new Part(3, "Spær", "passer bedst", 150.00, 300);
        Part raftJustTooLong = new Part (4, "Spær", "passer bedst", 150.00, 300);
        Part raftTooLong = new Part(5, "Spær", "for lang", 200.00, 600);

        Part straps400 = new Part(6, "Remme", "rem 400", 300.00, 400);
        Part straps600 = new Part(7, "Remme", "rem 600", 400.00, 600);

        Part post = new Part(8, "Stolpe", "stolpe", 200.00, 300);

        Part gutter600 = new Part(9, "Tagrende", "tagrende 600", 300.00, 600);
        Part gutter800 = new Part(10, "Tagrende", "tagrende 800", 400.00, 800);

        ArrayList<Part> allParts = new ArrayList<>();
        allParts.add(raftTooShort);
        allParts.add(raftJustTooShort);
        allParts.add(raftCorrect);
        allParts.add(raftJustTooLong);
        allParts.add(raftTooLong);
        allParts.add(straps400);
        allParts.add(straps600);
        allParts.add(post);
        allParts.add(gutter600);
        allParts.add(gutter800);

        // Act
        ArrayList<Part> result = CarportService.findMatchingParts(carport, allParts);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());

        assertEquals(raftCorrect, result.get(0)); // Spær: mindste længde der passer til bredde 300
        assertEquals(straps600, result.get(1));   // Remme til carport
        assertEquals(straps400, result.get(2));   // Remme til skur
        assertEquals(post, result.get(3));        // Første stolpe i listen
        assertEquals(gutter600, result.get(4));   // Tagrende
    }
    @Test
    void findMatchingPartsShouldReturnUnitTestErrorMatchingParts() {
        // Arrange
        Carport carport = new Carport(1, 2, 600, 300, true, 210, 210, true, 5, "Carport med redskabsskur og tagrender");

        Part raftTooShort = new Part(1, "Spær", "for kort", 100.00, 240);
        Part raft300 = new Part(2, "Spær", "passer næsten, er lidt for kort", 150.00, 300);
        Part raftAlso300= new Part(3, "Spær", "passer bedst", 150.00, 300);

        Part straps400 = new Part(6, "Remme", "rem 400", 300.00, 400);
        Part post = new Part(8, "Stolpe", "stolpe", 200.00, 300);
        Part gutter600 = new Part(9, "Tagrende", "tagrende 600", 300.00, 600);

        ArrayList<Part> allParts = new ArrayList<>();
        allParts.add(raftTooShort);

        allParts.add(raft300);
        allParts.add(raftAlso300);

        allParts.add(straps400);
        allParts.add(post);
        allParts.add(gutter600);

        // Act
        ArrayList<Part> result = CarportService.findMatchingParts(carport, allParts);

        // Assert
        assertNotNull(result);

        assertEquals(raft300, result.get(0));
        assertNotEquals(raftAlso300, result.get(0));
    }
}