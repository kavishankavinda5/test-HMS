package sample.controller.taskControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.StringLengthValidator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.Main;
import sample.controller.actionTask.ProfileAction;
import sample.controller.actionTask.ReferenceAction;
import sample.controller.actionTask.UserAction;
import sample.model.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javax.swing.*;

public class ProfileViewController {


    @FXML
    private ResourceBundle resources;

    @FXML private URL location;
    @FXML private Label profile_userNameLabel;
    @FXML private JFXButton profile_changePicButton;
    @FXML private JFXButton profile_UploadDocument;
    @FXML private GridPane profileView_userdataGrid;
    @FXML private JFXTextField profile_name;
    @FXML private JFXTextField profile_nicNumber;
    @FXML private JFXTextField profile_phoneNumber;
    @FXML private JFXComboBox<Gender> profile_genderDrop;
    @FXML private JFXTextArea profile_address;
    @FXML private DatePicker profile_dobDatePicker;
    @FXML private JFXTextField profile_userName;
    @FXML private JFXPasswordField profile_userPassword;
    @FXML private JFXButton profile_editProfileButton;
    @FXML private JFXButton profile_saveProfileButton;
    @FXML private GridPane profileView_patientExtraGrid;
    @FXML private JFXComboBox<BloodGroup> profile_bloodGropDrop;
    @FXML private JFXTextField profile_allergies;
    @FXML private GridPane profileView_empExtraGrid;
    @FXML private Label profile_staffID;
    @FXML private DatePicker profile_dateOfJoin;
    @FXML private JFXTextField profile_emailAddress;
    @FXML private GridPane profileView_doctorExtraGrid;
    @FXML private JFXComboBox<String> profile_specialityDrop;
    @FXML private JFXComboBox<String> profile_maritalDrop;
    @FXML private HBox profile_userPhoto;
    @FXML private JFXButton profile_OpenDocument;
    @FXML private JFXButton profile_resetProfileButton;
    @FXML private Label profile_UploadPhoto_Path;
    @FXML private Label profile_UploadFile_Path;
    @FXML private Window primaryStage;

    @FXML void initialize() {

        //Validate User Inputs
        validateInitialize();

        profile_maritalDrop.getItems().addAll(ReferenceAction.getMaritalStatus());
        profile_genderDrop.getItems().addAll(ReferenceAction.getGender());
        profile_specialityDrop.getItems().addAll(ReferenceAction.getDocSpecialityStringArray());
        profile_bloodGropDrop.getItems().addAll(ReferenceAction.getBloogGroup());
       // System.out.println("initialize() in profileViewController currentUser"+ Main.getCurrentSystemUser());
        setUserProfileData(Main.getCurrentSystemUser());


        profile_editProfileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switch (Main.getCurrentSystemUser().getUserRoll()){
                    case ADMIN :
                       profileView_userdataGrid.setDisable(false);
                        profile_UploadDocument.setVisible(true);
                        profile_changePicButton.setVisible(true);
                        profile_saveProfileButton.setDisable(false);
                        profile_resetProfileButton.setDisable(false);
                       break;
                    case RECEPTIONIST:
                        profileView_userdataGrid.setDisable(false);
                        profileView_empExtraGrid.setDisable(false);
                        profile_UploadDocument.setVisible(true);
                        profile_changePicButton.setVisible(true);
                        profile_saveProfileButton.setDisable(false);
                        profile_resetProfileButton.setDisable(false);
                        break;
                    case PATIENT:
                        profileView_userdataGrid.setDisable(false);
                        profileView_patientExtraGrid.setDisable(false);
                        profile_UploadDocument.setVisible(true);
                        profile_changePicButton.setVisible(true);
                        profile_saveProfileButton.setDisable(false);
                        profile_resetProfileButton.setDisable(false);
                        break;
                    case MEDICALOFFICER:
                        profileView_userdataGrid.setDisable(false);
                        profileView_empExtraGrid.setDisable(false);
                        profileView_doctorExtraGrid.setDisable(false);
                        profile_UploadDocument.setVisible(true);
                        profile_changePicButton.setVisible(true);
                        profile_saveProfileButton.setDisable(false);
                        profile_resetProfileButton.setDisable(false);
                        break;
                    default:
                        break;
                }
            }
        });

        profile_saveProfileButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if(checkInputs()) {

                    switch (Main.getCurrentSystemUser().getUserRoll()) {

                        case ADMIN:
                            if (validateInputs()) {
                                ProfileAction.updateCurrentUserData(getAdminDataFromView());
                            }
                            else {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            break;

                        case RECEPTIONIST:
                            if (profile_emailAddress.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Staff Email is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (profile_dateOfJoin.getValue()==null){
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Join Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Email Address
                                RegexValidator regexValidator_email = new RegexValidator();
                                regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                                profile_emailAddress.getValidators().add(regexValidator_email);
                                profile_emailAddress.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  profile_emailAddress.validate();
                                });

                                if (validateInputs() && profile_emailAddress.validate() ) {
                                    ProfileAction.updateCurrentUserData(getReceptionistFromView());
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;

                        case PATIENT:
                            if (profile_bloodGropDrop.getSelectionModel().getSelectedIndex() < 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Blood Group is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (profile_allergies.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Allergies is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Allergy is text
                                RegexValidator regexValidator = new RegexValidator();
                                regexValidator.setRegexPattern("[A-Za-z\\s]+");
                                profile_allergies.getValidators().add(regexValidator);
                                profile_allergies.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  profile_allergies.validate();
                                });

                                if (validateInputs() && profile_allergies.validate()) {
                                    ProfileAction.updateCurrentUserData(getPatientDataFromView());
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;
                        case MEDICALOFFICER:
                            if (profile_emailAddress.getText().length() <= 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Staff Email is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (profile_dateOfJoin.getValue()==null){
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Join Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else if (profile_specialityDrop.getSelectionModel().getSelectedIndex() < 0) {
                                Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Speciality is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                            else {

                                //Check Input Field Of Email Address
                                RegexValidator regexValidator_email = new RegexValidator();
                                regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
                                profile_emailAddress.getValidators().add(regexValidator_email);
                                profile_emailAddress.focusedProperty().addListener((o, oldValue, newValue) -> {
                                    if(!newValue)  profile_emailAddress.validate();
                                });

                                if (validateInputs() && profile_emailAddress.validate()) {
                                    ProfileAction.updateCurrentUserData(getMedicalOffFromView());
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                    JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;

                        default:
                            break;
                    }

                }
            }
        });

        profile_OpenDocument.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                //setUserDocument(Main.getCurrentSystemUser());
                //setUserProfileData(Main.getCurrentSystemUser());
                try {
                    if (Files.isReadable(Path.of(path))){
                        System.out.println("Preview Document");
                        Desktop.getDesktop().open(new File(path));
                    }else {
                        System.out.println("No Document");
                        JOptionPane.showMessageDialog(null,"NO Document");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        profile_changePicButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FileChooser fileChooser = new FileChooser();
                // Set title for FileChooser
                fileChooser.setTitle("Select Your Picture");
                // Add Extension Filters
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));

                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null)
                {
                    profile_UploadPhoto_Path.setText(file.getAbsolutePath());
                    profile_userPhoto.getChildren().clear();

                    try {

                        if (Files.isReadable(Path.of(file.getAbsolutePath()))){
                            FileInputStream imageStream = new FileInputStream(file.getAbsoluteFile());
                            javafx.scene.image.Image image = new Image(imageStream);
                            ImageView view = new ImageView();
                            view.setImage(image);
                            view.setFitWidth(200);
                            view.setFitHeight(200);
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
            }

        });

        profile_UploadDocument.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FileChooser fileChooser = new FileChooser();
                // Set title for FileChooser
                fileChooser.setTitle("Select File");
                // Add Extension Filters
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PDF", "*.pdf"));


                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null)
                {
                    profile_UploadFile_Path.setText(file.getAbsolutePath());
                }
                else {
                    System.out.println("No Select Document");
                }

            }
        });

        profile_resetProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                profile_userPhoto.getChildren().clear();
                profile_UploadFile_Path.setText(null);
                profile_UploadPhoto_Path.setText(null);
                setUserProfileData(Main.getCurrentSystemUser());
            }
        });

    }

    public void setInitialView(){
        profileView_userdataGrid.setDisable(true);
        profileView_patientExtraGrid.setDisable(true);
        profileView_doctorExtraGrid.setDisable(true);
    }

    public void setUserProfileData(SystemUser systemUser){

        switch (systemUser.getUserRoll()){
            case ADMIN :
                setViewForAdmin();
                setAdminDataToView(systemUser.getAdmin());
                viewAdminPhoto_File(systemUser.getAdmin());
                break;
            case RECEPTIONIST:
                setViewForReception();
                setReceptionDataToView(systemUser.getReceptionist());
                viewReceptionPhoto_File(systemUser.getReceptionist());
                break;
            case PATIENT:
                System.out.println("setUser for patient called");
                setViewForPatient();
                setPatientDataToView(systemUser.getPatient());
                viewPatientPhoto_File(systemUser.getPatient());
                break;
            case MEDICALOFFICER:
                setViewForMedicalOfficer();
                setMedicalDataToView(systemUser.getMedicalOfficer());
                viewMedicalOfficerPhoto(systemUser.getMedicalOfficer());
                break;
            default:
                break;

        }
    }

    public String path;


    //write a method for view Admin Data
    private void setAdminDataToView(Admin admin){
        profile_name.setText(admin.getName());
        profile_nicNumber.setText(admin.getIdCardNumber());
        profile_phoneNumber.setText(admin.getPhoneNumber());
        profile_address.setText(admin.getAddress());
        profile_maritalDrop.setValue(admin.getMaritalStatus());
        profile_genderDrop.setValue(admin.getGender());
        profile_dobDatePicker.setValue(admin.getDob());
        profile_userName.setText(UserAction.decryptUserData(admin.getUserName()));
        profile_userPassword.setText(UserAction.decryptUserData(admin.getUserPassword()));
    }

    //write a method for view Admin photo
    private void viewAdminPhoto_File(Admin admin) {

        try {
            String staffId = admin.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/admin";
            String photoSavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photoSavePath))){
                FileInputStream imageStream = new FileInputStream(photoSavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(200);
                view.setFitHeight(200);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/admin";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            path=fileSavePath;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //write a method for view Patient Data
    public void setPatientDataToView(Patient passPatient){
        System.out.println( "setViewForPatient();-----> method called");
        profile_name.setText(passPatient.getName());
        profile_nicNumber.setText(passPatient.getIdCardNumber());
        profile_phoneNumber.setText(passPatient.getPhoneNumber());
        profile_address.setText(passPatient.getAddress());
        profile_maritalDrop.setValue(passPatient.getMaritalStatus());
        profile_dobDatePicker.setValue(passPatient.getDob());
        profile_userName.setText(UserAction.decryptUserData(passPatient.getUserName()));
        profile_userPassword.setText(UserAction.decryptUserData(passPatient.getUserPassword()));
        profile_bloodGropDrop.setValue(passPatient.getBloodGroup());
        profile_allergies.setText(passPatient.getAllergies());
    }

    //write a method for view Patient photo
    private void viewPatientPhoto_File(Patient patient) {

        try {

            String staffId = patient.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/patient";
            String photoSavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photoSavePath))){
                FileInputStream imageStream = new FileInputStream(photoSavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(200);
                view.setFitHeight(200);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/patient";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            path=fileSavePath;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //write a method for view Reception Data
    public void setReceptionDataToView(Receptionist currentReceptionist){
        profile_name.setText(currentReceptionist.getName());
        profile_nicNumber.setText(currentReceptionist.getIdCardNumber());
        profile_phoneNumber.setText(currentReceptionist.getPhoneNumber());
        profile_address.setText(currentReceptionist.getAddress());
        profile_dobDatePicker.setValue(currentReceptionist.getDob());
        profile_maritalDrop.setValue(currentReceptionist.getMaritalStatus());
        profile_userName.setText(UserAction.decryptUserData(currentReceptionist.getUserName()));
        profile_userPassword.setText(UserAction.decryptUserData(currentReceptionist.getUserPassword()));
        profile_staffID.setText(String.valueOf(currentReceptionist.getStaffID()));
        profile_emailAddress.setText(currentReceptionist.getStaffEmailAddress());
        profile_dateOfJoin.setValue(currentReceptionist.getDateOfJoining());
    }

    //write a method for view Reception photo
    private void viewReceptionPhoto_File(Receptionist receptionist) {

        try {

            String staffId = receptionist.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/reception";
            String photosavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photosavePath))){
                FileInputStream imageStream = new FileInputStream(photosavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(200);
                view.setFitHeight(200);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/reception";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            path=fileSavePath;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //write a method for view MedicalOfficer Data
    public void setMedicalDataToView(MedicalOfficer medicalOfficerData){
        profile_name.setText(medicalOfficerData.getName());
        profile_nicNumber.setText(medicalOfficerData.getIdCardNumber());
        profile_phoneNumber.setText(medicalOfficerData.getPhoneNumber());
        profile_address.setText(medicalOfficerData.getAddress());
        profile_maritalDrop.setValue(medicalOfficerData.getMaritalStatus());
        profile_dobDatePicker.setValue(medicalOfficerData.getDob());
        profile_userName.setText(UserAction.decryptUserData(medicalOfficerData.getUserName()));
        profile_userPassword.setText(UserAction.decryptUserData(medicalOfficerData.getUserPassword()));
        profile_staffID.setText(String.valueOf(medicalOfficerData.getStaffID()));
        profile_emailAddress.setText(medicalOfficerData.getStaffEmailAddress());
        profile_dateOfJoin.setValue(medicalOfficerData.getDateOfJoining());
        profile_specialityDrop.setValue(medicalOfficerData.getSpeciality());
    }

    //write a method for view MedicalOfficer photo
    private void viewMedicalOfficerPhoto(MedicalOfficer medicalOfficer) {

        try {

            String staffId = medicalOfficer.getIdCardNumber();

            String photoPath = "src/sample/fileStorage/moduleData/userData/userPhoto/medicalOfficer";
            String photoSavePath = photoPath + "\\" + staffId + ".jpg";

            if (Files.isReadable(Path.of(photoSavePath))){
                FileInputStream imageStream = new FileInputStream(photoSavePath);
                Image image = new Image(imageStream);
                ImageView view = new ImageView();
                view.setImage(image);
                view.setFitWidth(200);
                view.setFitHeight(200);
                view.setSmooth(true);
                //view.setPreserveRatio(true);
                profile_userPhoto.getChildren().add(view);
                System.out.println("Preview PHOTO");
            }else {
                System.out.println("No Profile Photo");
            }

            String FilePath ="src/sample/fileStorage/moduleData/userData/userFile/medicalOfficer";
            String fileSavePath = FilePath + "\\" + staffId + ".pdf";

            path=fileSavePath;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setViewForAdmin() {
        profileView_patientExtraGrid.setVisible(false);
        profileView_doctorExtraGrid.setVisible(false);
        profileView_empExtraGrid.setVisible(false);
        profileView_userdataGrid.setDisable(true);
        profile_UploadDocument.setVisible(false);
        profile_changePicButton.setVisible(false);
        profile_saveProfileButton.setDisable(true);
        profile_resetProfileButton.setDisable(true);
    }

    private void setViewForReception() {
        profileView_patientExtraGrid.setVisible(false);
        profileView_empExtraGrid.setDisable(true);
        profileView_doctorExtraGrid.setVisible(false);
        profileView_userdataGrid.setDisable(true);
        profile_UploadDocument.setVisible(false);
        profile_changePicButton.setVisible(false);
        profile_saveProfileButton.setDisable(true);
        profile_resetProfileButton.setDisable(true);
    }

    private void setViewForPatient(){
        profileView_patientExtraGrid.setVisible(true);
        profileView_patientExtraGrid.setDisable(true);
        profileView_empExtraGrid.setVisible(false);
        profileView_doctorExtraGrid.setVisible(false);
        profileView_userdataGrid.setDisable(true);
        profileView_userdataGrid.setVisible(true);
        profile_UploadDocument.setVisible(false);
        profile_changePicButton.setVisible(false);
        profile_saveProfileButton.setDisable(true);
        profile_resetProfileButton.setDisable(true);
    }

    private void setViewForMedicalOfficer(){
        profileView_patientExtraGrid.setVisible(false);
        profileView_empExtraGrid.setDisable(true);
        profileView_doctorExtraGrid.setDisable(true);
        profileView_userdataGrid.setDisable(true);
        profile_UploadDocument.setVisible(false);
        profile_changePicButton.setVisible(false);
        profile_saveProfileButton.setDisable(true);
        profile_resetProfileButton.setDisable(true);
    }


    private SystemUser getPatientDataFromView(){
        SystemUser systemUser = new SystemUser();
        systemUser.setUserRoll(UserRoll.PATIENT);
        Patient patient=new Patient();
        patient.setUserRoll(Main.getCurrentSystemUser().getUserRoll());
        patient.setName(profile_name.getText());
        patient.setIdCardNumber(profile_nicNumber.getText());
        patient.setAddress(profile_address.getText());
        patient.setPhoneNumber(profile_phoneNumber.getText());
        patient.setMaritalStatus(profile_maritalDrop.getValue());
        patient.setUserName(UserAction.encryptUserData(profile_userName.getText()));
        patient.setUserPassword(UserAction.encryptUserData(profile_userPassword.getText()));
        patient.setBloodGroup(profile_bloodGropDrop.getValue());
        patient.setAllergies(profile_allergies.getText());
        patient.setPhotoPath(profile_UploadPhoto_Path.getText());
        patient.setFilePath(profile_UploadFile_Path.getText());

        systemUser.setPatient(patient);

        return systemUser;
    }

    private SystemUser getAdminDataFromView(){
        SystemUser adminUser =new SystemUser();
        Admin admin =new Admin();

        adminUser.setUserRoll(UserRoll.ADMIN);
        admin.setUserRoll(Main.getCurrentSystemUser().getUserRoll());
        admin.setGender(profile_genderDrop.getValue());
        admin.setDob(profile_dobDatePicker.getValue());
        admin.setName(profile_name.getText());
        admin.setIdCardNumber(profile_nicNumber.getText());
        admin.setPhoneNumber(profile_phoneNumber.getText());
        admin.setAddress(profile_address.getText());
        admin.setMaritalStatus(profile_maritalDrop.getValue());
        admin.setUserName(UserAction.encryptUserData(profile_userName.getText()));
        admin.setUserPassword(UserAction.encryptUserData(profile_userPassword.getText()));
        admin.setPhotoPath(profile_UploadPhoto_Path.getText());
        admin.setFilePath(profile_UploadFile_Path.getText());

        adminUser.setAdmin(admin);
        return adminUser;
    }

    private SystemUser getReceptionistFromView(){
        SystemUser systemUser=new SystemUser();
        Receptionist receptionist=new Receptionist();

        systemUser.setUserRoll(UserRoll.RECEPTIONIST);
        receptionist.setUserRoll(Main.getCurrentSystemUser().getUserRoll());
        receptionist.setGender(profile_genderDrop.getValue());
        receptionist.setDob(profile_dobDatePicker.getValue());
        receptionist.setName(profile_name.getText());
        receptionist.setIdCardNumber(profile_nicNumber.getText());
        receptionist.setAddress(profile_address.getText());
        receptionist.setPhoneNumber(profile_phoneNumber.getText());
        receptionist.setMaritalStatus(profile_maritalDrop.getValue());
        receptionist.setUserName(UserAction.encryptUserData(profile_userName.getText()));
        receptionist.setUserPassword(UserAction.encryptUserData(profile_userPassword.getText()));
        receptionist.setStaffID(Integer.parseInt(profile_staffID.getText()));
        receptionist.setStaffEmailAddress(profile_emailAddress.getText());
        receptionist.setDateOfJoining(profile_dateOfJoin.getValue());
        receptionist.setPhotoPath(profile_UploadPhoto_Path.getText());
        receptionist.setFilePath(profile_UploadFile_Path.getText());

        systemUser.setReceptionist(receptionist);

        return systemUser;

    }

    public SystemUser getMedicalOffFromView(){
        SystemUser systemUser =new SystemUser();
        MedicalOfficer medicalOfficer = new MedicalOfficer();

        systemUser.setUserRoll(UserRoll.MEDICALOFFICER);
        medicalOfficer.setUserRoll(Main.getCurrentSystemUser().getUserRoll());
        medicalOfficer.setName(profile_name.getText());
        medicalOfficer.setDob(profile_dobDatePicker.getValue());
        medicalOfficer.setGender(profile_genderDrop.getValue());
        medicalOfficer.setIdCardNumber(profile_nicNumber.getText());
        medicalOfficer.setAddress(profile_address.getText());
        medicalOfficer.setPhoneNumber(profile_phoneNumber.getText());
        medicalOfficer.setMaritalStatus(profile_maritalDrop.getValue());
        medicalOfficer.setUserName(UserAction.encryptUserData(profile_userName.getText()));
        medicalOfficer.setUserPassword(UserAction.encryptUserData(profile_userPassword.getText()));
        medicalOfficer.setStaffID(Integer.parseInt(profile_staffID.getText()));
        medicalOfficer.setStaffEmailAddress(profile_emailAddress.getText());
        medicalOfficer.setDateOfJoining(profile_dateOfJoin.getValue());
        medicalOfficer.setSpeciality(profile_specialityDrop.getValue());
        medicalOfficer.setPhotoPath(profile_UploadPhoto_Path.getText());
        medicalOfficer.setFilePath(profile_UploadFile_Path.getText());

        systemUser.setMedicalOfficer(medicalOfficer);

        return systemUser;
    }


    public void validateInitialize(){

        //Check Input Field Of Name is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");
        regexValidator.setMessage("Only Text");

        profile_name.getValidators().add(regexValidator);
        profile_name.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_name.validate();
        });
        profile_allergies.getValidators().add(regexValidator);
        profile_allergies.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_allergies.validate();
        });

        //Check Input Field Of Email Address
        RegexValidator regexValidator_email = new RegexValidator();
        regexValidator_email.setRegexPattern("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        regexValidator_email.setMessage("Invalid Please Check");

        profile_emailAddress.getValidators().add(regexValidator_email);
        profile_emailAddress.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_emailAddress.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        profile_phoneNumber.getValidators().add(regexValidator_phone_number);
        profile_phoneNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_phoneNumber.validate();
        });

        //Check Input Field Of NIC
        RegexValidator regexValidator_nic = new RegexValidator();
        regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");
        regexValidator_nic.setMessage("Invalid Please Check");

        profile_nicNumber.getValidators().add(regexValidator_nic);
        profile_nicNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_nicNumber.validate();
        });

    }

    public Boolean validateInputs(){

        Boolean dataInputs = false;

        //Check Input Field Of Name is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");

        profile_name.getValidators().add(regexValidator);
        profile_name.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_name.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        profile_phoneNumber.getValidators().add(regexValidator_phone_number);
        profile_phoneNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_phoneNumber.validate();
        });

        //Check Input Field Of NIC
        RegexValidator regexValidator_nic = new RegexValidator();
        regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");
        regexValidator_nic.setMessage("Invalid Please Check");

        profile_nicNumber.getValidators().add(regexValidator_nic);
        profile_nicNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  profile_nicNumber.validate();
        });

        if (profile_name.validate() && profile_phoneNumber.validate() && profile_nicNumber.validate() ){
            dataInputs = true;
        }
        return dataInputs;
    }

    public Boolean checkInputs(){

        Boolean allCheck =false;

        if (profile_name.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_genderDrop.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Gender is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_maritalDrop.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Marital is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_dobDatePicker.getValue()==null){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Date of Birth is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_phoneNumber.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Phone Number is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_nicNumber.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "NIC is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_address.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Address is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_userName.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "User Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (profile_userPassword.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Password is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else{
            allCheck = true;
        }
        return allCheck;
    }


}
