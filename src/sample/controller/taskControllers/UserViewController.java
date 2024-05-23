package sample.controller.taskControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.StringLengthValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.StringConverter;
import sample.Main;
import sample.controller.actionTask.ReferenceAction;
import sample.controller.actionTask.UserAction;
import sample.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {
    Boolean isPatientTSet =false;
    Boolean isMedicalTSet =false;
    Boolean isReceptionTset =false;
    Boolean isAdminTset = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML private Label userView_userNameLable;
    @FXML private JFXButton userView_addUser;
    @FXML private JFXButton userView_updateUser;
    @FXML private JFXButton userView_deleteUser;
    @FXML private JFXButton userView_viewAll;
    @FXML private JFXButton userView_reset;
    @FXML private JFXTextField userView_name;
    @FXML private JFXTextField userView_phoneNum;
    @FXML private JFXTextField userView_NIC;
    @FXML private DatePicker userView_dob;
    @FXML private JFXTextArea userView_address;
    @FXML private JFXComboBox<String> userView_marital;
    @FXML private JFXTextField userView_userName;
    @FXML private JFXTextField userView_allergies;
    @FXML private JFXComboBox<BloodGroup> userView_bloodGroup;
    @FXML private JFXComboBox<Gender> userView_gender;
    @FXML private JFXTextField userView_staffID;
    @FXML private JFXTextField userView_staffEmail;
    @FXML private DatePicker userView_staffdoj;
    @FXML private TextField userView_searchField;
    @FXML private JFXButton userView_searchButton;
    @FXML private JFXComboBox<String> userView_speciality;
    @FXML private JFXComboBox<UserRoll> userView_userTypeDrop;
    @FXML private JFXPasswordField userView_userPassword;
    @FXML private TabPane userView_mainTabPane;

    @FXML private JFXButton userView_UploadPhoto;
    @FXML private Label userView_UploadPhoto_Path;
    @FXML private JFXButton userView_UploadFile;
    @FXML private Label userView_UploadFile_Path;
    @FXML private Label userView_label_name;
    @FXML private HBox profile_userPhoto;
    @FXML private Label noDocument;
    @FXML private JFXButton openDocument;
    @FXML private Window primaryStage;
    

    @FXML public void initialize(URL url, ResourceBundle rb) {

        //Validate User Inputs
        validateInitialize();

        //Reset All Data
        resetDisplay();

        //set the drop down wit the data taken by the reference module
        userView_userTypeDrop.getItems().addAll(ReferenceAction.getUserRolls());
        userView_speciality.getItems().addAll(ReferenceAction.getDocSpecialityStringArray());
        userView_gender.getItems().addAll(ReferenceAction.getGender());
        userView_bloodGroup.getItems().addAll(ReferenceAction.getBloogGroup());
        userView_marital.getItems().addAll(ReferenceAction.getMaritalStatus());

        setViewForSystemUser();

        userView_userTypeDrop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switch (userView_userTypeDrop.getValue()){
                    case ADMIN:
                        setViewForAdmin();
                        break;
                    case PATIENT:
                        setViewForPatient();
                        break;
                    case RECEPTIONIST:
                        setViewForReception();
                        break;
                    case MEDICALOFFICER:
                        setViewForMedicalOfficer();
                        break;

                    default:
                        break;
                }
            }
        });

        userView_addUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                userView_dob.setConverter(new StringConverter<LocalDate>() {
                    String pattern = "yyyy-MM-dd";
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                    {
                        userView_dob.setPromptText(pattern.toLowerCase());
                    }

                    @Override public String toString(LocalDate date) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override public LocalDate fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                });

                if(checkInputs()) {

                    switch (userView_userTypeDrop.getValue()) {

                        case PATIENT:

                            if (userView_bloodGroup.getSelectionModel().getSelectedIndex() < 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Blood Group is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_allergies.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Allergies is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {
                                //Check Input Field Of Allergy is text
                                RegexValidator regexValidator = new RegexValidator();
                                regexValidator.setRegexPattern("[A-Za-z\\s]+");
                                userView_allergies.getValidators().add(regexValidator);
                                userView_allergies.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  userView_allergies.validate();
                                });

                                if (validateInputs() && userView_allergies.validate()) {

                                    if (userView_UploadPhoto_Path.getText() == null){
                                        Object[] options = { "OK", "CANCEL" };
                                        Toolkit.getDefaultToolkit().beep();
                                        int selectedValue = JOptionPane.showOptionDialog(null,
                                                "Do You Want to Add This Record With Out UserPhoto", "Warning",
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                null, options, options[0]);

                                        if ((selectedValue == JOptionPane.WHEN_FOCUSED)&&(userView_UploadFile_Path.getText() != null)) {
                                            UserAction.addPatient(getPatient(), UserRoll.ADMIN);
                                        }
                                    }
                                    if (userView_UploadFile_Path.getText() == null){
                                        Object[] options = { "OK", "CANCEL" };
                                        Toolkit.getDefaultToolkit().beep();
                                        int selectedValue = JOptionPane.showOptionDialog(null,
                                                "Do You Want to Add This Record With Out Document", "Warning",
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                null, options, options[0]);

                                        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                                            UserAction.addPatient(getPatient(), UserRoll.ADMIN);
                                        }
                                    }
                                    if ((userView_UploadPhoto_Path.getText()!=null)&&(userView_UploadFile_Path.getText()!=null)){
                                        UserAction.addPatient(getPatient(), UserRoll.ADMIN);
                                    }

                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            break;


                        case RECEPTIONIST:

                            if (userView_staffEmail.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Staff Email is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_staffdoj.getValue()==null){
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Join Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Email Address
                                RegexValidator regexValidator_email = new RegexValidator();
                                regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                                userView_staffEmail.getValidators().add(regexValidator_email);
                                userView_staffEmail.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  userView_staffEmail.validate();
                                });

                                if (validateInputs() && userView_staffEmail.validate() ) {

                                    if (userView_UploadPhoto_Path.getText() == null){
                                        Object[] options = { "OK", "CANCEL" };
                                        Toolkit.getDefaultToolkit().beep();
                                        int selectedValue = JOptionPane.showOptionDialog(null,
                                                "Do You Want to Add This Record With Out UserPhoto", "Warning",
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                null, options, options[0]);

                                        if ((selectedValue == JOptionPane.WHEN_FOCUSED)&&(userView_UploadFile_Path.getText() != null)) {
                                            UserAction.addReceptionist(getInitialReceptionist(), UserRoll.ADMIN);
                                        }
                                    }
                                    if (userView_UploadFile_Path.getText() == null){
                                        Object[] options = { "OK", "CANCEL" };
                                        Toolkit.getDefaultToolkit().beep();
                                        int selectedValue = JOptionPane.showOptionDialog(null,
                                                "Do You Want to Add This Record With Out Document", "Warning",
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                null, options, options[0]);

                                        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                                            UserAction.addReceptionist(getInitialReceptionist(), UserRoll.ADMIN);
                                        }
                                    }
                                    if ((userView_UploadPhoto_Path.getText()!=null)&&(userView_UploadFile_Path.getText()!=null)){
                                        UserAction.addReceptionist(getInitialReceptionist(), UserRoll.ADMIN);
                                    }

                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            break;

                        case ADMIN:

                            if (validateInputs()) {

                                if (userView_UploadPhoto_Path.getText() == null){
                                    Object[] options = { "OK", "CANCEL" };
                                    Toolkit.getDefaultToolkit().beep();
                                    int selectedValue = JOptionPane.showOptionDialog(null,
                                            "Do You Want to Add This Record With Out UserPhoto", "Warning",
                                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                            null, options, options[0]);

                                    if ((selectedValue == JOptionPane.WHEN_FOCUSED)&&(userView_UploadFile_Path.getText() != null)) {
                                        UserAction.addAdmin(getAdmin(), UserRoll.ADMIN);
                                    }
                                }
                                if (userView_UploadFile_Path.getText() == null){
                                    Object[] options = { "OK", "CANCEL" };
                                    Toolkit.getDefaultToolkit().beep();
                                    int selectedValue = JOptionPane.showOptionDialog(null,
                                            "Do You Want to Add This Record With Out Document", "Warning",
                                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                            null, options, options[0]);

                                    if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                                        UserAction.addAdmin(getAdmin(), UserRoll.ADMIN);
                                    }
                                }
                                if ((userView_UploadPhoto_Path.getText()!=null)&&(userView_UploadFile_Path.getText()!=null)){
                                    UserAction.addAdmin(getAdmin(), UserRoll.ADMIN);
                                }

                            }
                            else {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }

                            break;

                        case MEDICALOFFICER:

                            if (userView_staffEmail.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Staff Email is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_staffdoj.getValue()==null){
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Join Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            else if (userView_speciality.getSelectionModel().getSelectedIndex() < 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Speciality is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Email Address
                                RegexValidator regexValidator_email = new RegexValidator();
                                regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                                userView_staffEmail.getValidators().add(regexValidator_email);
                                userView_staffEmail.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  userView_staffEmail.validate();
                                });

                                if (validateInputs() && userView_staffEmail.validate() ) {

                                    if (userView_UploadPhoto_Path.getText() == null){
                                        Object[] options = { "OK", "CANCEL" };
                                        Toolkit.getDefaultToolkit().beep();
                                        int selectedValue = JOptionPane.showOptionDialog(null,
                                                "Do You Want to Add This Record With Out UserPhoto", "Warning",
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                null, options, options[0]);

                                        if ((selectedValue == JOptionPane.WHEN_FOCUSED)&&(userView_UploadFile_Path.getText() != null)) {
                                            UserAction.addMedicalOfficer(getInitialMedicalOfficer(), UserRoll.ADMIN);
                                        }
                                    }
                                    if (userView_UploadFile_Path.getText() == null){
                                        Object[] options = { "OK", "CANCEL" };
                                        Toolkit.getDefaultToolkit().beep();
                                        int selectedValue = JOptionPane.showOptionDialog(null,
                                                "Do You Want to Add This Record With Out Document", "Warning",
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                                null, options, options[0]);

                                        if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                                            UserAction.addMedicalOfficer(getInitialMedicalOfficer(), UserRoll.ADMIN);
                                        }
                                    }
                                    if ((userView_UploadPhoto_Path.getText()!=null)&&(userView_UploadFile_Path.getText()!=null)){
                                        UserAction.addMedicalOfficer(getInitialMedicalOfficer(), UserRoll.ADMIN);
                                    }

                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            break;

                    }

                }

            }
        });

        userView_searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String serachID = userView_searchField.getText();

                if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if (userView_searchField.getText().length() <= 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Search ID is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {

                    /*
                    //Check Input Field Of NIC
                    RegexValidator regexValidator_nic = new RegexValidator();
                    regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");

                    userView_searchField.getValidators().add(regexValidator_nic);
                    userView_searchField.focusedProperty().addListener((o, oldValue, newValue) -> {
                        if(!newValue)  userView_searchField.validate();
                    });
                    */

                    switch (userView_userTypeDrop.getValue()) {

                        case RECEPTIONIST:
                            Receptionist receptionist = UserAction.searchReceptionRecord(serachID, null, null);
                            displayReceptionistData(receptionist);
                            viewReceptionPhoto_File(receptionist);
                            break;

                        case MEDICALOFFICER:
                            //MedicalOfficer medicalOfficer = UserAction.searchMedicalOfficer(serachID, UserAction.medicalOfficerFilePath);
                            MedicalOfficer medicalOfficer = UserAction.searchMedicalOfficerRecord(serachID,null);
                            displayMedicalOfficerData(medicalOfficer);
                            viewMedicalOfficerPhoto_File(medicalOfficer);
                            break;

                        case PATIENT:

                            Patient patient = UserAction.searchPatient(serachID, null, null);
                            displayPatientData(patient);
                            viewPatientPhoto_File(patient);
                            break;

                        case ADMIN:
                            Admin admin = UserAction.searchAdmin(serachID, null);
                            displayAdminData(admin);
                            viewAdminPhoto_File(admin);
                            break;

                    }

                }
            }
        });

        userView_deleteUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if (userView_label_name.getText()==null){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter ID No and Search"+"\nor\n"+"Please Select a Record from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    UserAction.deleteUserRecord(UserRoll.ADMIN, userView_searchField.getText(), userView_userTypeDrop.getValue(), getLoginUser());

                }

            }
        });

        userView_updateUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if (userView_label_name.getText()==null){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter ID No and Search"+"\nor\n"+"Please Select a Record from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if(checkInputs()) {

                    switch (userView_userTypeDrop.getValue()) {

                        case PATIENT:

                            if (userView_bloodGroup.getSelectionModel().getSelectedIndex() < 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Blood Group is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_allergies.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Allergies is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Allergy is text
                                RegexValidator regexValidator = new RegexValidator();
                                regexValidator.setRegexPattern("[A-Za-z\\s]+");
                                userView_allergies.getValidators().add(regexValidator);
                                userView_allergies.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  userView_allergies.validate();
                                });

                                if (validateInputs() && userView_allergies.validate()) {
                                    UserAction.updatePatientRecord(UserRoll.ADMIN, getPatient(), userView_searchField.getText(), getLoginUser());
                                    resetDisplay();
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;


                        case RECEPTIONIST:

                            if (userView_staffEmail.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Staff Email is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_staffdoj.getValue()==null){
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Join Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Email Address
                                RegexValidator regexValidator_email = new RegexValidator();
                                regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                                userView_staffEmail.getValidators().add(regexValidator_email);
                                userView_staffEmail.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  userView_staffEmail.validate();
                                });

                                if (validateInputs() && userView_staffEmail.validate()) {
                                    UserAction.updateReceptionRecord(UserRoll.ADMIN, getReceptionist(), userView_searchField.getText(), getLoginUser());
                                    resetDisplay();
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            break;

                        case ADMIN:

                            if (validateInputs()) {
                                UserAction.updateAdmin(UserRoll.ADMIN, getAdmin(), userView_searchField.getText(), getLoginUser());
                                resetDisplay();
                            }
                            else {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }

                            break;

                        case MEDICALOFFICER:

                            if (userView_staffEmail.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Staff Email is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_staffdoj.getValue()==null){
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Join Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (userView_speciality.getSelectionModel().getSelectedIndex() < 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Speciality is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Email Address
                                RegexValidator regexValidator_email = new RegexValidator();
                                regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                                userView_staffEmail.getValidators().add(regexValidator_email);
                                userView_staffEmail.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  userView_staffEmail.validate();
                                });

                                if (validateInputs() && userView_staffEmail.validate() ) {
                                    UserAction.updateMedicalOfficerRecord(UserRoll.ADMIN, getMedicalOfficer(), userView_searchField.getText(), getLoginUser());
                                    resetDisplay();
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            break;
                    }
                }
            }
        });

        userView_reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                resetDisplay();
            }
        });

        userView_viewAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    switch (userView_userTypeDrop.getValue()){
                        case ADMIN:
                            reception_Tab.setDisable(true);
                            medical_tab.setDisable(true);
                            patientTable_Tab.setDisable(true);
                            admin_tab.setDisable(false);
                            userView_mainTabPane.getSelectionModel().select(admin_tab);
                            userView_mainTabPane.setDisable(false);
                            setAdminTable(UserAction.getAllAdmin());
                            break;
                        case RECEPTIONIST:
                            userView_mainTabPane.setDisable(false);
                            reception_Tab.setDisable(false);
                            admin_tab.setDisable(true);
                            medical_tab.setDisable(true);
                            patientTable_Tab.setDisable(true);
                            userView_mainTabPane.getSelectionModel().select(reception_Tab);
                            setReceptionTable(UserAction.getAllReceptionist());
                            break;
                        case MEDICALOFFICER:
                            userView_mainTabPane.setDisable(false);
                            medical_tab.setDisable(false);
                            admin_tab.setDisable(true);
                            patientTable_Tab.setDisable(true);
                            reception_Tab.setDisable(true);
                            userView_mainTabPane.getSelectionModel().select(medical_tab);
                            setMedicalOfficerTable(UserAction.getAllMedicalOfficer());
                            break;
                        case PATIENT:
                            userView_mainTabPane.setDisable(false);
                            patientTable_Tab.setDisable(false);
                            admin_tab.setDisable(true);
                            reception_Tab.setDisable(true);
                            medical_tab.setDisable(true);
                            userView_mainTabPane.getSelectionModel().select(patientTable_Tab);
                            setPatientTable(UserAction.getAllPatients());
                            break;
                        default:
                            break;
                    }

                }

            }
        });

        userV_PatientTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Patient patient =userV_PatientTable.getSelectionModel().getSelectedItem();
                userView_searchField.setText(patient.getIdCardNumber());
                displayPatientData(patient);
                viewPatientPhoto_File(patient);
            }
        });

        admin_mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Admin admin = admin_mainTable.getSelectionModel().getSelectedItem();
                userView_searchField.setText(admin.getIdCardNumber());
                displayAdminData(admin);
                viewAdminPhoto_File(admin);
            }
        });

        receptiontable_mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Receptionist receptionist =receptiontable_mainTable.getSelectionModel().getSelectedItem();
                userView_searchField.setText(receptionist.getIdCardNumber());
                displayReceptionistData(receptionist);
                viewReceptionPhoto_File(receptionist);
            }
        });

        medicalOfficer_mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MedicalOfficer medicalOfficer =medicalOfficer_mainTable.getSelectionModel().getSelectedItem();
                userView_searchField.setText(medicalOfficer.getIdCardNumber());
                displayMedicalOfficerData(medicalOfficer);
                viewMedicalOfficerPhoto_File(medicalOfficer);
            }
        });

        userView_UploadPhoto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    FileChooser fileChooser = new FileChooser();
                    // Set title for FileChooser
                    fileChooser.setTitle("Select Your Picture");
                    // Add Extension Filters
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                            new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));

                    switch (userView_userTypeDrop.getValue()) {

                        case ADMIN:
                        case RECEPTIONIST:
                        case PATIENT:
                        case MEDICALOFFICER:

                            File file = fileChooser.showOpenDialog(primaryStage);

                            if (file != null)
                            {
                                userView_UploadPhoto_Path.setText(file.getAbsolutePath());

                                profile_userPhoto.getChildren().clear();

                                try {

                                    if (Files.isReadable(Path.of(file.getAbsolutePath()))){
                                        FileInputStream imageStream = new FileInputStream(file.getAbsoluteFile());
                                        javafx.scene.image.Image image = new Image(imageStream);
                                        ImageView view = new ImageView();
                                        view.setImage(image);
                                        view.setFitWidth(130);
                                        view.setFitHeight(130);
                                        view.setSmooth(true);
                                        //view.setPreserveRatio(true);
                                        profile_userPhoto.getChildren().add(view);
                                        System.out.println("Preview PHOTO");
                                    }else {
                                        System.out.println("No Profile Photo");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                System.out.println("No Select Photo");
                            }


                            break;
                    }
                }
            }
        });

        userView_UploadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {

                    FileChooser fileChooser = new FileChooser();
                    // Set title for FileChooser
                    fileChooser.setTitle("Select File");
                    // Add Extension Filters
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("PDF", "*.pdf"));

                    switch (userView_userTypeDrop.getValue()) {

                        case ADMIN:
                        case RECEPTIONIST:
                        case PATIENT:
                        case MEDICALOFFICER:

                            File file = fileChooser.showOpenDialog(primaryStage);

                            if (file != null)
                            {
                                userView_UploadFile_Path.setText(file.getAbsolutePath());
                                noDocument.setVisible(false);
                                openDocument.setVisible(true);
                            }
                            else {
                                noDocument.setVisible(true);
                                openDocument.setVisible(false);
                                System.out.println("No Select Document");
                            }

                            break;

                    }
                }
            }
        });

        openDocument.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (userView_UploadFile_Path.getText()!=null){
                        Desktop.getDesktop().open(new File(userView_UploadFile_Path.getText()));
                    }
                    else {
                         if (Files.isReadable(Path.of(path))){
                             Desktop.getDesktop().open(new File(path));
                             System.out.println("Preview PDF");
                         }else {
                             JOptionPane.showMessageDialog(null,"NO Document");
                         }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void setViewForSystemUser(){
        switch (Main.getCurrentSystemUser().getUserRoll()){
            case ADMIN :
                break;
            case RECEPTIONIST:
                setUserViewForReception();
                break;
            default:
                break;
        }
    }

    private void setUserViewForReception() {
        userView_userTypeDrop.setValue(UserRoll.PATIENT);
        userView_userTypeDrop.setDisable(true);
        admin_mainTable.setVisible(false);
        receptiontable_mainTable.setVisible(false);
        medicalOfficer_mainTable.setVisible(false);

    }

    private LoginUser getLoginUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        loginUser.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));

        return loginUser;
    }

    public void displayPatientData(Patient patient){
        userView_userTypeDrop.setValue(patient.getUserRoll());
        userView_name.setText(patient.getName());
        userView_gender.setValue(patient.getGender());
        userView_marital.setValue(patient.getMaritalStatus());
        userView_dob.setValue(patient.getDob());
        userView_phoneNum.setText(patient.getPhoneNumber());
        userView_NIC.setText(patient.getIdCardNumber());
        userView_address.setText(patient.getAddress());
        userView_userName.setText(UserAction.decryptUserData(patient.getUserName()));
        userView_userPassword.setText(UserAction.decryptUserData(patient.getUserPassword()));
        userView_bloodGroup.setValue(patient.getBloodGroup());
        userView_allergies.setText(patient.getAllergies());
        userView_label_name.setText(patient.getName());
    }

    public void displayMedicalOfficerData(MedicalOfficer medicalOfficer){
        userView_userTypeDrop.setValue(medicalOfficer.getUserRoll());
        userView_name.setText(medicalOfficer.getName());
        userView_gender.setValue(medicalOfficer.getGender());
        userView_marital.setValue(medicalOfficer.getMaritalStatus());
        userView_dob.setValue(medicalOfficer.getDob());
        userView_phoneNum.setText(medicalOfficer.getPhoneNumber());
        userView_NIC.setText(medicalOfficer.getIdCardNumber());
        userView_address.setText(medicalOfficer.getAddress());
        userView_userName.setText(UserAction.decryptUserData(medicalOfficer.getUserName()));
        userView_userPassword.setText(UserAction.decryptUserData(medicalOfficer.getUserPassword()));
        userView_staffID.setText(String.valueOf(medicalOfficer.getStaffID()));
        userView_staffEmail.setText(medicalOfficer.getStaffEmailAddress());
        userView_staffdoj.setValue(medicalOfficer.getDateOfJoining());
        userView_speciality.setValue(medicalOfficer.getSpeciality().trim());
        userView_label_name.setText(medicalOfficer.getName());


    }

    public void displayAdminData(Admin admin){
        userView_userTypeDrop.setValue(admin.getUserRoll());
        userView_name.setText(admin.getName());
        userView_gender.setValue(admin.getGender());
        userView_marital.setValue(admin.getMaritalStatus());
        userView_dob.setValue(admin.getDob());
        userView_phoneNum.setText(admin.getPhoneNumber());
        userView_NIC.setText(admin.getIdCardNumber());
        userView_address.setText(admin.getAddress());
        userView_userName.setText(UserAction.decryptUserData(admin.getUserName()));
        userView_userPassword.setText(UserAction.decryptUserData(admin.getUserPassword()));
        userView_label_name.setText(admin.getName());
    }

    public void displayReceptionistData(Receptionist receptionist){
        userView_userTypeDrop.setValue(receptionist.getUserRoll());
        userView_name.setText(receptionist.getName());
        userView_gender.setValue(receptionist.getGender());
        userView_marital.setValue(receptionist.getMaritalStatus());
        userView_dob.setValue(receptionist.getDob());
        userView_phoneNum.setText(receptionist.getPhoneNumber());
        userView_NIC.setText(receptionist.getIdCardNumber());
        userView_address.setText(receptionist.getAddress());
        userView_userName.setText(UserAction.decryptUserData(receptionist.getUserName()));
        userView_userPassword.setText(UserAction.decryptUserData(receptionist.getUserPassword()));
        userView_staffdoj.setValue(receptionist.getDateOfJoining());
        userView_staffEmail.setText(receptionist.getStaffEmailAddress());
        userView_staffID.setText(String.valueOf(receptionist.getStaffID()));
        userView_label_name.setText(receptionist.getName());
    }

    public Patient getPatient(){
        Patient patient = new Patient();
        patient.setUserRoll(userView_userTypeDrop.getValue());
        patient.setName(userView_name.getText());
        patient.setGender(userView_gender.getValue());
        patient.setMaritalStatus(userView_marital.getValue());
        patient.setDob(userView_dob.getValue());
        patient.setPhoneNumber(userView_phoneNum.getText());
        patient.setIdCardNumber(userView_NIC.getText());
        patient.setAddress(userView_address.getText());
        patient.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        patient.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));
        patient.setBloodGroup(userView_bloodGroup.getValue());
        patient.setAllergies(userView_allergies.getText());

        patient.setPhotoPath(userView_UploadPhoto_Path.getText());
        patient.setFilePath(userView_UploadFile_Path.getText());

        return patient;
    }

    public Receptionist getInitialReceptionist(){
        Receptionist receptionist = new Receptionist();
        receptionist.setUserRoll(userView_userTypeDrop.getValue());
        receptionist.setName(userView_name.getText()); ;
        receptionist.setGender(userView_gender.getValue());
        receptionist.setMaritalStatus(userView_marital.getValue());
        receptionist.setDob(userView_dob.getValue()); ;
        receptionist.setPhoneNumber(userView_phoneNum.getText());
        receptionist.setIdCardNumber(userView_NIC.getText());
        receptionist.setAddress(userView_address.getText());
        receptionist.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        receptionist.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));
        receptionist.setStaffID(Main.getStaffID());
        receptionist.setStaffEmailAddress(userView_staffEmail.getText());
        receptionist.setDateOfJoining(userView_staffdoj.getValue());

        receptionist.setPhotoPath(userView_UploadPhoto_Path.getText());
        receptionist.setFilePath(userView_UploadFile_Path.getText());

        return receptionist;
    }

    public Receptionist getReceptionist(){
        Receptionist receptionist = new Receptionist();
        receptionist.setUserRoll(userView_userTypeDrop.getValue());
        receptionist.setName(userView_name.getText()); ;
        receptionist.setGender(userView_gender.getValue());
        receptionist.setMaritalStatus(userView_marital.getValue());
        receptionist.setDob(userView_dob.getValue()); ;
        receptionist.setPhoneNumber(userView_phoneNum.getText());
        receptionist.setIdCardNumber(userView_NIC.getText());
        receptionist.setAddress(userView_address.getText());
        receptionist.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        receptionist.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));
        receptionist.setStaffID(Integer.parseInt(userView_staffID.getText()));
        receptionist.setStaffEmailAddress(userView_staffEmail.getText());
        receptionist.setDateOfJoining(userView_staffdoj.getValue());

        receptionist.setPhotoPath(userView_UploadPhoto_Path.getText());
        receptionist.setFilePath(userView_UploadFile_Path.getText());


        return receptionist;
    }

    public Admin getAdmin(){
        Admin admin = new Admin();
        admin.setUserRoll(userView_userTypeDrop.getValue());
        admin.setName(userView_name.getText()); ;
        admin.setGender(userView_gender.getValue());
        admin.setMaritalStatus(userView_marital.getValue());
        admin.setDob(userView_dob.getValue()); ;
        admin.setPhoneNumber(userView_phoneNum.getText());
        admin.setAddress(userView_address.getText());
        admin.setIdCardNumber(userView_NIC.getText());
        admin.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        admin.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));

        admin.setPhotoPath(userView_UploadPhoto_Path.getText());
        admin.setFilePath(userView_UploadFile_Path.getText());


        return admin;

    }

    public MedicalOfficer getInitialMedicalOfficer(){
        MedicalOfficer medicalOfficer = new MedicalOfficer();

        medicalOfficer.setUserRoll(userView_userTypeDrop.getValue());
        medicalOfficer.setName(userView_name.getText()); ;
        medicalOfficer.setGender(userView_gender.getValue());
        medicalOfficer.setMaritalStatus(userView_marital.getValue());
        medicalOfficer.setDob(userView_dob.getValue()); ;
        medicalOfficer.setPhoneNumber(userView_phoneNum.getText());
        medicalOfficer.setIdCardNumber(userView_NIC.getText());
        medicalOfficer.setAddress(userView_address.getText());
        medicalOfficer.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        medicalOfficer.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));
        medicalOfficer.setStaffID(Main.getStaffID());
        medicalOfficer.setStaffEmailAddress(userView_staffEmail.getText());
        medicalOfficer.setSpeciality(userView_speciality.getValue());
        medicalOfficer.setDateOfJoining(userView_staffdoj.getValue());

        medicalOfficer.setPhotoPath(userView_UploadPhoto_Path.getText());
        medicalOfficer.setFilePath(userView_UploadFile_Path.getText());

        return medicalOfficer;
    }

    public  MedicalOfficer getMedicalOfficer(){

        MedicalOfficer medicalOfficer = new MedicalOfficer();

        medicalOfficer.setUserRoll(userView_userTypeDrop.getValue());
        medicalOfficer.setName(userView_name.getText()); ;
        medicalOfficer.setGender(userView_gender.getValue());
        medicalOfficer.setMaritalStatus(userView_marital.getValue());
        medicalOfficer.setDob(userView_dob.getValue()); ;
        medicalOfficer.setPhoneNumber(userView_phoneNum.getText());
        medicalOfficer.setIdCardNumber(userView_NIC.getText());
        medicalOfficer.setAddress(userView_address.getText());
        medicalOfficer.setUserName(UserAction.encryptUserData(userView_userName.getText()));
        medicalOfficer.setUserPassword(UserAction.encryptUserData(userView_userPassword.getText()));
        medicalOfficer.setStaffID(Integer.parseInt(userView_staffID.getText()));
        medicalOfficer.setStaffEmailAddress(userView_staffEmail.getText());
        medicalOfficer.setSpeciality(userView_speciality.getValue());
        medicalOfficer.setDateOfJoining(userView_staffdoj.getValue());

        medicalOfficer.setPhotoPath(userView_UploadPhoto_Path.getText());
        medicalOfficer.setFilePath(userView_UploadFile_Path.getText());

        return medicalOfficer;

    }

    public void resetDisplay(){
        userView_name.clear();
        userView_gender.setValue(null);
        userView_marital.setValue(null);
        userView_phoneNum.clear();
        userView_userPassword.clear();
        userView_NIC.clear();
        userView_userName.clear();
        userView_address.clear();
        userView_allergies.clear();
        userView_dob.setValue(null);
        userView_staffdoj.setValue(null);
        userView_gender.setValue(null);
        userView_speciality.setValue(null);
        userView_bloodGroup.setValue(null);
        userView_staffEmail.clear();
        userView_searchField.clear();
        userView_staffID.clear();
        userView_userTypeDrop.getItems();
        userV_PatientTable.setItems(null);
        admin_mainTable.setItems(null);
        receptiontable_mainTable.setItems(null);
        medicalOfficer_mainTable.setItems(null);
        userView_mainTabPane.setDisable(true);
        userView_label_name.setText(null);
        userView_UploadPhoto_Path.setText(null);
        userView_UploadFile_Path.setText(null);
        profile_userPhoto.getChildren().clear();
        noDocument.setVisible(false);
        openDocument.setVisible(false);


    }

    public void setViewForAdmin(){
        userView_name.setDisable(false);
        userView_gender.setDisable(false);
        userView_marital.setDisable(false);
        userView_dob.setDisable(false);
        userView_phoneNum.setDisable(false);
        userView_NIC.setDisable(false);
        userView_address.setDisable(false);
        userView_userName.setDisable(false);
        userView_userPassword.setDisable(false);
        userView_bloodGroup.setDisable(true);
        userView_allergies.setDisable(true);
        userView_staffID.setDisable(true);
        userView_staffEmail.setDisable(true);
        userView_staffdoj.setDisable(true);
        userView_speciality.setDisable(true);
    }

    public void setViewForReception(){
        userView_name.setDisable(false);
        userView_gender.setDisable(false);
        userView_marital.setDisable(false);
        userView_dob.setDisable(false);
        userView_phoneNum.setDisable(false);
        userView_NIC.setDisable(false);
        userView_address.setDisable(false);
        userView_userName.setDisable(false);
        userView_userPassword.setDisable(false);
        userView_bloodGroup.setDisable(true);
        userView_allergies.setDisable(true);
        userView_staffID.setDisable(true);
        userView_staffEmail.setDisable(false);
        userView_staffdoj.setDisable(false);
        userView_speciality.setDisable(true);
    }

    public void setViewForPatient(){
        userView_name.setDisable(false);
        userView_gender.setDisable(false);
        userView_marital.setDisable(false);
        userView_dob.setDisable(false);
        userView_phoneNum.setDisable(false);
        userView_NIC.setDisable(false);
        userView_address.setDisable(false);
        userView_userName.setDisable(false);
        userView_userPassword.setDisable(false);
        userView_bloodGroup.setDisable(false);
        userView_allergies.setDisable(false);
        userView_staffID.setDisable(true);
        userView_staffEmail.setDisable(true);
        userView_staffdoj.setDisable(true);
        userView_speciality.setDisable(true);
    }

    public void setViewForMedicalOfficer(){
        userView_name.setDisable(false);
        userView_gender.setDisable(false);
        userView_marital.setDisable(false);
        userView_dob.setDisable(false);
        userView_phoneNum.setDisable(false);
        userView_NIC.setDisable(false);
        userView_address.setDisable(false);
        userView_userName.setDisable(false);
        userView_userPassword.setDisable(false);
        userView_bloodGroup.setDisable(true);
        userView_allergies.setDisable(true);
        userView_staffID.setDisable(true);
        userView_staffEmail.setDisable(false);
        userView_staffdoj.setDisable(false);
        userView_speciality.setDisable(false);
    }


    public String path;

    //write a method for view photo
    private void viewAdminPhoto_File(Admin admin) {

        profile_userPhoto.getChildren().clear();

        try {
            String staffId = admin.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/admin";
            String photosavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photosavePath))){
                FileInputStream imageStream = new FileInputStream(photosavePath);
                javafx.scene.image.Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(130);
                view.setFitHeight(130);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String adminFilePath ="src/sample/fileStorage/moduleData/userData/userFile/admin";
            String fileSavePath = adminFilePath + "\\" + staffId + ".pdf";

            if (Files.isReadable(Path.of(fileSavePath))){
                noDocument.setVisible(false);
                openDocument.setVisible(true);
                path =fileSavePath;
            }else {

                noDocument.setVisible(true);
                openDocument.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //write a method for view Patient photo
    private void viewPatientPhoto_File(Patient patient) {

        profile_userPhoto.getChildren().clear();

        try {

            String staffId = patient.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/patient";
            String photosavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photosavePath))){
                FileInputStream imageStream = new FileInputStream(photosavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(130);
                view.setFitHeight(130);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/patient";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            if (Files.isReadable(Path.of(fileSavePath))){
                noDocument.setVisible(false);
                openDocument.setVisible(true);
                path =fileSavePath;
            }else {

                noDocument.setVisible(true);
                openDocument.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //write a method for view Reception photo
    private void viewReceptionPhoto_File(Receptionist receptionist) {

        profile_userPhoto.getChildren().clear();

        try {

            String staffId = receptionist.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/reception";
            String photosavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photosavePath))){
                FileInputStream imageStream = new FileInputStream(photosavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(130);
                view.setFitHeight(130);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/reception";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            if (Files.isReadable(Path.of(fileSavePath))){
                noDocument.setVisible(false);
                openDocument.setVisible(true);
                path =fileSavePath;
            }else {

                noDocument.setVisible(true);
                openDocument.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //write a method for view MedicalOfficer photo
    private void viewMedicalOfficerPhoto_File(MedicalOfficer medicalOfficer) {

        profile_userPhoto.getChildren().clear();

        try {

            String staffId = medicalOfficer.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/medicalOfficer";
            String photoSavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photoSavePath))){
                FileInputStream imageStream = new FileInputStream(photoSavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(130);
                view.setFitHeight(130);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/medicalOfficer";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            if (Files.isReadable(Path.of(fileSavePath))){
                noDocument.setVisible(false);
                openDocument.setVisible(true);
                path =fileSavePath;
            }else {

                noDocument.setVisible(true);
                openDocument.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void validateInitialize(){

        //Check Input Fields Of User Name and Allergies is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");
        regexValidator.setMessage("Only Text");

        userView_name.getValidators().add(regexValidator);
        userView_name.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_name.validate();
        });

        userView_allergies.getValidators().add(regexValidator);
        userView_allergies.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_allergies.validate();
        });

        //Check Input Field Of Email Address
        RegexValidator regexValidator_email = new RegexValidator();
        //regexValidator_email.setRegexPattern("^[A-Za-z0-9+_.-]+@(.+)$");
        regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        regexValidator_email.setMessage("Invalid Please Check");

        userView_staffEmail.getValidators().add(regexValidator_email);
        userView_staffEmail.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_staffEmail.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        userView_phoneNum.getValidators().add(regexValidator_phone_number);
        userView_phoneNum.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_phoneNum.validate();
        });

        //Check Input Field Of NIC
        RegexValidator regexValidator_nic = new RegexValidator();
        regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");
        regexValidator_nic.setMessage("Invalid Please Check");

        userView_NIC.getValidators().add(regexValidator_nic);
        userView_NIC.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_NIC.validate();
        });

    }

    public Boolean validateInputs(){

        Boolean dataInputs = false;

        //Check Input Field Of Name is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");

        userView_name.getValidators().add(regexValidator);
        userView_name.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_name.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");

        userView_phoneNum.getValidators().add(regexValidator_phone_number);
        userView_phoneNum.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_phoneNum.validate();
        });

        //Check Input Field Of NIC
        RegexValidator regexValidator_nic = new RegexValidator();
        regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");

        userView_NIC.getValidators().add(regexValidator_nic);
        userView_NIC.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  userView_NIC.validate();
        });

        if (userView_name.validate() && userView_phoneNum.validate() && userView_NIC.validate() ){
            dataInputs = true;
        }
            return dataInputs;
    }

    public Boolean checkInputs(){

        Boolean allCheck =false;

        if (userView_userTypeDrop.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "User Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_name.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_gender.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Gender is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_marital.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Marital is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_dob.getValue()==null){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Date of Birth is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_phoneNum.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Phone Number is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_NIC.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "NIC is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_address.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Address is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_userName.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "User Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (userView_userPassword.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Password is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else{
            allCheck = true;
        }
        return allCheck;
    }


    //Patient Table details
    @FXML private Tab patientTable_Tab;
    @FXML private TableView<Patient> userV_PatientTable;
    @FXML private TableColumn<Patient, String> pTable_name;
    @FXML private TableColumn<Patient, Gender> pTable_gender;
    @FXML private TableColumn<Patient,String > pTable_marital;
    @FXML private TableColumn<Patient, LocalDate> pTable_dob;
    @FXML private TableColumn<Patient, String> pTable_phone;
    @FXML private TableColumn<Patient, String> pTable_nic;
    @FXML private TableColumn<Patient, String> pTable_address;
    @FXML private TableColumn<Patient, String> pTable_UserName;
    @FXML private TableColumn<Patient, BloodGroup> pTable_bloodGroup;
    @FXML private TableColumn<Patient, String> pTable_Allergies;

    public void setPatientTable(ArrayList<Patient> patientArrayList){

        ObservableList<Patient> observableList = FXCollections.observableList(patientArrayList);
        if (!isPatientTSet){
            pTable_name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
            pTable_gender.setCellValueFactory(new PropertyValueFactory<Patient, Gender>("gender"));
            pTable_marital.setCellValueFactory(new PropertyValueFactory<Patient, String>("maritalStatus"));
            pTable_dob.setCellValueFactory(new PropertyValueFactory<Patient, LocalDate>("dob"));
            pTable_phone.setCellValueFactory(new PropertyValueFactory<Patient, String>("phoneNumber"));
            pTable_nic.setCellValueFactory(new PropertyValueFactory<Patient, String>("idCardNumber"));
            pTable_address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
            pTable_UserName.setCellValueFactory(new PropertyValueFactory<Patient, String>("userName"));
            pTable_bloodGroup.setCellValueFactory(new PropertyValueFactory<Patient, BloodGroup>("bloodGroup"));
            pTable_Allergies.setCellValueFactory(new PropertyValueFactory<Patient, String>("allergies"));
        }

        userV_PatientTable.setItems(observableList);
    }

    @FXML private Tab medical_tab;
    @FXML private TableView<MedicalOfficer> medicalOfficer_mainTable;
    @FXML private TableColumn<MedicalOfficer, Integer> Mtable_staffID;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_staffName;
    @FXML private TableColumn<MedicalOfficer, Gender> Mtable_staffGender;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_staffMarital;
    @FXML private TableColumn<MedicalOfficer, LocalDate> Mtable_staffDOB;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_staffPhone;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_staffNIC;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_address;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_userName;
    @FXML private TableColumn<MedicalOfficer, String> Mtable_email;

    public void setMedicalOfficerTable(ArrayList<MedicalOfficer> medicalOfficer){

        ObservableList<MedicalOfficer> medicalOfficerObservableList =FXCollections.observableList(medicalOfficer);

        if (!isMedicalTSet){
            Mtable_staffID.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, Integer>("staffID"));
            Mtable_staffName.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("name"));
            Mtable_staffGender.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, Gender>("gender"));
            Mtable_staffMarital.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("maritalStatus"));
            Mtable_staffDOB.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, LocalDate>("dob"));
            Mtable_staffPhone.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("phoneNumber"));
            Mtable_staffNIC.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("idCardNumber"));
            Mtable_address.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("address"));
            Mtable_userName.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("userName"));
            Mtable_email.setCellValueFactory(new PropertyValueFactory<MedicalOfficer, String>("staffEmailAddress"));

        }
        medicalOfficer_mainTable.setItems(medicalOfficerObservableList);
    }


    @FXML private Tab admin_tab;
    @FXML private TableView<Admin> admin_mainTable;
    @FXML private TableColumn<Admin, String> Atable_name;
    @FXML private TableColumn<Admin, Gender> A_tableGender;
    @FXML private TableColumn<Admin, String> Atable_marital;
    @FXML private TableColumn<Admin, LocalDate> Atable_DOB;
    @FXML private TableColumn<Admin, String> Atable_phone;
    @FXML private TableColumn<Admin, String> Atable_NIC;
    @FXML private TableColumn<Admin, String> Atable_address;
    @FXML private TableColumn<Admin, String> Atable_userName;

    public void setAdminTable(ArrayList<Admin> adminArrayList){

        ObservableList<Admin> observableList = FXCollections.observableList(adminArrayList);
        if (!isAdminTset){
            Atable_name.setCellValueFactory(new PropertyValueFactory<Admin, String>("name"));
            A_tableGender.setCellValueFactory(new PropertyValueFactory<Admin, Gender>("gender"));
            Atable_marital.setCellValueFactory(new PropertyValueFactory<Admin, String>("maritalStatus"));
            Atable_DOB.setCellValueFactory(new PropertyValueFactory<Admin, LocalDate>("dob"));
            Atable_phone.setCellValueFactory(new PropertyValueFactory<Admin, String>("phoneNumber"));
            Atable_NIC.setCellValueFactory(new PropertyValueFactory<Admin, String>("idCardNumber"));
            Atable_address.setCellValueFactory(new PropertyValueFactory<Admin, String>("address"));
            Atable_userName.setCellValueFactory(new PropertyValueFactory<Admin, String>("userName"));

        }

        admin_mainTable.setItems(observableList);
    }


    @FXML private Tab reception_Tab;
    @FXML private TableView<Receptionist> receptiontable_mainTable;
    @FXML private TableColumn<Receptionist, Integer> Rtable_staffID;
    @FXML private TableColumn<Receptionist, String> Rtable_name;
    @FXML private TableColumn<Receptionist, Gender> Rtable_gender;
    @FXML private TableColumn<Receptionist, String> Rtable_marital;
    @FXML private TableColumn<Receptionist, LocalDate> Rtable_dob;
    @FXML private TableColumn<Receptionist, String> Rtable_phone;
    @FXML private TableColumn<Receptionist, String> Rtable_nic;
    @FXML private TableColumn<Receptionist, String> Rtable_address;
    @FXML private TableColumn<Receptionist, String> Rtable_userName;
    @FXML private TableColumn<Receptionist, String> Rtable_email;

    public void setReceptionTable(ArrayList<Receptionist> receptionistArrayList){

        ObservableList<Receptionist> receptionistObservableList = FXCollections.observableList(receptionistArrayList);

        if (!isReceptionTset){
            Rtable_staffID.setCellValueFactory(new PropertyValueFactory<Receptionist, Integer>("staffID"));
            Rtable_name.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("name"));
            Rtable_gender.setCellValueFactory(new PropertyValueFactory<Receptionist, Gender>("gender"));
            Rtable_marital.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("maritalStatus"));
            Rtable_dob.setCellValueFactory(new PropertyValueFactory<Receptionist, LocalDate>("dob"));
            Rtable_phone.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("phoneNumber"));
            Rtable_nic.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("idCardNumber"));
            Rtable_address.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("address"));
            Rtable_userName.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("userName"));
            Rtable_email.setCellValueFactory(new PropertyValueFactory<Receptionist, String>("staffEmailAddress"));
        }

        receptiontable_mainTable.setItems(receptionistObservableList);

    }

}
