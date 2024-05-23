package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Main;
import sample.controller.actionTask.UserAction;
import sample.controller.taskControllers.SystemDataWriter;
import sample.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static sample.Main.*;

public class CustomLoginController {

    private static String userLogFile ="src/sample/fileStorage/logData/loginDataLog.txt";
    private String userRoll;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Label customLogin_invalidMessage;
    @FXML private Label userLogin_userLable;
    @FXML private JFXTextField userLogin_userName;
    @FXML private JFXPasswordField userLogin_userPassword;
    @FXML private JFXButton userLogin_SigninButoon;
    @FXML private Hyperlink userLogin_forgetPassword;
    @FXML private Label loginView_timelable;
    @FXML private Label loginView_dateLabel;
    @FXML private Label userLogin_diaplayData;
    @FXML private JFXButton userLogin_backButton;

    @FXML
    void initialize() {

        liveClock(loginView_timelable);

        userLogin_SigninButoon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userLogin_userName.getText().length() <= 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if (userLogin_userPassword.getText().length() <= 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Password is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {

                    //get data from the view and store it int variables
                    String inputUserName = userLogin_userName.getText().trim();
                    String inputPassword = userLogin_userPassword.getText().trim();

                    int i = UserAction.verifyLogin(inputUserName,inputPassword, userRoll.toLowerCase().trim());

                    if (i == 1){
                        switch (userRoll){
                            case "Admin":
                                openDashBoard("adminMainView");
                                break;
                            case "Reception":
                                openDashBoard("receptionistMainView");
                                break;
                            case "Patient":
                                openDashBoard("patientMainView");
                                break;
                            case "MedicalOfficer":
                                openDashBoard("medicalOfficerView");
                                break;

                            default:
                                customLogin_invalidMessage.setText("Invalid User Input !!!");

                        }
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        customLogin_invalidMessage.setText("Invalid User Input !!!");
                        JOptionPane.showMessageDialog(null, "Invalid User Input", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }


            }
        });

        userLogin_backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Object[] options = { "OK", "CANCEL" };
                Toolkit.getDefaultToolkit().beep();
                int selectedValue = JOptionPane.showOptionDialog(null, "Are You Sure To Go Back"+"\nClick OK to continue", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);

                if (selectedValue == JOptionPane.WHEN_FOCUSED) {

                    userLogin_backButton.getScene().getWindow().hide();
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
                    detailsStage.setResizable(false);
                    detailsStage.setTitle("Login Windows");
                    detailsStage.show();

                }

            }
        });


    }
    public void setUserLoginLable(String name){
        userLogin_userLable.setText(name+" Login");
        this.userRoll = name;
    }

    private void openDashBoard(String fileName){
        userLogin_SigninButoon.getScene().getWindow().hide();
        UserLoginLog userLoginLog =null;

        Stage dashBoardStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/view/dashBoards/"+fileName+".fxml"));

        try {
            loader.load();

        }catch (IOException e){
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        dashBoardStage.setScene(new Scene(root));
        String dashBoardTitile=null;

        switch (fileName){
            case "patientMainView":
                Patient patientDetails = UserAction.searchPatient(null,userLogin_userName.getText(),userLogin_userPassword.getText());
                SystemUser systemUser = new SystemUser();
                systemUser.setUserRoll(UserRoll.PATIENT);
                systemUser.setPatient(patientDetails);
                Main.setCurrentSystemUser(systemUser);
                userLoginLog = new UserLoginLog(LocalDate.now(),
                        LocalTime.now(),
                        userLogin_userName.getText(),
                        UserRoll.PATIENT);
                PatientViewController patientViewController = loader.getController();
                patientViewController.setCurrentpatient(patientDetails);
                dashBoardTitile="User Dashboard";
                break;

            case "receptionistMainView":
                Receptionist receptionRecord = UserAction.searchReceptionRecord(null,userLogin_userName.getText(),userLogin_userPassword.getText());
                SystemUser receptionSystemUser = new SystemUser();
                receptionSystemUser.setUserRoll(UserRoll.RECEPTIONIST);
                receptionSystemUser.setReceptionist(receptionRecord);
                Main.setCurrentSystemUser(receptionSystemUser);
                userLoginLog = new UserLoginLog(LocalDate.now(),
                        LocalTime.now(),
                        userLogin_userName.getText(),
                        UserRoll.RECEPTIONIST);
                ReceptionMainViewController receptionMainViewController = loader.getController();
                receptionMainViewController.setCurrentreceptionist(receptionRecord);
                dashBoardTitile="Receptionist Dashboard";
                break;
            case "adminMainView":

                Admin adminDetails = UserAction.searchAdmin(userLogin_userName.getText(),userLogin_userPassword.getText());
                //System.out.println("found Admin record in ----->"+adminDetails);
                AdminMainController adminMainController = loader.getController();
                SystemUser adminSysUser = new SystemUser();
                adminSysUser.setUserRoll(UserRoll.ADMIN);
                adminSysUser.setAdmin(adminDetails);
                Main.setCurrentSystemUser(adminSysUser);
                userLoginLog = new UserLoginLog(LocalDate.now(),
                        LocalTime.now(),
                        userLogin_userName.getText(),
                        UserRoll.ADMIN);
                adminMainController.setCurrentAdmin(adminDetails);
                dashBoardTitile="Admin Dashboard";
                break;
            case "medicalOfficerView":
                MedicalOfficer medicalOfficer = UserAction.searchMedicalOfficer(userLogin_userName.getText(),userLogin_userPassword.getText());
                SystemUser medicalSysUser = new SystemUser();
                medicalSysUser.setUserRoll(UserRoll.MEDICALOFFICER);
                medicalSysUser.setMedicalOfficer(medicalOfficer);
                Main.setCurrentSystemUser(medicalSysUser);
                userLoginLog = new UserLoginLog(LocalDate.now(),
                        LocalTime.now(),
                        userLogin_userName.getText(),
                        UserRoll.MEDICALOFFICER);
                MedicalOfficerController medicalOfficerController = loader.getController();
                medicalOfficerController.setCurrentmedicalofficer(medicalOfficer);
                dashBoardTitile="Medical Officer Dashboard";
                break;
            default:
                break;
        }
        SystemDataWriter systemDataWriter=new SystemDataWriter();
        systemDataWriter.writeDataToFile(userLoginLog.toString(),userLogFile,5);

        dashBoardStage.setTitle(dashBoardTitile);
        dashBoardStage.setResizable(false);
        dashBoardStage.show();
    }

}
