package sample.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientViewController {

    private Patient currentpatient;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private BorderPane patientView;
    @FXML private ImageView patientView_home;
    @FXML private JFXButton patientView_appointment;
    @FXML private JFXButton patientView_complaint;
    @FXML private Pane patientMain_logout;
    @FXML private JFXButton patientView_logoutButton;
    @FXML private JFXButton patientView_profile;
    @FXML private ImageView patientMain_backIcon;
    @FXML private AnchorPane patientView_homePane;


    @FXML
    void initialize() {



        patientView_appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                   // System.out.println("taskView/appointmentView");
                    Pane view = Main.getView("taskView/appointmentView");
                    setPatientViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        patientView_complaint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                   // System.out.println("taskView/complaintView");
                    Pane view = Main.getView("taskView/complaintView");
                    setPatientViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        patientView_profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                  //  System.out.println("taskView/profileSettingView");
                    Pane view = Main.getView("taskView/profileSettingView");
                    setPatientViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        patientView_logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Object[] options = { "OK", "CANCEL" };
                Toolkit.getDefaultToolkit().beep();
                int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure LogOut"+"\nClick OK to continue", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);

                if (selectedValue == JOptionPane.WHEN_FOCUSED) {

                    patientMain_logout.getScene().getWindow().hide();
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
        });

        patientView_home.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                patientView.setCenter(patientView_homePane);
            }
        });

    }


    public void setPatientViewCenter(Pane view){
        patientView.setCenter(view);

    }

    public Patient getCurrentpatient() {
        return currentpatient;
    }

    public void setCurrentpatient(Patient currentpatient) {
        this.currentpatient = currentpatient;
       // System.out.println( "Patient set in PatientDashboard dashboard"+currentpatient.toString());
    }


}
