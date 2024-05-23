package sample.controller.taskControllers;

import java.io.*;
import java.util.ArrayList;

public class SystemDataWriter {

    //when update and Delete use  int=10 as deleteItem parameter
    //in any other case use any inter other than 10
    public void writeDataToFile(String dataLine,String filePath,int deleteItems){
        File file = new File((filePath));

        if (deleteItems ==10){
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataLine);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
           // System.out.println("Data written to appointment File single data");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //when update and Delete use  int=10 as deleteItem parameter
    //in any other case use any inter other than 10
    public  void writeDataToFile(ArrayList<String> dataList,String filePath,int deleteItems){
        File file = new File(filePath);

        if (deleteItems ==10){
            boolean isDeletd = file.delete();
            System.out.println("file deleted : "+isDeletd);
            try {
               boolean isFileCreted=  file.createNewFile();
                System.out.println("file created : "+isFileCreted);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        try {
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
            for (int i=0;i<dataList.size();i++){
                bufferedWriter.write(dataList.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileWriter.close();
           // System.out.println("Data written to  File ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
