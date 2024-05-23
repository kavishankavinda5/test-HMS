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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.Main;
import sample.controller.actionTask.PostalAction;
import sample.controller.actionTask.ReferenceAction;
import sample.model.Postal;
import sample.model.PostalType;

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

public class PostalViewController {


    boolean isTableSet = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Postal> postalView_userTable;

    @FXML
    private TableColumn<Postal, String> Table_ReferenceNunber;
    @FXML
    private TableColumn<Postal, PostalType> Table_PostalType;
    @FXML
    private TableColumn<Postal, LocalDate> Table_Date;
    @FXML
    private TableColumn<Postal, String> Table_name;
    @FXML
    private TableColumn<Postal, String> Table_address;
    @FXML
    private TableColumn<Postal, String> Table_note;

    @FXML
    private JFXButton postalView_addPostal;

    @FXML
    private JFXButton postalView_deletePostal;

    @FXML
    private JFXButton postalView_viewAll;

    @FXML
    private JFXButton postalView_updatePostal;

    @FXML
    private JFXButton postalView_reset;

    @FXML
    private JFXTextField postalView_toName;

    @FXML
    private Label postalView_addressLable;

    @FXML
    private Label postalView_nameLabel;

    @FXML
    private DatePicker postalView_date;

    @FXML
    private JFXButton postalView_searchPostalBtn;

    @FXML
    private JFXTextArea postalView_note;

    @FXML
    private JFXTextArea postalView_address;

    @FXML
    private JFXComboBox<PostalType> postalView_type;

    @FXML
    private JFXTextField postalView_searchBox;

    @FXML
    private JFXTextField postalView_refecenceNo;

    @FXML
    private JFXTextArea postalView_displayArea;

    @FXML
    private Label postalView_label_refecenceNo;

    @FXML
    private JFXButton postalView_Upload_File;

    @FXML
    private Label postalView_UploadFile_Path;

    @FXML
    private Label noDocument;

    @FXML
    private JFXButton openDocument;

    @FXML
    private Window primaryStage;

    @FXML
    void initialize() {

        //Validate input Data
        validateInitialize();

        //Reset all Data
        resetDisplay();

        postalView_type.getItems().addAll(ReferenceAction.getPostalTypes());

        postalView_type.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!(postalView_type.getValue() == null)) {
                    PostalType postalType = postalView_type.getValue();
                    switch (postalType) {
                        case RECEIVED:
                            postalView_nameLabel.setText("From Name");
                            postalView_addressLable.setText("From Address");
                            break;
                        case DISPATCH:
                            postalView_nameLabel.setText("To Name");
                            postalView_addressLable.setText("To Address");
                            break;
                        default:
                            break;

                    }
                }
            }
        });

        postalView_addPostal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (checkInputs()) {

                    if (validateInputs()) {

                        if (postalView_UploadFile_Path.getText() == null){
                            Object[] options = { "OK", "CANCEL" };
                            Toolkit.getDefaultToolkit().beep();
                            int selectedValue = JOptionPane.showOptionDialog(null,
                                    "Do You Want to Add This Record With Out Document", "Warning",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);

                            if (selectedValue == JOptionPane.WHEN_FOCUSED) {
                                PostalAction.addPostaRecord(getInitialPostal());
                            }
                        }else {
                            PostalAction.addPostaRecord(getInitialPostal());
                        }
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        postalView_updatePostal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (postalView_label_refecenceNo.getText() == null) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Reference No and Search" + "\nor\n" + "Please Select a Record from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                } else if (checkInputs()) {

                    if (validateInputs()) {
                        PostalAction.updatePostalRecord(getPostalRecord());
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Invalid Data", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        postalView_deletePostal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (postalView_label_refecenceNo.getText() == null) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Reference No and Search" + "\nor\n" + "Please Select a Record from the Table",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    PostalAction.deletePostalRecord(getPostalRecord());
                }
            }
        });

        postalView_searchPostalBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (postalView_searchBox.getText().length() <= 0) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null,
                            "Reference No is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {

                    if (postalView_searchBox.validate()) {

                        int searchTerm = Integer.parseInt(postalView_searchBox.getText());

                        Postal foundPostal = PostalAction.searchPostalRecord(searchTerm);
                        dislpayPostalRecord(foundPostal);
                        setPostaDatainView(foundPostal);
                        viewFile(foundPostal);
                    }
                }
            }
        });

        postalView_reset.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                resetDisplay();
            }
        });

        postalView_viewAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ArrayList<Postal> postalArrayList = PostalAction.getAllPostals();
                setMainTable(postalArrayList);
            }
        });

        postalView_userTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Postal postal = postalView_userTable.getSelectionModel().getSelectedItem();
                dislpayPostalRecord(postal);
                setPostaDatainView(postal);
                viewFile(postal);
            }
        });

        postalView_Upload_File.setOnAction(new EventHandler<ActionEvent>() {
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
                    postalView_UploadFile_Path.setText(file.getAbsolutePath());
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
                    if (postalView_UploadFile_Path.getText()!=null){
                        Desktop.getDesktop().open(new File(postalView_UploadFile_Path.getText()));
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


    public Postal getInitialPostal() {
        Postal postal = new Postal();
        postal.setReferenceNo(Main.getPostalReferenceID());
        postal.setPostalType(postalView_type.getValue());
        postal.setDate(postalView_date.getValue());
        postal.setName(postalView_toName.getText());
        postal.setAddress(postalView_address.getText());
        postal.setNote(postalView_note.getText());
        postal.setFilePath(postalView_UploadFile_Path.getText());

        return postal;
    }

    public Postal getPostalRecord() {
        Postal postal = new Postal();
        postal.setPostalType(postalView_type.getValue());
        postal.setReferenceNo(Integer.parseInt(postalView_refecenceNo.getText()));
        postal.setDate(postalView_date.getValue());
        postal.setName(postalView_toName.getText());
        postal.setAddress(postalView_address.getText());
        postal.setNote(postalView_note.getText());
        postal.setFilePath(postalView_UploadFile_Path.getText());

        return postal;
    }

    public void resetDisplay() {
        postalView_type.setValue(null);
        postalView_refecenceNo.setText(null);
        postalView_date.setValue(null);
        postalView_toName.setText(null);
        postalView_address.setText(null);
        postalView_note.setText(null);
        postalView_searchBox.clear();
        postalView_displayArea.setText(null);
        postalView_userTable.setItems(null);
        postalView_label_refecenceNo.setText(null);
        postalView_UploadFile_Path.setText(null);
        noDocument.setVisible(false);
        openDocument.setVisible(false);
    }

    public void dislpayPostalRecord(Postal postal) {

        if (postal.getPostalType() == PostalType.RECEIVED) {
            String receiveRecord = "----RECEIVED POSTAL----\n" +
                    "Reference Number : " + postal.getReferenceNo() + " \n" +
                    "Date     : " + postal.getDate() + " \n" +
                    "From Name  : " + postal.getName() + " \n" +
                    "From Address : " + postal.getAddress() + " \n" +
                    "Note   : " + postal.getNote() + " \n";

            postalView_displayArea.setText(receiveRecord);
        } else {
            String dispatchRecord = "----DISPATCH POSTAL----\n" +
                    "Reference Number : " + postal.getReferenceNo() + " \n" +
                    "Date     : " + postal.getDate() + " \n" +
                    "To Name  : " + postal.getName() + " \n" +
                    "To Address : " + postal.getAddress() + " \n" +
                    "Note   : " + postal.getNote() + " \n";

            postalView_displayArea.setText(dispatchRecord);
        }


    }

    public void setPostaDatainView(Postal postal) {
        postalView_type.setValue(postal.getPostalType());
        postalView_refecenceNo.setText(String.valueOf(postal.getReferenceNo()));
        postalView_label_refecenceNo.setText(String.valueOf(postal.getReferenceNo()));
        postalView_date.setValue(postal.getDate());
        postalView_toName.setText(postal.getName());
        postalView_address.setText(postal.getAddress());
        postalView_toName.setText(postal.getName());
        postalView_note.setText(postal.getNote());
    }

    public void setMainTable(ArrayList<Postal> postalArrayList) {

        ObservableList<Postal> observableList = FXCollections.observableList(postalArrayList);

        if (!isTableSet) {
            Table_ReferenceNunber.setCellValueFactory(new PropertyValueFactory<Postal, String>("referenceNo"));
            Table_PostalType.setCellValueFactory(new PropertyValueFactory<Postal, PostalType>("postalType"));
            Table_Date.setCellValueFactory(new PropertyValueFactory<Postal, LocalDate>("date"));
            Table_name.setCellValueFactory(new PropertyValueFactory<Postal, String>("name"));
            Table_address.setCellValueFactory(new PropertyValueFactory<Postal, String>("address"));
            Table_note.setCellValueFactory(new PropertyValueFactory<Postal, String>("note"));

        }

        postalView_userTable.setItems(observableList);

    }

    public void validateInitialize() {

        //Check Input Field Of is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");
        regexValidator.setMessage("Only Text");

        postalView_toName.getValidators().add(regexValidator);
        postalView_toName.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue) postalView_toName.validate();
        });

        //Check Input Field Of Reference Number is number
        NumberValidator numbValid = new NumberValidator();
        numbValid.setMessage("Only Number");
        postalView_refecenceNo.getValidators().add(numbValid);
        postalView_refecenceNo.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) postalView_refecenceNo.validate();
        });
        postalView_searchBox.getValidators().add(numbValid);
        postalView_searchBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) postalView_searchBox.validate();
        });

        //Check Length Of Reference Number
        StringLengthValidator lengthValidatorNumb = new StringLengthValidator(10);
        postalView_refecenceNo.getValidators().add(lengthValidatorNumb);
        postalView_refecenceNo.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue) postalView_refecenceNo.validate();
        });
        postalView_searchBox.getValidators().add(lengthValidatorNumb);
        postalView_searchBox.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue) postalView_searchBox.validate();
        });

    }

    public Boolean validateInputs() {

        boolean dataInputs = false;

        //Check Input Field Of is text
        RegexValidator regexValidator = new RegexValidator();
        regexValidator.setRegexPattern("[A-Za-z\\s]+");

        postalView_toName.getValidators().add(regexValidator);
        postalView_toName.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue) postalView_toName.validate();
        });

        //Check Input Field Of Reference Number is number
        NumberValidator numbValid = new NumberValidator();

        postalView_refecenceNo.getValidators().add(numbValid);
        postalView_refecenceNo.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) postalView_refecenceNo.validate();
        });


        //Check Length Of Reference Number
        StringLengthValidator lengthValidatorNumb = new StringLengthValidator(10);
        postalView_refecenceNo.getValidators().add(lengthValidatorNumb);
        postalView_refecenceNo.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue) postalView_refecenceNo.validate();
        });

        if (postalView_toName.validate() && postalView_refecenceNo.validate()) {
            dataInputs = true;
        }
        return dataInputs;
    }

    public Boolean checkInputs() {

        boolean allCheck = false;

        if (postalView_type.getSelectionModel().getSelectedIndex() < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Postal Type is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (postalView_refecenceNo.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Reference Number is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (postalView_date.getValue() == null) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Date is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (postalView_toName.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Name is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (postalView_address.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Address is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (postalView_note.getText().length() <= 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Note is Empty", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            allCheck = true;
        }
        return allCheck;
    }

    public String path;

    public void viewFile(Postal postal) {

        try {

            int referenceNo = postal.getReferenceNo();

            String moFilePath ="src/sample/fileStorage/moduleData/userData/postalData";
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

}