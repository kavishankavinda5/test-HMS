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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Main;
import sample.controller.actionTask.PostalAction;
import sample.controller.actionTask.ReferenceAction;
import sample.controller.actionTask.VisitorAction;
import sample.model.Gender;
import sample.model.Visitor;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VisitorViewController {

    public Boolean isMainTableSet=false;
    private Visitor currentVisitor;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField Vview_idSearch;

    @FXML
    private JFXButton Vview_searchButton;

    @FXML
    private JFXButton Vview_outTimeAddBtn;

    @FXML
    private JFXButton Vview_inTimeAddBtn;

    @FXML
    private JFXTextArea Vview_note;

    @FXML
    private Label Vview_recordID;

    @FXML
    private JFXButton Vview_attchementsBtn;

    @FXML
    private DatePicker Vview_date;

    @FXML
    private JFXTextField Vview_idNumber;

    @FXML
    private JFXTextField Vview_vName;

    @FXML
    private JFXTextField Vview_purpose;

    @FXML
    private JFXTextField Vview_phoneNumber;

    @FXML
    private JFXComboBox<Gender> Vview_gender;

    @FXML
    private Label Vview_inTime;

    @FXML
    private Label Vview_outTime;

    @FXML
    private JFXTextArea Vview_displayArea;

    @FXML
    private JFXButton Vview_addBtn;

    @FXML
    private JFXButton Vview_deleteBtn;

    @FXML
    private JFXButton Vview_updateBtn;

    @FXML
    private JFXButton Vview_viewAllBtn;

    @FXML
    private JFXButton Vview_resetBtn;

    @FXML
    private JFXButton Vview_inVisitorsBtn;


    @FXML
    private TableView<Visitor> Vview_mainTable;

    @FXML
    private TableColumn<Visitor,Integer> Vview_TvisitorID;

    @FXML
    private TableColumn<Visitor,Gender> Vview_TGender;

    @FXML
    private TableColumn<Visitor, String> Vview_TidNumberCol;

    @FXML
    private TableColumn<Visitor, String> Vview_TidNameCol;

    @FXML
    private TableColumn<Visitor, String> Vview_TreasonCol;

    @FXML
    private TableColumn<Visitor, String> Vview_TiphoneCol;

    @FXML
    private TableColumn<Visitor, LocalDate> Vview_TdateCol;

    @FXML
    private TableColumn<Visitor, LocalTime> Vview_TarrivalCol;

    @FXML
    private TableColumn<Visitor, LocalTime> Vview_ToutTimeCol;

    @FXML
    private TableColumn<Visitor, String> Vview_TnotesCol;

    @FXML
    void initialize() {

        //Validate User Inputs
        validateInitialize();

        //Reset Label Data
        Vview_recordID.setText(null);

        currentVisitor =new Visitor();

        Vview_searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (Vview_idSearch.getText().length() <= 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Search ID is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {

                    //Check Input Field Of NIC
                    RegexValidator regexValidator_nic = new RegexValidator();
                    regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");
                    regexValidator_nic.setMessage("Invalid Please Check");

                    Vview_idSearch.getValidators().add(regexValidator_nic);
                    Vview_idSearch.focusedProperty().addListener((o, oldVal,newVal)->{
                        if(!newVal) Vview_idSearch.validate();
                    });

                    if(Vview_idSearch.validate()){

                    ArrayList<Visitor> foundVisitors =VisitorAction.searchVisitorRecords(Vview_idSearch.getText(),null);

                    //System.out.println("search term : "+Vview_idSearch.getText()+" found Records : "+foundVisitors);
                    ObservableList<Visitor> observableList =FXCollections.observableList(foundVisitors);
                    setTable();
                    Vview_mainTable.setItems(observableList);
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });


        Vview_addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if(checkInputs()) {

                    if(validateInputs()){
                        VisitorAction.addVisitorRecord(getInitialVisitorRecord());
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        Vview_updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (Vview_recordID.getText()==null){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Visitor ID and Search"+"\nor\n"+"Please Select a Record from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if(checkInputs()){

                    if(validateInputs()){
                        VisitorAction.updateVisitorRecord(getVisitorRecord());
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }


            }
        });

        Vview_deleteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (Vview_recordID.getText()==null){
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Reference No and Search"+"\nor\n"+"Please Select a Record from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    VisitorAction.deleteVisitorRecord(getVisitorRecord());
                    resetDisplay();
                }

            }
        });

        Vview_resetBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                resetDisplay();
            }
        });

        Vview_inVisitorsBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                resetRecords();
                ArrayList<Visitor> getINVisitorRecords = VisitorAction.getInVisitorRecords();
                ObservableList<Visitor> visitorObservableList =FXCollections.observableList(getINVisitorRecords);
                setTable();
                Vview_mainTable.setItems(visitorObservableList);

            }
        });

        Vview_viewAllBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ArrayList<Visitor> allVisitors= VisitorAction.viewAllVisitorRecords();
                ObservableList<Visitor> allVisitorRecords = FXCollections.observableArrayList(allVisitors);
                setTable();
                Vview_mainTable.setItems(allVisitorRecords);

            }
        });


        Vview_gender.getItems().addAll(ReferenceAction.getGender());

        Vview_inTimeAddBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                LocalTime inTime = LocalTime.now();
                Vview_inTime.setText(Main.getStringFromLocalTime(inTime));


            }
        });

        Vview_outTimeAddBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                LocalTime inTime = LocalTime.now();
                Vview_outTime.setText(Main.getStringFromLocalTime(inTime));

            }
        });


        Vview_mainTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                resetRecords();
                Visitor selectedVisitor = Vview_mainTable.getSelectionModel().getSelectedItem();
                if (selectedVisitor.isVisitorIn()){
                    setCurrentVisitor(selectedVisitor);
                    displayVisitor(selectedVisitor);
                    displayVisitorDetails();
                }

            }
        });


    }

    public Visitor getInitialVisitorRecord(){
        Visitor visitor =new Visitor();

        currentVisitor.setVisitorIn(Vview_inTime.getText()!=null);

        visitor.setVisitorOut(false);
        visitor.setDate(Vview_date.getValue());
        visitor.setVisitorIn(currentVisitor.isVisitorIn());
        visitor.setVisitorID(Main.getVisitorID());
        visitor.setIdNumber(Vview_idNumber.getText());
        visitor.setName(Vview_vName.getText());
        visitor.setPurpose(Vview_purpose.getText());
        visitor.setPhoneNumber(Vview_phoneNumber.getText());
        visitor.setGender(Vview_gender.getValue());
        visitor.setNote(Vview_note.getText());
        visitor.setInTime(Main.getLocalTimeFromString(Vview_inTime.getText()));

        return visitor;
    }

    public Visitor getVisitorRecord(){

        Visitor visitor =new Visitor();
        currentVisitor.setVisitorIn(Vview_inTime.getText() != null);
        currentVisitor.setVisitorOut(Vview_outTime.getText() != null);

        visitor.setVisitorIn(currentVisitor.isVisitorIn());
        visitor.setVisitorOut(currentVisitor.isVisitorOut());
        visitor.setVisitorID(currentVisitor.getVisitorID());
        visitor.setIdNumber(Vview_idNumber.getText());
        visitor.setDate(Vview_date.getValue());
        visitor.setName(Vview_vName.getText());
        visitor.setPurpose(Vview_purpose.getText());
        visitor.setPhoneNumber(Vview_phoneNumber.getText());
        visitor.setGender(Vview_gender.getValue());
        visitor.setNote(Vview_note.getText());
        visitor.setInTime(Main.getLocalTimeFromString(Vview_inTime.getText()));
        visitor.setOutTime(Main.getLocalTimeFromString(Vview_outTime.getText()));

       // System.out.println("Returned visitor : from getVisitorRecord() "+visitor.toString());

        return visitor;
    }

    public void resetDisplay(){
        Vview_gender.setValue(null);
        Vview_date.setValue(null);
        Vview_displayArea.setText(null);
        Vview_idNumber.setText(null);
        Vview_idSearch.clear();
        Vview_mainTable.setItems(null);
        Vview_inTime.setText(null);
        Vview_outTime.setText(null);
        Vview_note.setText(null);
        Vview_vName.setText(null);
        Vview_phoneNumber.setText(null);
        Vview_purpose.setText(null);
        Vview_recordID.setText(null);
        currentVisitor=new Visitor();
    }

    public void resetRecords(){
        Vview_gender.setValue(null);
        Vview_date.setValue(null);
        Vview_displayArea.setText(null);
        Vview_idNumber.setText(null);
        Vview_idSearch.clear();
        Vview_inTime.setText(null);
        Vview_outTime.setText(null);
        Vview_note.setText(null);
        Vview_phoneNumber.setText(null);
        Vview_purpose.setText(null);
        Vview_recordID.setText(null);
        currentVisitor =new Visitor();
    }

    public void displayVisitorDetails(){
        String strVisitor="\n---Visitor Details---\n"+
                "\nVisitor ID \t: "+currentVisitor.getVisitorID()+"\n"+
                "Date  \t: "+currentVisitor.getDate()+"\n"+
                "Name \t: "+currentVisitor.getName()+"\n"+
                "ID Number \t: "+currentVisitor.getIdNumber()+"\n"+
                "Phone Number \t: "+currentVisitor.getPhoneNumber()+"\n"+
                "Gender  \t: "+currentVisitor.getGender()+"\n"+
                "Note \t: "+currentVisitor.getNote()+"\n"+
                "IN Time \t: "+currentVisitor.getInTime()+"\n"

                ;

        Vview_displayArea.setText(strVisitor);
    }

    public void displayVisitor(Visitor visitor){
        Vview_recordID.setText(String.valueOf(visitor.getVisitorID()));
        Vview_date.setValue(visitor.getDate());
        Vview_idNumber.setText(visitor.getIdNumber());
        Vview_vName.setText(visitor.getName());
        Vview_purpose.setText(visitor.getPurpose());
        Vview_phoneNumber.setText(visitor.getPhoneNumber());
        Vview_gender.setValue(visitor.getGender());
        Vview_note.setText(visitor.getNote());
        Vview_inTime.setText(Main.getStringFromLocalTime(visitor.getInTime()));
        if (!(visitor.getOutTime()==(null))){
            Vview_outTime.setText(Main.getStringFromLocalTime(visitor.getOutTime()));
        }





    }

    public void setTable(){

        if (!isMainTableSet){
            isMainTableSet =true;


            Vview_TvisitorID.setCellValueFactory(new PropertyValueFactory<Visitor,Integer>("visitorID"));
            Vview_TidNumberCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("idNumber"));
            Vview_TidNameCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("name"));
            Vview_TGender.setCellValueFactory(new PropertyValueFactory<Visitor, Gender>("gender"));
            Vview_TreasonCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("purpose"));
            Vview_TiphoneCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("phoneNumber"));
            Vview_TarrivalCol.setCellValueFactory(new PropertyValueFactory<Visitor, LocalTime>("inTime"));
            Vview_ToutTimeCol.setCellValueFactory(new PropertyValueFactory<Visitor, LocalTime>("outTime"));
            Vview_TdateCol.setCellValueFactory(new PropertyValueFactory<Visitor, LocalDate>("date"));
            Vview_TnotesCol.setCellValueFactory(new PropertyValueFactory<Visitor, String>("note"));

        }
    }

    public void setCurrentVisitor(Visitor currentVisitor) {
        this.currentVisitor = currentVisitor;
    }


    public void validateInitialize(){

        //Check Input Field Of Name is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");
        regexValidator.setMessage("Only Text");

        Vview_vName.getValidators().add(regexValidator);
        Vview_vName.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_vName.validate();
        });
        Vview_purpose.getValidators().add(regexValidator);
        Vview_purpose.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_purpose.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        Vview_phoneNumber.getValidators().add(regexValidator_phone_number);
        Vview_phoneNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_phoneNumber.validate();
        });

        //Check Input Field Of NIC
        RegexValidator regexValidator_nic = new RegexValidator();
        regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");
        regexValidator_nic.setMessage("Invalid Please Check");

        Vview_idNumber.getValidators().add(regexValidator_nic);
        Vview_idNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_idNumber.validate();
        });
        Vview_idSearch.getValidators().add(regexValidator_nic);
        Vview_idSearch.focusedProperty().addListener((o, oldVal,newVal)->{
            if(!newVal) Vview_idSearch.validate();
        });

    }

    public Boolean validateInputs(){

        Boolean dataInputs = false;

        //Check Input Field Of Name is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");

        Vview_vName.getValidators().add(regexValidator);
        Vview_vName.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_vName.validate();
        });
        Vview_purpose.getValidators().add(regexValidator);
        Vview_purpose.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_purpose.validate();
        });

        //Check Input Field Of Phone Number
        RegexValidator regexValidator_phone_number = new RegexValidator();
        regexValidator_phone_number.setRegexPattern("[+]94[1-9][0-9]{8}|0[1-9][0-9]{8}");
        regexValidator_phone_number.setMessage("Invalid Please Check");

        Vview_phoneNumber.getValidators().add(regexValidator_phone_number);
        Vview_phoneNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_phoneNumber.validate();
        });

        //Check Input Field Of NIC
        RegexValidator regexValidator_nic = new RegexValidator();
        regexValidator_nic.setRegexPattern("[1-9][0-9]{8}[Vv]|[1-9][0-9]{11}");
        regexValidator_nic.setMessage("Invalid Please Check");

        Vview_idNumber.getValidators().add(regexValidator_nic);
        Vview_idNumber.focusedProperty().addListener((o, oldValue, newValue) -> {
            if(!newValue)  Vview_idNumber.validate();
        });

        if (Vview_vName.validate() && Vview_purpose.validate() && Vview_idNumber.validate() && Vview_phoneNumber.validate() ){
            dataInputs = true;
        }
        return dataInputs;
    }

    public Boolean checkInputs(){

        Boolean allCheck =false;

        if (Vview_date.getValue()==null){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Vview_idNumber.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "ID Number is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Vview_vName.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Vview_purpose.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Purpose is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Vview_phoneNumber.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Phone Number is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Vview_gender.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Gender is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else if (Vview_note.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Note is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        else{
            allCheck = true;
        }
        return allCheck;
    }


}
