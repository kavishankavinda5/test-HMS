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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.model.Receptionist;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceptionMainViewController {

    Receptionist currentreceptionist ;


    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private BorderPane mainReceptionView;
    @FXML private ImageView receptionView_home;
    @FXML private Label receptionView_userName;
    @FXML private JFXButton receptionView_visitor;
    @FXML private JFXButton receptionView_patient;
    @FXML private JFXButton receptionView_postal;
    @FXML private JFXButton receptionView_appointment;
    @FXML private JFXButton receptionView_complaint;
    @FXML private Pane receptionMain_logout;
    @FXML private JFXButton receptionMain_logoutButton;
    @FXML private JFXButton receptionView_profile;
    @FXML private ImageView adminMain_backIcon;
    @FXML private AnchorPane receptionView_homePane;


    @FXML
    void initialize() {

        receptionView_patient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                   // System.out.println("taskView/userView");
                    Pane view = getView("taskView/userView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        receptionView_visitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               try {
                 //  System.out.println("taskView/visitorView");
                   Pane view = getView("taskView/visitorView");
                   setReceptionViewCenter(view);
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });

        receptionView_postal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               // System.out.println("you clicked receptionView_viewPostal view");
                try {
                    Pane view = getView("taskView/postalView");
                    setReceptionViewCenter(view);
                } catch (Exception e){
                e.printStackTrace();
            }

            }
        });

        receptionView_appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                  //  System.out.println("taskView/appointmentView");
                    Pane view = getView("taskView/appointmentView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        receptionView_home.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                setReceptionViewCenter(receptionView_homePane);
            }
        });

        receptionView_profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                  //  System.out.println("taskView/profileSettingView");
                    Pane view = getView("taskView/profileSettingView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        receptionView_complaint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                   // System.out.println("taskView/complaintView");
                    Pane view = getView("taskView/complaintView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        receptionMain_logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Object[] options = { "OK", "CANCEL" };
                Toolkit.getDefaultToolkit().beep();
                int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure LogOut"+"\nClick OK to continue", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);

                if (selectedValue == JOptionPane.WHEN_FOCUSED) {

                    receptionMain_logoutButton.getScene().getWindow().hide();
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

    }

    public Pane getView(String fileName){
        //receptionView_task.setVisible(false);
       // System.out.println("you clicked receptionView//patient");
        MultipleFXMLLoader newFXML = new MultipleFXMLLoader();
        Pane view = newFXML.getPage(fileName);

        return view;
    }

    public void setReceptionViewCenter(Pane view){
        mainReceptionView.setCenter(view);

    }

    public void setReceptionViewLeft(Pane view){
        mainReceptionView.setLeft(view);
    }

    public void setUserName(String name){
        receptionView_userName.setText(name);
    }

    public Receptionist getCurrentreceptionist() {
        return currentreceptionist;
    }

    public void setCurrentreceptionist(Receptionist currentreceptionist) {
        this.currentreceptionist = currentreceptionist;
       // System.out.println("receptionist set in receptionView : "+currentreceptionist);
    }
}
