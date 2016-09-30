package org.matsim.example.planCreation;


import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.matsim.example.MatsimExecuter.munich;

/**
 * Created by carlloga on 9/12/2016.
 */
public class CentroidsToLocations {



    public static ArrayList<Location> readCentroidList() {


        //read the centroid list
        String workDirectory = munich.getString("location.list.folder");
        String fileName = workDirectory + munich.getString("location.list.file");

        BufferedReader bufferReader = null;
        ArrayList<Location> locationList = new ArrayList<>();

        try {
            String line;
            bufferReader = new BufferedReader(new FileReader(fileName));

            // How to read file in java line by line?

            while ((line = bufferReader.readLine()) != null ) {
                Location location = CSVtoLocation(line);
                locationList.add(location);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) bufferReader.close();
            } catch (IOException crunchifyException) {
                crunchifyException.printStackTrace();
            }
        }

        return locationList;

    }
    public static Location CSVtoLocation(String csvLine) {
        int id;
        double x;
        double y;
        long pop;
        long emp;
        float size;
        String[] splitData = csvLine.split("\\s*,\\s*");
        id = Integer.parseInt(splitData[0]);
        x =Double.parseDouble(splitData[1]);
        y =Double.parseDouble(splitData[2]);
        pop =Long.parseLong(splitData[3]);
        emp = Long.parseLong(splitData[4]);
        size = Float.parseFloat(splitData[5]);
        Location location = new Location(id,x,y, pop,emp, size);
        return location;
    }


}
