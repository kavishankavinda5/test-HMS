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
import sample.model.MedicalOfficer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MedicalOfficerController {

    private MedicalOfficer currentmedicalofficer;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private BorderPane medicalMain_boarderPane;
    @FXML private ImageView medicalMain_mainHome;
    @FXML private JFXButton medicalMain_appointment;
    @FXML private Pane medicalMain_logout;
    @FXML private JFXButton medicalMain_logoutButton;
    @FXML private JFXButton medicalMain_profile;
    @FXML private ImageView adminMain_backIcon;
    @FXML private AnchorPane medicalMain_loaderPane;


    @FXML void initialize() {

        medicalMain_mainHome.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                medicalMain_boarderPane.setCenter(medicalMain_loaderPane);
            }
        });

        medicalMain_appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/appointmentView");
                    Pane view = Main.getView("taskView/appointmentView");
                    setMedicalViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        medicalMain_profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/profileSettingView");
                    Pane view = Main.getView("taskView/profileSettingView");
                    setMedicalViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        medicalMain_logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Object[] options = { "OK", "CANCEL" };
                Toolkit.getDefaultToolkit().beep();
                int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure LogOut"+"\nClick OK to continue", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);

                if (selectedValue == JOptionPane.WHEN_FOCUSED) {

                    medicalMain_logoutButton.getScene().getWindow().hide();
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

    public void setMedicalViewCenter(Pane view){
        medicalMain_boarderPane.setCenter(view);

    }

    public MedicalOfficer getCurrentmedicalofficer() {
        return currentmedicalofficer;
    }

    public void setCurrentmedicalofficer(MedicalOfficer currentmedicalofficer) {
        this.currentmedicalofficer = currentmedicalofficer;
        //System.out.println("medicalOfficer set in officerView : "+currentmedicalofficer);
    }
}
