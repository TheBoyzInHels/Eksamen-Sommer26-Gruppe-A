package app.service;

import app.entities.Part;
import app.entities.PartsList;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PdfGenerator {
    public static void generatePartsListPdf(PartsList partList)
            throws Exception {

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                new FileOutputStream("src/main/resources/public/pdf/partslist.pdf")
        );

        document.open();

        document.add(new Paragraph("Stykliste"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);


        table.addCell("Beskrivelse");
        table.addCell("Længde");
        table.addCell("Antal");
        table.addCell("Enhed");
        table.addCell("Beskrivelse");

        for (Part part : partList.getParts().keySet()) {

            int amount = partList.getParts().get(part);



            table.addCell(part.getDescription());
            table.addCell(String.valueOf(part.getLength()));
            table.addCell(String.valueOf(amount));
            table.addCell("Stk");
            table.addCell(part.getName());
        }
        document.add(table);

        document.close();

        Thread.sleep(200);
    }
}
