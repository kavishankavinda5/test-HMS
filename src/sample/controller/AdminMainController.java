package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import sample.Main;
import sample.model.Admin;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController {

    private Admin currentAdmin;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private BorderPane adminMain_boarderPane;
    @FXML private ImageView adminMain_mainHome;
    @FXML private JFXButton adminMain_reference;
    @FXML private JFXButton adminMain_user;
    @FXML private JFXButton adminMain_postal;
    @FXML private JFXButton adminMain_appointment;
    @FXML private JFXButton adminMain_complaint;
    @FXML private JFXButton adminMain_generateReport;
    @FXML private Pane adminMain_logout;
    @FXML private JFXButton adminMain_logoutButton;
    @FXML private ImageView adminMain_backIcon;
    @FXML private AnchorPane adminMain_loaderPane;
    @FXML private JFXTextField adminMain_userName;
    @FXML private JFXTextField adminMain_userID;
    @FXML private JFXButton adminMain_profile;


    @FXML void initialize() {

        adminMain_reference.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/referenceView");
                    Pane view = Main.getView("taskView/referenceView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        adminMain_user.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/userView");
                    Pane view = Main.getView("taskView/userView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        adminMain_postal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/postalView");
                    Pane view = Main.getView("taskView/postalView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        adminMain_appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/appointmentView");
                    Pane view = Main.getView("taskView/appointmentView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        adminMain_complaint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/complaint");
                    Pane view = Main.getView("taskView/complaintView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        adminMain_generateReport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/reportGenerateView");
                    Pane view = Main.getView("taskView/reportGenerateView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        adminMain_mainHome.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                adminMain_boarderPane.setCenter(adminMain_loaderPane);
            }
        });

        adminMain_logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Main main = new Main();
                main.backToMain(adminMain_logoutButton.getScene());
            }

        });

        adminMain_profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //System.out.println("taskView/profileSettingView");
                    Pane view = Main.getView("taskView/profileSettingView");
                    setReceptionViewCenter(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void setReceptionViewCenter(Pane view){
        adminMain_boarderPane.setCenter(view);

    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(Admin currentAdmin) {

        this.currentAdmin =currentAdmin;
        //System.out.println("admin set in adminView : "+currentAdmin);
    }


}
