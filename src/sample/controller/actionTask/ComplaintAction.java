package sample.controller.actionTask;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import sample.Main;
import sample.controller.SystemDataReader;
import sample.controller.taskControllers.SystemDataWriter;
import sample.model.Complaint;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComplaintAction {

    private static String complaintFilePath= "src/sample/fileStorage/moduleData/complaintData.txt";

    public static void addComplaintRecord(Complaint complaint){
        addComplaint(complaint);
        saveFile(complaint);
        JOptionPane.showMessageDialog(null,"Complaint Add Successfully");
    }

    public static void updateComplaintRecord(Complaint complaint){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            editDeleteComplaint(complaint,1);
            saveFile(complaint);
        }

    }

    public static void deleteComplaintRecord(Complaint complaint){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Delete This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            editDeleteComplaint(complaint,10);
            deleteFile(complaint);
        }

    }

    //write a method for save file
    public static void saveFile(Complaint complaint){

        try{
            int referenceNo = complaint.getComplaintID();

            if (complaint.getFilePath()!=null){
            String moFilePath ="src/sample/fileStorage/moduleData/userData/complaintData";
            String fileSavePath = moFilePath + "\\" + referenceNo + ".pdf";

            OutputStream savePath = new FileOutputStream(new File(fileSavePath));
            FileInputStream inputPath = new FileInputStream(complaint.getFilePath());
            PdfReader pdfReader = new PdfReader(inputPath);
            new PdfStamper(pdfReader, savePath).close();
            System.out.println("Save PDF");
            }else {
                System.out.println("No Pdf");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    //write a method for Delete file
    public static void deleteFile(Complaint complaint){

        try{
            int referenceNo = complaint.getComplaintID();

            String moFilePath ="src/sample/fileStorage/moduleData/userData/complaintData";
            String fileSavePath = moFilePath + "\\" + referenceNo + ".pdf";
            Files.delete(Path.of(fileSavePath));

            System.out.println("Delete PDF");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Complaint searchComplaintRecord(String complaintID,String complaintBy){
        return searchComplaint(complaintID,complaintBy);
    }

    public static ArrayList<Complaint> getAllComplaintRecords(){
        return getAllComplaints();
    }

    private static Complaint searchComplaint(String complaintID, String vName){

        boolean isFound = false;

        ArrayList<Complaint> allComplaint =getAllComplaints();
        Complaint foundComplaint =null;
        for (int i=0;i<allComplaint.size();i++){
            if (String.valueOf(allComplaint.get(i).getComplaintID()).equals(complaintID) || allComplaint.get(i).getComplaintBy().equals(vName)){
                foundComplaint=allComplaint.get(i);
                isFound= true;
                System.out.println("Complaint Record Found in Complaint file");
            }
        }

        if(isFound){
            //found
            JOptionPane.showMessageDialog(null,"Complaint Found");
        }
        else{
            //not found
            JOptionPane.showMessageDialog(null,"Complaint Not Found");
        }

        return foundComplaint;
    }


    private static void addComplaint(Complaint complaint){
        SystemDataWriter systemDataWriter =new SystemDataWriter();
        systemDataWriter.writeDataToFile(complaint.toString(),complaintFilePath,5);
    }

    //operation =1 will update record any other integer will delete record
    private static void editDeleteComplaint(Complaint complaint,int operationType){
        ArrayList<Complaint> finalEditedDataArray = new ArrayList<>();
        ArrayList<Complaint> allComplaints =getAllComplaints();

        for (int i=0;i<allComplaints.size();i++){
            Complaint tempComplaint =allComplaints.get(i);
            if (tempComplaint.getComplaintID() == complaint.getComplaintID()){
                if(operationType==1){
                    finalEditedDataArray.add(complaint);
                    JOptionPane.showMessageDialog(null,"Complaint Update Successfully");
                    System.out.println("Complaint Update Record added Success");
                }
                else {
                    JOptionPane.showMessageDialog(null,"Complaint Delete Successfully");
                    System.out.println("Complaint Deleted Success");
                }
            }else{
                finalEditedDataArray.add(allComplaints.get(i));
            }

        }

        SystemDataWriter systemDataWriter = new SystemDataWriter();
        ArrayList<String> stringComplaintArray =getStringComplaintArray(finalEditedDataArray);
        systemDataWriter.writeDataToFile(stringComplaintArray,complaintFilePath,10);

    }

    private static ArrayList<String> getStringComplaintArray(ArrayList<Complaint> finalEditedDataArray) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i=0;i<finalEditedDataArray.size();i++){
            stringArrayList.add(finalEditedDataArray.get(i).toString());
        }

        return stringArrayList;
    }

    private static ArrayList<Complaint> getAllComplaints() {
        SystemDataReader systemDataReader =new SystemDataReader();
        ArrayList<String> coplaintStrngArray=systemDataReader.getTempDataArray(complaintFilePath);
        ArrayList<Complaint> complaintArrayList = getComplaintArrayByStringArray(coplaintStrngArray);

        return  complaintArrayList;
    }

    private static ArrayList<Complaint> getComplaintArrayByStringArray(ArrayList<String> complaintStrngArray) {
        ArrayList<Complaint> complaintArrayList = new ArrayList<>();

        for (int i=0;i<complaintStrngArray.size();i++){
            String line =complaintStrngArray.get(i);
            Complaint complaint =getComplaintByStringLine(line);
            complaintArrayList.add(complaint);
        }
        System.out.println("complaint Arrya passed from getComplaintArrayByStringArray()---->");
        return complaintArrayList;
    }

    private static Complaint getComplaintByStringLine(String line) {
        Complaint complaint =new Complaint();
        List<String> tempComplant = Arrays.asList(line.split("[~\n]"));

        complaint.setComplaintID(Integer.parseInt(tempComplant.get(0)));
        complaint.setComplaintDate(Main.getLocalDatefromString(tempComplant.get(1)));
        complaint.setComplaintType(tempComplant.get(2));
        complaint.setComplaintBy(tempComplant.get(3));
        complaint.setPhoneNumber(tempComplant.get(4));
        complaint.setDescription(tempComplant.get(5));
        complaint.setNote(tempComplant.get(6));
        complaint.setActiontaken(tempComplant.get(7));

        return complaint;
    }
}
