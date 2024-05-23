package sample.controller.actionTask;

import sample.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReferenceAction {

    public static ArrayList<String> referenceTypes =new ArrayList<>();
    public static final String referenceFile = "src/sample/fileStorage/moduleData/referenceData.txt";

    //Constant Value arrays
    public static ArrayList<UserRoll> userRolls =new ArrayList<>();
    public static ArrayList<Gender> gender = new ArrayList<>();
    public static ArrayList<BloodGroup> bloogGroup = new ArrayList<>();
    public static ArrayList<String> maritalStatus = new ArrayList<>();
    public static ArrayList<PostalType>postalTypes=new ArrayList<>();
    public static ArrayList<AppointmentStatus> apointmentStatus =new ArrayList<>();
    public static ArrayList<String> complaintRefStringArrayList = new ArrayList<>();
    public static ArrayList<String> doctorSpecialityStringList = new ArrayList<>();

    //Changeable type Arrays
    public static ArrayList<Reference> complaintRefArrayList = new ArrayList<>();
    public static ArrayList<Reference> doctorSpecialityArray = new ArrayList<>();


/*
=======================================================================================================================
                              Complaint Type and Speciality type Reference
=======================================================================================================================
 */

    public static void addReference(Reference reference){
    File newRef = new File(referenceFile);

    try(FileWriter fileWriter = new FileWriter(newRef,true)) {
        BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
        bufferedWriter.write(reference.toString());
        bufferedWriter.newLine();
        bufferedWriter.close();
        fileWriter.close();
        JOptionPane.showMessageDialog(null,"Record Add Successfully");
        System.out.println("Ref Added "+reference.toString());


    } catch (IOException e) {
        e.printStackTrace();

    }

    loadSavedReference();
    setRefDataToStringList();
}

    public static void updateReference(Reference reference){
        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            updateOrDeleteReference(reference,1);
        }
    }

    public static void deleteReference(Reference reference){
        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Delete This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            updateOrDeleteReference(reference,10);
        }
    }

    //update or delete ref record input 1 for edit and for any other integer delete
    //to delete reference only the selected reference should be pass
    private static void updateOrDeleteReference(Reference newReference,int operation){

        File file = new File(referenceFile);
        ArrayList<String> tempRefeArrya = new ArrayList<>();

        boolean found =false;
        boolean isDeleted =false;

        try (FileReader fileReader = new FileReader(file)){
            BufferedReader bufferedReader =new BufferedReader(fileReader);
            String line=null;

            while (((line=bufferedReader.readLine()) != null ) ){
                List<String> tempRef =Arrays.asList(line.split("~"));
                System.out.println("temp reference in updateDelete : "+tempRef);
                System.out.println("reference from updateView : "+newReference);
                if (!(Integer.parseInt(tempRef.get(0) )== newReference.getRefenceID())){
                    System.out.println("line Added");
                    tempRefeArrya.add(line);
                }else if (operation==1)
                    {
                        System.out.println("matched record found");
                        line=newReference.toString();
                        tempRefeArrya.add(line);
                        JOptionPane.showMessageDialog(null,"Reference Update Successfully");
                        System.out.println("Reference updated success , Line = "+line);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Reference Delete Successfully");
                        System.out.println("Reference deleted success , LinerDeleted = "+line);

                    }
                }


            bufferedReader.close();
            fileReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(tempRefeArrya);

                  if(file.exists()){
                        file.delete();
                                           }
                try {
                    file.createNewFile();
                    writeListToRefFile(referenceFile,tempRefeArrya);
                    loadSavedReference();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            System.out.println("Record effected successfully");
            setRefDataToStringList();

    }

    private static void writeListToRefFile(String fileName,ArrayList<String> RefArray) {
        File fileNew = new File(fileName);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileNew);

            BufferedWriter refbufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < RefArray.size(); i++) {
                refbufferedWriter.write(RefArray.get(i));
                refbufferedWriter.newLine();

                System.out.println("Reference record successfully Written");
            }
            refbufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

 /*
=======================================================================================================================
***********************************************************************************************************************
=======================================================================================================================
 */
    public static ArrayList<BloodGroup> getBloogGroup() {
        return bloogGroup;
    }

    public static void loadReference(){

        setGender();
        setUserRolls();
        setMaritalStatus();
        setBloogGroup();
        setApointmentStatus();
        loadSavedReference();
        setReferenceTypes();
        setPostalTypes();

    }

    public static ArrayList<Reference> getComplaintRefArrayList() {
        return complaintRefArrayList;
    }

    public static ArrayList<String > getComplaintStringArray(){
        return  complaintRefStringArrayList;
    }

    public static ArrayList<String> getDocSpecialityStringArray() {
        return doctorSpecialityStringList;
    }

    private static void loadSavedReference(){

        ArrayList<Reference> allSavedReference =new ArrayList<>();

        File refFile = new File(referenceFile);
        try (FileReader fileReader = new FileReader(refFile)){
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line =bufferedReader.readLine() )!=null){
                List<String> temrefObj = Arrays.asList(line.split("[~\n]"));
                Reference reference = new Reference(Integer.parseInt(temrefObj.get(0)),temrefObj.get(1),temrefObj.get(2));
                allSavedReference.add(reference);
            }
            complaintRefArrayList.clear();
            doctorSpecialityArray.clear();

            for (int i=0;i<allSavedReference.size();i++){

                if (allSavedReference.get(i).getReferenceType().equals("Complaint Types")){
                    System.out.println(allSavedReference.toString());
                    Reference reference = new Reference(allSavedReference.get(i).getRefenceID(),"Complaint Types",allSavedReference.get(i).getReferenceValue());
                    complaintRefArrayList.add(reference);
                }else {
                    Reference reference = new Reference(allSavedReference.get(i).getRefenceID(),"Doctor Speciality",allSavedReference.get(i).getReferenceValue());
                    doctorSpecialityArray.add(reference);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setRefDataToStringList();
    }

    private static void setRefDataToStringList(){
        complaintRefStringArrayList.clear();
        doctorSpecialityStringList.clear();

        for (int i=0;i<complaintRefArrayList.size();i++){
            complaintRefStringArrayList.add(complaintRefArrayList.get(i).getReferenceValue());
        }

        for (int i = 0; i< doctorSpecialityArray.size(); i++){
            doctorSpecialityStringList.add(doctorSpecialityArray.get(i).getReferenceValue());
        }
    }

    public static void setUserRolls() {
        userRolls.add(UserRoll.ADMIN);
        userRolls.add(UserRoll.PATIENT);
        userRolls.add(UserRoll.RECEPTIONIST);
        userRolls.add(UserRoll.MEDICALOFFICER);
    }

    public static ArrayList<UserRoll> getUserRolls() {
        return userRolls;
    }

    public static ArrayList<Reference> getDoctorSpecialityArray() {
        return doctorSpecialityArray;
    }

    public static ArrayList<Gender> getGender() {
        return gender;
    }

    public static void setGender() {

        gender.add(Gender.MALE);
        gender.add(Gender.FEMALE);
        gender.add(Gender.OTHER);
    }

    public static void setBloogGroup() {
        bloogGroup.add(BloodGroup.A_POSITIVE);
        bloogGroup.add(BloodGroup.A_NEGATIVE);
        bloogGroup.add(BloodGroup.AB_NEGATIVE);
        bloogGroup.add(BloodGroup.AB_POSITIVE);
        bloogGroup.add(BloodGroup.B_NEGATIVE);
        bloogGroup.add(BloodGroup.B_POSITIVE);
        bloogGroup.add(BloodGroup.O_NEGATIVE);
        bloogGroup.add(BloodGroup.O_POSITIVE);
    }

    public static void setMaritalStatus() {
        maritalStatus.add("Married");
        maritalStatus.add("Unmarried");
    }

    public static void setApointmentStatus(){
        apointmentStatus.add(AppointmentStatus.PENDING);
        apointmentStatus.add(AppointmentStatus.APPROVED);
        apointmentStatus.add(AppointmentStatus.COMPLETED);
    }



    public static ArrayList<String> getMaritalStatus() {
        return maritalStatus;
    }

    public static ArrayList<PostalType> getPostalTypes() {
        return postalTypes;
    }

    public static void setPostalTypes() {
        postalTypes.add(PostalType.RECEIVED);
        postalTypes.add(PostalType.DISPATCH);
    }

    public static void setReferenceTypes(){
        referenceTypes.add("Complaint Types");
        referenceTypes.add("Doctor Speciality");
    }
}
