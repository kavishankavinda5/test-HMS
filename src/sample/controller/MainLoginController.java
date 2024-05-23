   package sample.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static sample.Main.liveClock;

public class MainLoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView mainLogin_adminView;

    @FXML
    private ImageView mainLogin_receptionView;

    @FXML
    private ImageView mainLogin_medicalOfficer;

    @FXML
    private ImageView mainLogin_patientLogin;

    @FXML
    private Label mainLogin_DateTime;

    @FXML
    void initialize() {

        liveClock(mainLogin_DateTime);

        mainLogin_adminView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                loadUserLogin("customLoginWindow", "Admin");
            }
        });

        mainLogin_receptionView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                loadUserLogin("customLoginWindow", "Reception");
            }
        });

        mainLogin_medicalOfficer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                loadUserLogin("customLoginWindow", "MedicalOfficer");
            }
        });

        mainLogin_patientLogin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                loadUserLogin("customLoginWindow", "Patient");
            }
        });

    }

    public void loadUserLogin(String fileName, String viewName){

        mainLogin_adminView.getScene().getWindow().hide();
        Stage detailsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/sample/view/"+fileName+".fxml"));
        try {
            loader.load();

        }catch (IOException e){
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        detailsStage.setScene(new Scene(root));

        CustomLoginController detailsController = loader.getController();
        detailsController.setUserLoginLable(viewName);
        detailsStage.show();
    }



}




