package sample.controller.actionTask;

import sample.Main;
import sample.controller.SystemDataReader;
import sample.controller.taskControllers.SystemDataWriter;
import sample.model.Gender;
import sample.model.Visitor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisitorAction {

    public static String visitorFilePath ="src/sample/fileStorage/moduleData/visitorData.txt";
    public static int inVisitorCount =0;
    public static int outVisitorCount =0;

    public static void addVisitorRecord(Visitor visitor){
        addVisitor(visitor);
        JOptionPane.showMessageDialog(null,"Record Add Successfully");
        System.out.println("visitor Record Added : "+visitor.toString());
    }

    public static void updateVisitorRecord(Visitor visitor){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            editDeleteVisitor(visitor,1);
            System.out.println("visitor Record Updated : "+visitor.toString());
        }

    }

    public static void deleteVisitorRecord(Visitor visitor){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Delete This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            editDeleteVisitor(visitor,2);
            System.out.println("visitor Record Deleted : "+visitor.toString());
        }

    }

    public static ArrayList<Visitor> getInVisitorRecords(){
        ArrayList<Visitor> inVisitors = getInOutVisitors(1);
        return inVisitors;
    }

    public static ArrayList<Visitor> viewAllVisitorRecords(){
        return viewAllVisitors();
    }

    public static int getInVisitorCount(){
        int[] count =getInOutVisitorCount();
        System.out.println("IN Visitor Count: "+count[0]);
        return count[0];

    }

    public static int getOutVisitorCount(){
        int[] count =getInOutVisitorCount();
        System.out.println("PUT Visitor Count: "+count[1]);
        return count[1];
    }

    public static ArrayList<Visitor> searchVisitorRecords(String idNumber,String name){
        return searchMultipleRecords(idNumber,name);
    }

    private static void addVisitor(Visitor visitor){
        SystemDataWriter systemDataWriter =new SystemDataWriter();
        systemDataWriter.writeDataToFile(visitor.toString(),visitorFilePath,13);
    }

    /*
     * operation=1 will update Record
     * operation=2 will delete Record
     */
    private static void editDeleteVisitor(Visitor visitor,int operatrion){
        SystemDataReader systemDataReader =new SystemDataReader();
        ArrayList<String> stringVisitorArray = systemDataReader.getTempDataArray(visitorFilePath);
        ArrayList<Visitor> visitorArrayList = getVisitorByStrinArray(stringVisitorArray);
        ArrayList<Visitor> returnVisitorArray =new ArrayList<>();

        for (int i=0;i<visitorArrayList.size();i++){
            Visitor visitorFromFile =visitorArrayList.get(i);
            if (visitorFromFile.getVisitorID() ==visitor.getVisitorID()){
                if (operatrion==1){
                    returnVisitorArray.add(visitor);
                    JOptionPane.showMessageDialog(null,"Visitor Record Update Successfully");
                    System.out.println("visitor record updated success : editDeleteVisitor()-->VisitorAction");
                }
                else {
                    JOptionPane.showMessageDialog(null,"Visitor Record Delete Successfully");
                    System.out.println("visitor record deleted success : editDeleteVisitor()-->VisitorAction");
                }
            }else {
                returnVisitorArray.add(visitorFromFile);
            }
        }

        SystemDataWriter systemDataWriter =new SystemDataWriter();
        ArrayList<String> strVisitorArray =getStrinVisitorArray(returnVisitorArray);
        systemDataWriter.writeDataToFile(strVisitorArray,visitorFilePath,10);

    }

    private static ArrayList<Visitor> viewAllVisitors(){
        SystemDataReader systemDataReader =new SystemDataReader();
        ArrayList<String> strVisitorAllRecords=systemDataReader.getTempDataArray(visitorFilePath);
        ArrayList<Visitor> visitorAllRecords =getVisitorByStrinArray(strVisitorAllRecords);

        return visitorAllRecords;

    }

    /*
    * operation=1 for in visitors
    * operation=2 for out visitors
    * */
    private static ArrayList<Visitor> getInOutVisitors(int operation){
        ArrayList<Visitor> allVisitorRec = viewAllVisitors();
        ArrayList<Visitor> inOrOutVisitorList =new ArrayList<>();

        if (operation==1){
            for (int i=0;i<allVisitorRec.size();i++){
                if (allVisitorRec.get(i).isVisitorIn()){
                    inOrOutVisitorList.add(allVisitorRec.get(i));
                }
            }
        }else if(operation==2){
            for (int i = 0; i < allVisitorRec.size(); i++) {
                if (allVisitorRec.get(i).isVisitorOut()){
                    inOrOutVisitorList.add(allVisitorRec.get(i));
                }
            }
        }else {
            System.out.println("No Records");
        }

        return inOrOutVisitorList;
    }

    private static int[] getInOutVisitorCount(){
        ArrayList<Visitor> visitorCount =getInOutVisitors(1);
        inVisitorCount =visitorCount.size();

        visitorCount =getInOutVisitors(2);
        outVisitorCount =visitorCount.size();

        int[] intOutCout = new int[2];
        intOutCout[1]=inVisitorCount;
        intOutCout[2]= outVisitorCount;

        return intOutCout;
    }

    private static Visitor searchVisitor(int visitorID,String vName){
        ArrayList<Visitor> allVisitors =viewAllVisitors();
        Visitor foundVisitor =null;
        for (int i=0;i<allVisitors.size();i++){
            if (allVisitors.get(i).getVisitorID() ==visitorID || allVisitors.get(i).getName()==vName){
                foundVisitor=allVisitors.get(i);
                System.out.println("VisitorRecord Found in visitorFile.txt");
            }
        }

        return foundVisitor;
    }

    private static ArrayList<Visitor> searchMultipleRecords(String idNumber,String name){

        boolean isFound = false;

        ArrayList<Visitor> allAvailableVisitors =viewAllVisitors();
        ArrayList<Visitor> sameVisitor =new ArrayList<>();
        for (int i=0;i<allAvailableVisitors.size();i++){
            if ((allAvailableVisitors.get(i).getIdNumber().equals(idNumber))){
                Visitor foundVisitor =allAvailableVisitors.get(i);
                sameVisitor.add(foundVisitor);
                isFound= true;
                System.out.println("VisitorRecord Found in visitorFile.txt");
            }
        }
        if(isFound){
            //found
            JOptionPane.showMessageDialog(null,"Postal Record Found");
        }
        else{
            //not found
            JOptionPane.showMessageDialog(null,"Postal Record Not Found");
        }

        return sameVisitor;
    }

    private static ArrayList<String> getStrinVisitorArray(ArrayList<Visitor> visitorArrayList) {
        ArrayList<String> stringVisitor =new ArrayList<>();

        for (int i=0;i<visitorArrayList.size();i++){
            String visitor =visitorArrayList.get(i).toString();
            stringVisitor.add(visitor);
        }

       return stringVisitor;
    }

    private static ArrayList<Visitor> getVisitorByStrinArray(ArrayList<String> stringVisitorArray) {
        ArrayList<Visitor> tempVisitorArray =new ArrayList<>();

        for (int i=0;i<stringVisitorArray.size();i++){
            String visitorStr =stringVisitorArray.get(i);
            Visitor visitor =getVisitorByStringLine(visitorStr);
            tempVisitorArray.add(visitor);
        }

        return tempVisitorArray;
    }

    private static Visitor getVisitorByStringLine(String visitorStr) {
        List<String> temStringVisitor = Arrays.asList(visitorStr.split("~"));

        Visitor newVisitor = new Visitor();
        newVisitor.setVisitorID(Integer.parseInt(temStringVisitor.get(0)));
        newVisitor.setDate(Main.getLocalDatefromString(temStringVisitor.get(1)));
        newVisitor.setIdNumber(temStringVisitor.get(2));
        newVisitor.setName(temStringVisitor.get(3));
        newVisitor.setPurpose(temStringVisitor.get(4));
        newVisitor.setPhoneNumber(temStringVisitor.get(5));
        newVisitor.setGender(getGender(temStringVisitor.get(6)));
        newVisitor.setNote(temStringVisitor.get(7));
        newVisitor.setInTime(Main.getLocalTimeFromString(temStringVisitor.get(8)));
        if(!(temStringVisitor.get(9).equals("null"))){
            newVisitor.setOutTime(Main.getLocalTimeFromString(temStringVisitor.get(9)));
        }
        newVisitor.setVisitorIn(getVisitorInOut(temStringVisitor.get(10)));
        newVisitor.setVisitorOut(getVisitorInOut(temStringVisitor.get(11)));

        return newVisitor;
    }

    private static Gender getGender(String str){
        Gender gender =null;
        switch (str){
            case "MALE":
                gender =Gender.MALE;
                break;
            case "FEMALE":
                gender =Gender.FEMALE;
                break;
            case "OTHER":
                gender =Gender.OTHER;
                break;
            default:
                break;

        }

        return gender;
    }

    private static Boolean getVisitorInOut(String inOrOut){
        Boolean status =false;

        switch (inOrOut){
            case "true":
                status =true;
                break;
            case "false":
                status=false;
                break;
            default:
                break;
        }
        return status;
    }

}
