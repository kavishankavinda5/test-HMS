package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.controller.MultipleFXMLLoader;
import sample.controller.SystemDataReader;
import sample.controller.actionTask.ReferenceAction;
import sample.controller.actionTask.UserAction;
import sample.model.SystemUser;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    private static int referenceID ;
    private static int staffID ;
    private static int appointmentID;
    private static int complaintID;
    private static int visitorID;
    private static int postalReferenceID;
    private static SystemUser currentSystemUser;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/mainLoginWindow.fxml"));
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {

        ReferenceAction.loadReference();

        loadSystemData();
        launch(args);
        saveSystemData();
    }

    public static void liveClock(Label dispalyLable) {
        //live clock
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            dispalyLable.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    public static Pane getView(String fileName) {
        //receptionView_task.setVisible(false);

        MultipleFXMLLoader newFXML = new MultipleFXMLLoader();
        Pane viewCurrent = newFXML.getPage(fileName);

        return viewCurrent;
    }
    public void backToMain(Scene scene){

        Object[] options = { "OK", "CANCEL" };
        Toolkit.getDefaultToolkit().beep();
        int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure LogOut"+"\nClick OK to continue", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (selectedValue == JOptionPane.WHEN_FOCUSED) {

            scene.getWindow().hide();
            Stage detailsStage = new Stage();
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/sample/view/mainLoginWindow.fxml"));
            try {
                loader.load();

            }catch (IOException e){
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            detailsStage.setScene(new Scene(root));
            detailsStage.show();

        }

    }


    public static void loadSystemData(){

        SystemDataReader systemDataReader =new SystemDataReader();
        ArrayList<String> systemData = systemDataReader.getTempDataArray("src/sample/fileStorage/systemData.txt");
        for (int i=0;i<systemData.size();i++){
            String line =systemData.get(i);
            List<String> temSystemDataList = Arrays.asList(line.split("~"));
            switch (temSystemDataList.get(0)){
                case "staffID":
                    staffID = Integer.parseInt(temSystemDataList.get(1));
                   // System.out.println("staff ID : "+staffID);
                    break;
                case "referenceID":
                    referenceID =Integer.parseInt(temSystemDataList.get(1));
                   // System.out.println("reference ID : "+referenceID);
                    break;
                case "appointmentID":
                    appointmentID = Integer.parseInt(temSystemDataList.get(1));
                   // System.out.println("appointment ID : "+appointmentID);
                    break;
                case "complaintID":
                    complaintID =Integer.parseInt(temSystemDataList.get(1));
                   // System.out.println("complaint ID : "+complaintID);
                    break;
                case "visitorID":
                    visitorID =Integer.parseInt(temSystemDataList.get(1));
                   // System.out.println("visitorID : "+visitorID);
                    break;
                case "postalReferenceID":
                    postalReferenceID =Integer.parseInt(temSystemDataList.get(1));
                  //  System.out.println("postalReferenceID : "+postalReferenceID);
                    break;
                default:
                    break;

            }
        }

    }

    public static void saveSystemData(){
        File file =new File("src/sample/fileStorage/systemData.txt");
        file.delete();

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("staffID~"+staffID);
            bufferedWriter.newLine();
            bufferedWriter.write("referenceID~"+referenceID);
            bufferedWriter.newLine();
            bufferedWriter.write("appointmentID~"+appointmentID);
            bufferedWriter.newLine();
            bufferedWriter.write("complaintID~"+complaintID);
            bufferedWriter.newLine();
            bufferedWriter.write("visitorID~"+visitorID);
            bufferedWriter.newLine();
            bufferedWriter.write("postalReferenceID~"+postalReferenceID);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
            System.out.println("system data saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getVisitorID(){
        return   ++visitorID;
    }

    public static int getStaffID(){
      return   ++staffID;
    }

    public static int getComplaintID(){
        return ++complaintID;
    }

    public static int getAppointmentID(){
        return ++appointmentID;
    }

    public static int getPostalReferenceID(){return ++postalReferenceID;}

    public static int getReferenceID(){
        ++referenceID;
        return referenceID;
    }

    public static LocalDate getLocalDatefromString (String string){
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
    }

    public static LocalTime getLocalTimeFromString(String string){
        String pattern = "HH:mm";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        if (string != null && !string.isEmpty()) {
            return LocalTime.parse(string, dateFormatter);
        } else {
            return null;
        }
    }

    public static String getStringFromLocalTime(LocalTime localTime){
        String pattern = "HH:mm";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        if (localTime != null) {
            return dateFormatter.format(localTime);
        } else {
            return "";
        }
    }

    public static String getStringfromLocalDate (LocalDate date){
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }

    public static SystemUser getCurrentSystemUser() {
        return currentSystemUser;
    }

    public static String getCurretUserName(){
        String userName = null;
        SystemUser systemUser =getCurrentSystemUser();
        switch (systemUser.getUserRoll()){
            case PATIENT:
                userName =systemUser.getPatient().getUserName();
                break;
            case RECEPTIONIST:
                userName =systemUser.getReceptionist().getUserName();
                break;
            case MEDICALOFFICER:
                userName =systemUser.getMedicalOfficer().getUserName();
                break;
            case ADMIN:
                userName =systemUser.getAdmin().getUserName();
                break;
            default:
                break;

        }
        return userName;
    }

    public static void setCurrentSystemUser(SystemUser currentSystemUser) {
        Main.currentSystemUser = currentSystemUser;
       // System.out.println("System User set in main ---->"+currentSystemUser.toString());
    }
}
