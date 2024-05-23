package sample.controller.actionTask;

import sample.Main;
import sample.controller.SystemDataReader;
import sample.controller.taskControllers.SystemDataWriter;
import sample.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppointmentAction {

    public static String appointmentDataFile ="src/sample/fileStorage/moduleData/appointment/appointmentData.txt";

    public static void addAppointment(Appointment appointment){
        saveAppointment(appointment);
        JOptionPane.showMessageDialog(null,"Appointment Add Successfully");
        System.out.println("Appointment saved success");
    }

    public static void updateAppointment(Appointment appointment){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure Update This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            updateDeleteAppointment(appointment,1);
            System.out.println("Appointment updated success");
        }

    }

    public static void deleteAppointment(Appointment appointment){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null,
                "Are You Sure Delete This Record"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
            updateDeleteAppointment(appointment,10);
            System.out.println("Appointment delete success");
        }

    }


    /*
     * to get pending appointments statusType=1
     * approved appointments statusType=2
     * completed appointments statusType=3
     * */
    public static ArrayList<Appointment> getAppointmentByStatus(int statusType){
        ArrayList<Appointment> passAppointment = getAppByStatus(statusType);
        ArrayList<String> appPending = getAppointmentStringArray(passAppointment);
        System.out.println("appointments by status passed");
        return passAppointment;
    }

    public static Appointment searchAppointmentByID(int appID){
        Appointment foundAppointment = searchAppointment(appID);
        System.out.println("found appointment record passed");
        return foundAppointment;
    }

    private static void saveAppointment(Appointment appointment){

        SystemDataWriter systemDataWriter = new SystemDataWriter();
        systemDataWriter.writeDataToFile(appointment.toString(),appointmentDataFile,0);

    }

    public static ArrayList<Appointment> getAppointmentArrayList(){
        ArrayList<Appointment> allAppointments =new ArrayList<>();
        SystemDataReader systemDataReader =new SystemDataReader();
        ArrayList<String> pendingAppStringList =systemDataReader.getTempDataArray(appointmentDataFile);
        for (int i=0;i<pendingAppStringList.size();i++){
            String line =pendingAppStringList.get(i);
            Appointment pendingApp =getAppointmentByString(line);
            allAppointments.add(pendingApp);
        }

        return allAppointments;
    }

    private static Appointment getAppointmentByString(String line) {
        List<String> tempLine;
        List<String> tempItems = Arrays.asList(line.split("~"));
        Appointment newTemp =new Appointment();
        newTemp.setAppointmentID(Integer.parseInt(tempItems.get(0)));

        //Patient
        tempLine =Arrays.asList(tempItems.get(1).split("%%"));
        Patient patient = new Patient();
        patient.setName(tempLine.get(0));
        patient.setIdCardNumber(tempLine.get(1));
        patient.setPhoneNumber(tempLine.get(2));
        newTemp.setPatient(patient);

        newTemp.setAppointmentDate(Main.getLocalDatefromString(tempItems.get(2)));
        //set Appointment time as AppointmentTimeClass
        AppointmentTime appointmentTime =new AppointmentTime();
        tempLine =Arrays.asList(tempItems.get(3).split(":"));
        appointmentTime.setHours(tempLine.get(0));
        appointmentTime.setMinutes(tempLine.get(1));
        newTemp.setTime(appointmentTime);

        tempLine =Arrays.asList(tempItems.get(4).split("%%"));
        MedicalOfficer medicalOfficer =new MedicalOfficer();
        medicalOfficer.setName(tempLine.get(0));
        medicalOfficer.setIdCardNumber(tempLine.get(1));
        medicalOfficer.setSpeciality(tempLine.get(2));
        newTemp.setMedicalOfficer(medicalOfficer);

        newTemp.setAppointmentStatus(getAppStatus(tempItems.get(5)));
        newTemp.setSymtomes(tempItems.get(6));

        return  newTemp;

    }

    private static AppointmentStatus getAppStatus(String s) {
        AppointmentStatus appointmentStatus =null;
        switch (s){
            case "PENDING":
                appointmentStatus =AppointmentStatus.PENDING;
                break;
            case "APPROVED":
                appointmentStatus =AppointmentStatus.APPROVED;
                break;
            case "COMPLETED":
                appointmentStatus=AppointmentStatus.COMPLETED;
                break;
            default:
                break;
        }
        return appointmentStatus;
    }

    private static Appointment searchAppointment(int id){

        boolean isFound = false;

        Appointment foundAppointment = new Appointment();
        ArrayList<Appointment> allRecords = getAppointmentArrayList();
        for (int i=0;i<allRecords.size();i++){
            if (allRecords.get(i).getAppointmentID() == id){
                foundAppointment = allRecords.get(i);
                isFound= true;
                break;
            }
        }
        if(isFound){
            //found
            JOptionPane.showMessageDialog(null,"Appointment Found");
        }
        else{
            //not found
            JOptionPane.showMessageDialog(null,"Appointment Not Found");
        }
        return foundAppointment;
    }

    //operation=1 to update appointmentrecord any other integer will update
    private static void updateDeleteAppointment(Appointment updatedAppointment,int operation){

        ArrayList<Appointment> newAppointmentArray =new ArrayList<>();
        ArrayList<Appointment> allAppRecords = getAppointmentArrayList();
        for (int i=0;i<allAppRecords.size();i++){
            if (!(allAppRecords.get(i).getAppointmentID() == updatedAppointment.getAppointmentID())){
                newAppointmentArray.add(allAppRecords.get(i));
            }
            else {
                if (operation==1){
                    newAppointmentArray.add(updatedAppointment);
                    JOptionPane.showMessageDialog(null,"Appointment Update Successfully");
                    System.out.println("Appointment updated success");
                }
                else {
                    JOptionPane.showMessageDialog(null,"Appointment Delete Successfully");
                    System.out.println("Appointment Deleted success");
                }

            }
        }

        SystemDataWriter systemDataWriter = new SystemDataWriter();
        systemDataWriter.writeDataToFile(getAppointmentStringArray(newAppointmentArray),appointmentDataFile,10);
    }

    private static ArrayList<String> getAppointmentStringArray(ArrayList<Appointment> appointmentArrayList){
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i=0;i<appointmentArrayList.size();i++){
            stringArrayList.add(appointmentArrayList.get(i).toString());
        }
        return stringArrayList;
    }

    /*
    * to get pending appointments statusType=1
    * approved appointments statusType=2
    * completed appointments statusType=3
    * */
    private static ArrayList<Appointment> getAppByStatus(int statusType){
        ArrayList<Appointment> selectedStatusApp =new ArrayList<>();
        ArrayList<Appointment> allAppointments = getAppointmentArrayList();

        AppointmentStatus appointmentStatus =null;
        switch (statusType){
            case 1:
                System.out.println("pending appointment requested");
                appointmentStatus=AppointmentStatus.PENDING;
                break;
            case 2:
                System.out.println("approved appointment requested");
                appointmentStatus=AppointmentStatus.APPROVED;
                break;
            case 3:
                System.out.println("completed appointment requested");
                appointmentStatus=AppointmentStatus.COMPLETED;
                break;
            default:
                break;
        }


        for (int i=0;i<allAppointments.size();i++){
            if (allAppointments.get(i).getAppointmentStatus() == appointmentStatus){
                selectedStatusApp.add(allAppointments.get(i));
            }
        }
        System.out.println("appointments by "+appointmentStatus+" status passed");

       return selectedStatusApp;
    }

}
