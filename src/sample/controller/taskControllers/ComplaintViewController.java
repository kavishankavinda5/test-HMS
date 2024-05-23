package sample.controller.taskControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.StringLengthValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.Main;
import sample.controller.actionTask.ComplaintAction;
import sample.controller.actionTask.ReferenceAction;
import sample.model.Complaint;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ComplaintViewController {

    Complaint currentComplaint;
    Boolean isTableSet =false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane vedComplaintView;

    @FXML
    private JFXComboBox<String> Cview_complaintDropDown;

    @FXML
    private JFXTextArea Cview_note;

    @FXML
    private JFXTextField Cview_complaitBy;

    @FXML
    private JFXTextField Cview_phoneNumber;

    @FXML
    private TextField Cview_idSearchFiled;

    @FXML
    private JFXTextField Cview_description;

    @FXML
    private JFXTextArea Cview_actionTaken;

    @FXML
    private Label Cview_DateCurrent;

    @FXML
    private JFXButton Cview_searchButton;

    @FXML
    private JFXButton Cview_uploadFile;

    @FXML
    private JFXTextArea Cview_displayArea;

    @FXML
    private Label Cview_complaintID;

    @FXML
    private JFXButton Cview_addBtn;

    @FXML
    private JFXButton Cview_updateBtn;

    @FXML
    private JFXButton Cview_deleteBtn;

    @FXML
    private JFXButton Cview_viewAllBtn;

    @FXML
    private JFXButton Cview_resetBtn;

    @FXML
    private TableView<Complaint> Cview_mainTable;

    @FXML
    private TableColumn<Complaint, Integer> Cview_Tid;

    @FXML
    private TableColumn<Complaint, LocalDate> Cview_Tdate;

    @FXML
    private TableColumn<Complaint, String> Cview_Tcomplainttype;

    @FXML
    private TableColumn<Complaint, String> Cview_complaintBy;

    @FXML
    private TableColumn<Complaint, String> Cview_TphoneNumber;

    @FXML
    private TableColumn<Complaint, String> Cview_Tdescription;

    @FXML
    private TableColumn<Complaint, String> Cview_Tnote;

    @FXML
    private TableColumn<Complaint, String> Cview_Tattachment;

    @FXML
    private TableColumn<Complaint, String> Cview_TactionTaken;

    @FXML
    private Label CView_UploadFile_Path;

    @FXML
    private Label noDocument;

    @FXML
    private JFXButton openDocument;

    @FXML private Window primaryStage;


    @FXML
    void initialize() {

        //Validate input Data
        validateInitialize();

        //Reset all Data
        resetDisplay();

        currentComplaint =new Complaint();
        Cview_DateCurrent.setText(Main.getStringfromLocalDate(LocalDate.now()));
        Cview_complaintDropDown.getItems().addAll(ReferenceAction.getComplaintStringArray());

        Cview_addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if(checkInputs()) {

                    if(validateInputs()){
                        if (CView_UploadFile_Path.getText() == null){
                            Object[] options = { "OK", "CANCEL" };
                            Toolkit.getDefaultToolkit().beep();
                            int selectedValue = JOptionPane.showOptionDialog(null,
                                    "Do You Want to Add This Record With Out Document", "Warning",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);

                            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                                ComplaintAction.addComplaintRecord(getInitialComplaintData());
                                resetDisplay();
                            }
                        }else {
                            ComplaintAction.addComplaintRecord(getInitialComplaintData());
                            resetDisplay();
                        }
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        });

        Cview_updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (Cview_complaintID.getText()==null){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Complaint ID and Search"+"\nor\n"+"Please Select a Complaint from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if(checkInputs()){

                    if(validateInputs()){
                        ComplaintAction.updateComplaintRecord(getComplaintDetails());
                        resetDisplay();
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        Cview_deleteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            /*    if ((Cview_idSearchFiled.getText().length() <= 0) && (Cview_mainTable.getSelectionModel().getSelectedIndex() < 0)){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Complaint ID and Search"+"\nor\n"+"Please Select a Complaint from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                } */
                if (Cview_complaintID.getText()==null){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Complaint ID and Search"+"\nor\n"+"Please Select a Complaint from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    ComplaintAction.deleteComplaintRecord(getComplaintDetails());
                    resetDisplay();
                }

            }
        });

        Cview_searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (Cview_idSearchFiled.getText().length() <= 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Complaint ID is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    String searchTerm =Cview_idSearchFiled.getText();
                    Complaint foundComplaint = ComplaintAction.searchComplaintRecord(searchTerm,searchTerm);
                    setComplaintViewData(foundComplaint);
                    displayComplaintDetails(foundComplaint);
                    viewFile(foundComplaint);

                }
            }
        });

        Cview_uploadFile.setOnAction(new EventHandler<ActionEvent>() {
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
                    CView_UploadFile_Path.setText(file.getAbsolutePath());
                    noDocument.setVisible(false);
                    openDocument.setVisible(true);
                }
                else {
                    noDocument.setVisible(true);
                    openDocument.setVisible(false);
                    System.out.println("No Select Document");
                }

            }
        });

        openDocument.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (CView_UploadFile_Path.getText()!=null){
                        Desktop.getDesktop().open(new File(CView_UploadFile_Path.getText()));
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

        Cview_viewAllBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ArrayList<Complaint> allComplaints = ComplaintAction.getAllComplaintRecords();
                setMainTable(allComplaints);
            }
        });

        Cview_resetBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                resetDisplay();
            }
        });

        Cview_mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Complaint selectedComplaint =Cview_mainTable.getSelectionModel().getSelectedItem();
                displayComplaintDetails(selectedComplaint);
                setComplaintViewData(selectedComplaint);
                viewFile(selectedComplaint);

            }
        });

    }

    public Complaint getInitialComplaintData(){
        Complaint newComplaint = new Complaint();
        newComplaint.setComplaintID(Main.getComplaintID());
        newComplaint.setComplaintDate(Main.getLocalDatefromString(Cview_DateCurrent.getText()));
        newComplaint.setComplaintType(Cview_complaintDropDown.getValue());
        newComplaint.setComplaintBy(Cview_complaitBy.getText());
        newComplaint.setPhoneNumber(Cview_phoneNumber.getText());
        newComplaint.setDescription(Cview_description.getText());
        newComplaint.setNote(Cview_note.getText());
        newComplaint.setFilePath(CView_UploadFile_Path.getText());
        newComplaint.setActiontaken("PENDING");

        return newComplaint;
    }

    public Complaint getComplaintDetails(){
        Complaint complaint = new Complaint();
        complaint.setComplaintID(Integer.parseInt(Cview_complaintID.getText()));
        complaint.setComplaintDate(Main.getLocalDatefromString(Cview_DateCurrent.getText()));
        complaint.setComplaintType(Cview_complaintDropDown.getValue());
        complaint.setPhoneNumber(Cview_phoneNumber.getText());
        complaint.setDescription(Cview_description.getText());
        complaint.setNote(Cview_note.getText());
        complaint.setActiontaken(Cview_actionTaken.getText());
        complaint.setFilePath(CView_UploadFile_Path.getText());

        return complaint;
    }

    public void resetDisplay(){
        Cview_complaintDropDown.setValue(null);
        Cview_actionTaken.setText(null);
        Cview_complaintID.setText(null);
        Cview_idSearchFiled.clear();
        Cview_description.setText(null);
        Cview_displayArea.setText(null);
        Cview_phoneNumber.setText(null);
        Cview_complaitBy.setText(null);
        Cview_actionTaken.setText(null);
        Cview_note.setText(null);
        CView_UploadFile_Path.setText(null);
        noDocument.setVisible(false);
        openDocument.setVisible(false);
    }

    public void setComplaintViewData(Complaint complaintRecord){
        Cview_complaintID.setText(String.valueOf(complaintRecord.getComplaintID()));
        Cview_DateCurrent.setText(Main.getStringfromLocalDate(complaintRecord.getComplaintDate()));
        Cview_complaitBy.setText(complaintRecord.getComplaintBy());
        Cview_complaintDropDown.setValue(complaintRecord.getComplaintType());
        Cview_phoneNumber.setText(complaintRecord.getPhoneNumber());
        Cview_description.setText(complaintRecord.getDescription());
        Cview_note.setText(complaintRecord.getNote());
        Cview_actionTaken.setText(complaintRecord.getActiontaken());
    }

    public void displayComplaintDetails(Complaint complaint){
        Cview_displayArea.setText(
                "\n-----Complaint Data-----\n"+
                        "Complaint ID : "+complaint.getComplaintID()+"\n"+
                        "Complaint Type : "+complaint.getComplaintType()+"\n"+
                        "Complaint By : "+complaint.getComplaintBy()+"\n"+
                        "Contact Number : "+complaint.getPhoneNumber()+"\n"+
                        "Description : "+complaint.getDescription()+"\n"+
                        "Note : "+complaint.getNote()+"\n"+
                        "Action Taken : "+complaint.getActiontaken()
        );
    }

    public void setMainTable(ArrayList<Complaint> complaints){

        ObservableList<Complaint> observableList = FXCollections.observableList(complaints);

        if (!isTableSet){
            Cview_Tid.setCellValueFactory(new PropertyValueFactory<Complaint,Integer>("complaintID"));
            Cview_Tdate.setCellValueFactory(new PropertyValueFactory<Complaint,LocalDate>("complaintDate"));
            Cview_Tcomplainttype.setCellValueFactory(new PropertyValueFactory<Complaint,String>("complaintType"));
            Cview_complaintBy.setCellValueFactory(new PropertyValueFactory<Complaint,String>("complaintBy"));
            Cview_TphoneNumber.setCellValueFactory(new PropertyValueFactory<Complaint,String>("phoneNumber"));
            Cview_Tdescription.setCellValueFactory(new PropertyValueFactory<Complaint,String>("description"));
            Cview_Tnote.setCellValueFactory(new PropertyValueFactory<Complaint,String>("note"));
            Cview_TactionTaken.setCellValueFactory(new PropertyValueFactory<Complaint,String>("actiontaken"));

        }

        Cview_mainTable.setItems(observableList);
    }

    public String path;

    public void viewFile(Complaint complaint) {

        try {

            int referenceNo = complaint.getComplaintID();

            String moFilePath ="src/sample/fileStorage/moduleData/userData/complaintData";
            String fileSavePath = moFilePath + "\\" + referenceNo + ".pdf";

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

        //Check Input Field Of is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");
        regexValidator.setMessage("Only Text");

        Cview_complaitBy.getValidators().add(regexValidator);
        Cview_complaitBy.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_complaitBy.validate();
        });
        Cview_description.getValidators().add(regexValidator);
        Cview_description.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_description.validate();
        });
        Cview_actionTaken.getValidators().add(regexValidator);
        Cview_actionTaken.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_actionTaken.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        Cview_phoneNumber.getValidators().add(regexValidator_phone_number);
        Cview_phoneNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_phoneNumber.validate();
        });
    }

    public Boolean validateInputs(){

        boolean dataInputs = false;

        //Check Input Field Of is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");
        regexValidator.setMessage("Only Text");

        Cview_complaitBy.getValidators().add(regexValidator);
        Cview_complaitBy.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_complaitBy.validate();
        });
        Cview_description.getValidators().add(regexValidator);
        Cview_description.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_description.validate();
        });
      /*  Cview_actionTaken.getValidators().add(regexValidator);
        Cview_actionTaken.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_actionTaken.validate();
        });*/

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        Cview_phoneNumber.getValidators().add(regexValidator_phone_number);
        Cview_phoneNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Cview_phoneNumber.validate();
        });

        if (Cview_complaitBy.validate() && Cview_phoneNumber.validate() && Cview_description.validate() ){
            dataInputs = true;
        }
        return dataInputs;
    }

    public Boolean checkInputs(){

        boolean allCheck =false;

        if (Cview_complaintDropDown.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Complaint Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Cview_complaitBy.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Complainer is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Cview_phoneNumber.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Phone Number is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Cview_description.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Description is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Cview_note.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Note is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
      /*  else if (Cview_actionTaken.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Action Taken is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }*/
        else{
            allCheck = true;
        }
        return allCheck;
    }


}
