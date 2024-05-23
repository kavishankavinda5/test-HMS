package sample.controller;

import java.io.*;
import java.util.ArrayList;

public class SystemDataReader {
    private ArrayList<String> tempDataArray =new ArrayList<>();

    public ArrayList<String> getTempDataArray(String filePath){
        File file = new File(filePath);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            String line =null;

            while ((line= bufferedReader.readLine()) !=null){
                tempDataArray.add(line);
            }
            bufferedReader.close();
            fileReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempDataArray;
    }
}
