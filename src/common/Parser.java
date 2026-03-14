package common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Parser {

    public static ArrayList<Vehicle> parse(String filePath) throws Exception {
        ArrayList<Vehicle> list = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(new InputSource(isr));
        isr.close();

        NodeList n = doc.getElementsByTagName("vehicle");
        for (int i = 0; i < n.getLength(); i++) {

            try {
                Element e = (Element) n.item(i);
                Vehicle v = new Vehicle();
//                v.setId(Long.parseLong(e.getAttribute("id")));
                v.setName(e.getElementsByTagName("name").item(0).getTextContent());
                Element c = (Element) e.getElementsByTagName("coordinates").item(0);
                v.setCoordinates(Integer.parseInt(c.getElementsByTagName("x").item(0).getTextContent()), Float.parseFloat(c.getElementsByTagName("y").item(0).getTextContent()));

                String dateStr = e.getElementsByTagName("creationDate").item(0).getTextContent().trim();
                Date parsedDate;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    parsedDate = sdf.parse(dateStr);
                    v.setCreationDateHand(parsedDate);
                } catch (ParseException ec) {
                    v.setCreationDate();
                }


                v.setEnginePower(Float.parseFloat(e.getElementsByTagName("enginePower").item(0).getTextContent()));
                v.setDistanceTravelled(Float.parseFloat(e.getElementsByTagName("distanceTravelled").item(0).getTextContent()));
                v.setType(VehicleType.valueOf(e.getElementsByTagName("type").item(0).getTextContent().toUpperCase()));
                v.setFuelType(FuelType.valueOf(e.getElementsByTagName("fuelType").item(0).getTextContent().toUpperCase()));
                if (v.getName() == null || v.getName().trim().isEmpty() || v.getCoordinates() == null || v.getCoordinates().toString().trim().isEmpty() || v.getCoordinates().y <= -668F || v.getEnginePower() <= 0 || v.getDistanceTravelled() <= 0) {
                    throw new NullPointerException();
                } else {
                    list.add(v);
                }
            } catch (NullPointerException e) {
                System.out.println("Данные в объекте номер " + (i + 1) + " не валидны");
            } catch (Exception e) {
                System.out.println("Данные в объекте номер " + (i + 1) + " не валидны; ошибка в константах");
            }

        }
        return list;
    }


}
