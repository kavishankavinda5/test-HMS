package sample.controller.taskControllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import sample.Main;
import sample.controller.actionTask.ReferenceAction;
import sample.controller.actionTask.ReportAction;
import sample.controller.actionTask.UserAction;
import sample.model.AppointmentTableData;
import sample.controller.actionTask.AppointmentAction;


import sample.model.*;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportGenController {

    private boolean isReportTypeSelected=false;
    private boolean isFromdateSelected=false;
    private boolean isToDateSelected=false;
    private boolean isMedicalOfficerSelected=false;
    private boolean isUsertypecreated=false;
    private ReportMedicalOfficerTableData selectedOfficerForReport;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private BorderPane reportGenarator_boarderPane;
    @FXML private TabPane reportView_maintabPain;
    @FXML private ComboBox<ReportType> reportGenarator_rType;
    @FXML private ComboBox<String> selectMedicalOfficer;
    @FXML private ComboBox<UserRoll> userType;
    @FXML private ComboBox<PrintType> printType_format;
    @FXML private DatePicker reportGenarator_dateFrom;
    @FXML private DatePicker reportGenarator_dateTo;
    @FXML private JFXButton appointmentSearch;
    @FXML private AnchorPane medicalOfficerSection;
    @FXML private AnchorPane userTypeSection;
    @FXML private AnchorPane datePickerSection;

    @FXML private TabPane reportGen_tabPane;


    @FXML private JFXButton reportGen_selectDoc;
    @FXML private JFXButton reportGen_doctorselectButton;
    @FXML private JFXButton addMedicalOfficer;
    @FXML private Label reportGen_doctorShow;
    @FXML private Label medicalOfficerID_selected;
    @FXML private JFXTextField selectedDoctorName;
    @FXML private JFXButton printDocument;

    //@FXML private TableView<AppointmentTableData> appTab_appointmentTable;



    @FXML void initialize() {

        resetDisplay();
        ReportAction newReportAction = new ReportAction();
        reportGen_tabPane.setDisable(true);

        reportGenarator_rType.getItems().addAll(newReportAction.getReporttype());
        printType_format.getItems().addAll(newReportAction.getPrintType());
        userType.getItems().addAll(newReportAction.getUserType());

        reportGen_doctorselectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //String docSpectArea = reportgen_doctorSpecDrop.getValue();
                setDoctorSelectTable(newReportAction.getMedicalOfficerdata());
            }
        });

        reportGen_docTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedOfficerForReport = reportGen_docTable.getSelectionModel().getSelectedItem();
                reportGen_doctorShow.setText(
                        "StaffID : "+selectedOfficerForReport.getIdNumber()+"\n"+
                        "Name : "+selectedOfficerForReport.getDoctorName()+"\n"
                );
                System.out.println("selected medicalOfficer"+selectedOfficerForReport.toString());
            }
        });

        reportGen_selectDoc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedDoctorName.setText(selectedOfficerForReport.getDoctorName());
                medicalOfficerID_selected.setText(selectedOfficerForReport.getIdNumber());

            }
        });


        printDocument.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                switch (reportGenarator_rType.getValue()){

                    case APPOINTMENT_DATA_REPORT:
                        printAppoinmentData();
                        break;
                    case PATIENT_LOGIN_DATA:
                        printLoginData();
                        break;
                    case USERLOG_DATA_REPORT:
                        userLogData();
                        break;
                }
            }
        });


        reportGenarator_rType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                switch (reportGenarator_rType.getValue()){
                    case APPOINTMENT_DATA_REPORT:
                        setViewForAppointment();
                        break;
                    case PATIENT_LOGIN_DATA:
                        setViewForPatientLoginData();
                        break;
                    case USERLOG_DATA_REPORT:
                        setViewForLoginLog();
                        break;
                }
            }
        });

        appointmentSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate start =  reportGenarator_dateFrom.getValue();
                LocalDate end = reportGenarator_dateTo.getValue();
                UserRoll selectedroll = userType.getValue();

                switch (reportGenarator_rType.getValue()){
                    case APPOINTMENT_DATA_REPORT:
                        ObservableList<AppointmentTableData> tableDataApp = newReportAction.getAppointmentTableData(
                                reportGenarator_dateFrom.getValue(),
                                reportGenarator_dateTo.getValue(),
                                medicalOfficerID_selected.getText()
                        );
                        searchAppointmentRecords(tableDataApp);
                        break;
                    case USERLOG_DATA_REPORT:
                        ObservableList<UserLogingLogTableData> tableDataApp1 = newReportAction.getLoginLogtableData(
                              start,
                              end,
                                selectedroll
                        );
                        searchUserlogginglog(tableDataApp1);
                        break;
                    case PATIENT_LOGIN_DATA:
                        ObservableList<PatientloginTableData> tableDataApp2 = newReportAction.generateuserLoginDataList();
                        searchPatientLogindata(tableDataApp2);
                        break;
                }


            }
        });

        addMedicalOfficer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                reportGen_tabPane.getSelectionModel().select(reportGen_tabPane_mediSelect);
            }
        });

    }

    private void userLogData() {

        UserLogingLogTableData userLogingLogTableData = new UserLogingLogTableData();
        List <List<String>> arrList =new ArrayList<>();
        for (int i=0; i<appView_userLogT.getItems().size(); i++){

            userLogingLogTableData=appView_userLogT.getItems().get(i);
            arrList.add(new ArrayList<>());
            arrList.get(i).add(userLogingLogTableData.getLogdate());
            arrList.get(i).add(userLogingLogTableData.getLoggingTime());
            arrList.get(i).add(userLogingLogTableData.getUserName());
            arrList.get(i).add(userLogingLogTableData.getUserRoll());

        }

        FileOutputStream file = null;
        Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new CMYKColor(255, 255, 255, 255));

        try {
            file = new FileOutputStream(new File("src/sample/fileStorage/moduleData/report\\USER_LOG_DATA.pdf"));


        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        }


        Document document = new Document();
        try {
            PdfWriter.getInstance(document, file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        Paragraph p = new Paragraph("",blueFont);

        try
        {
            p.add(" =========================================================================================");
            p.add("\n                                                                       USER   LOG   DATA  REPORT ");
            p.add("\n =========================================================================================");

            for (int i=0; i<arrList.size();i++){

                p.add("\n");
                p.add("\n          Logging Date                :- "+arrList.get(i).get(0));
                p.add("\n          Logging Time                :- "+arrList.get(i).get(1));
                p.add("\n          User Name                   :- "+arrList.get(i).get(2));
                p.add("\n          UserRoll                       :- "+arrList.get(i).get(2));
                p.add("\n");
                p.add("\n -----------------------------------------------------------------------------------------");
            }
            p.add("\n");
            p.add("\n =========================================================================================");

            document.add(p);

            document.close();
            //writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void printLoginData() {

        PatientloginTableData patientloginTableData = new PatientloginTableData();
        List <List<String>> arrList =new ArrayList<>();
        for (int i=0; i<appView_PatientDataT.getItems().size(); i++){

            patientloginTableData=appView_PatientDataT.getItems().get(i);
            arrList.add(new ArrayList<>());
            arrList.get(i).add(patientloginTableData.getPatientName());
            arrList.get(i).add(patientloginTableData.getUserName());
            arrList.get(i).add(patientloginTableData.getUserPassword());

        }

        FileOutputStream file = null;
        Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new CMYKColor(255, 255, 255, 255));

        try {
            file = new FileOutputStream(new File("src/sample/fileStorage/moduleData/report\\PATIENT_LOGIN_DATA.pdf"));


        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        }


        Document document = new Document();
        try {
            PdfWriter.getInstance(document, file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        Paragraph p = new Paragraph("",blueFont);

        try
        {
            p.add(" =========================================================================================");
            p.add("\n                                                                   PATIENT   LOGIN   DATA  REPORT ");
            p.add("\n =========================================================================================");

            for (int i=0; i<arrList.size();i++){

                p.add("\n");
                p.add("\n          Patient Name                :- "+arrList.get(i).get(0));
                p.add("\n          User Name                    :- "+arrList.get(i).get(1));
                p.add("\n          User Password              :- "+arrList.get(i).get(2));
                p.add("\n");
                p.add("\n -----------------------------------------------------------------------------------------");
            }
            p.add("\n");
            p.add("\n =========================================================================================");

            document.add(p);

            document.close();
            //writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void printAppoinmentData() {

        AppointmentTableData appointmentTableData = new AppointmentTableData();
        List <List<String>> arrList =new ArrayList<>();
        for (int i=0; i<appTab_appointmentTable.getItems().size(); i++){

            appointmentTableData=appTab_appointmentTable.getItems().get(i);
            arrList.add(new ArrayList<>());
            arrList.get(i).add(String.valueOf(appointmentTableData.getAppointmentId()));
            arrList.get(i).add(String.valueOf(appointmentTableData.getAppStatus()));
            arrList.get(i).add(appointmentTableData.getAppTime());
            arrList.get(i).add(appointmentTableData.getAppDate());
            arrList.get(i).add(appointmentTableData.getAppPatientName());
            arrList.get(i).add(appointmentTableData.getAppPatientPhoneNumber());
            arrList.get(i).add(appointmentTableData.getAppPatientIdCardNumber());
            arrList.get(i).add(appointmentTableData.getAppMedicalOfficerName());
            arrList.get(i).add(appointmentTableData.getAppMedicalOfficerIdNumber());

        }

        FileOutputStream file = null;
        Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new CMYKColor(255, 255, 255, 255));

        try {
            file = new FileOutputStream(new File("src/sample/fileStorage/moduleData/report\\PRINT.pdf"));


        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        }


        Document document = new Document();
        try {
            PdfWriter.getInstance(document, file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        Paragraph p = new Paragraph("",blueFont);

        try
        {
            p.add(" =========================================================================================");
            p.add("\n                                                                       APPOINTMENT   DATA   REPORT ");
            p.add("\n =========================================================================================");

            for (int i=0; i<arrList.size();i++){

                p.add("\n");
                p.add("\n          Appointment Id                :- "+arrList.get(i).get(0));
                p.add("\n          Status                              :- "+arrList.get(i).get(1));
                p.add("\n          Time                                :- "+arrList.get(i).get(2));
                p.add("\n          Date                                :- "+arrList.get(i).get(3));
                p.add("\n          Patient Name                  :- "+arrList.get(i).get(4));
                p.add("\n          Patient Phone Number   :- "+arrList.get(i).get(5));
                p.add("\n          Patient NIC                     :- "+arrList.get(i).get(6));
                p.add("\n          MedicalOfficer Name      :- "+arrList.get(i).get(7));
                p.add("\n          MedicalOfficer NIC         :- "+arrList.get(i).get(8));
                p.add("\n");
                p.add("\n -----------------------------------------------------------------------------------------");
            }
            p.add("\n");
            p.add("\n =========================================================================================");

            document.add(p);

            document.close();
            //writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void searchPatientLogindata(ObservableList<PatientloginTableData> data) {
        setPatientLoginDatatable(data);
    }

    private void searchUserlogginglog(ObservableList<UserLogingLogTableData> data) {
        setLoginLogtable(data);
    }

    private void searchAppointmentRecords(ObservableList<AppointmentTableData> data) {
        setAppointTable(data);
    }


    private void resetDisplay(){
        medicalOfficerSection.setVisible(false);
        userTypeSection.setVisible(false);
        if(!reportGenarator_dateTo.isVisible()){
            reportGenarator_dateTo.setVisible(true);
            reportGenarator_dateFrom.setVisible(true);
        }

    }

    private void setViewForLoginLog() {
        reportGen_tabPane.setDisable(true);
        medicalOfficerSection.setVisible(false);
        userTypeSection.setVisible(true);
        appointmentData_Ttab.setDisable(true);
        appT_UserLogTableTab.setDisable(false);
        reportView_maintabPain.getSelectionModel().select(appT_UserLogTableTab);
        patientLog_tableTab.setDisable(true);
        datePickerSection.setVisible(true);

    }

    private void setViewForPatientLoginData() {
        reportGen_tabPane.setDisable(true);
        medicalOfficerSection.setVisible(false);
        userTypeSection.setVisible(false);
        appointmentData_Ttab.setDisable(true);
        appT_UserLogTableTab.setDisable(true);
        patientLog_tableTab.setDisable(false);
        reportView_maintabPain.getSelectionModel().select(patientLog_tableTab);
        reportGenarator_dateTo.setVisible(false);
        reportGenarator_dateFrom.setVisible(false);
        datePickerSection.setVisible(false);
    }

    private void setViewForAppointment() {
        reportGen_tabPane.setDisable(false);
        medicalOfficerSection.setVisible(true);
        userTypeSection.setVisible(false);
        appointmentData_Ttab.setDisable(false);
        appT_UserLogTableTab.setDisable(true);
        patientLog_tableTab.setDisable(true);
        reportView_maintabPain.getSelectionModel().select(appointmentData_Ttab);
        datePickerSection.setVisible(true);

    }

    //AppointmentReportTable
    @FXML private Tab appointmentData_Ttab;
    @FXML private TableView<AppointmentTableData> appTab_appointmentTable;
    @FXML private TableColumn<AppointmentTableData, String> appT_appid;
    @FXML private TableColumn<AppointmentTableData, String> appT_appStatus;
    @FXML private TableColumn<AppointmentTableData, String> appT_appTime;
    @FXML private TableColumn<AppointmentTableData, String> appT_pateintName;
    @FXML private TableColumn<AppointmentTableData, String> appT_appdate;
    @FXML private TableColumn<AppointmentTableData, String> appT_pateintPhoneNumber;
    @FXML private TableColumn<AppointmentTableData, String> appT_pateintNIC;
    @FXML private TableColumn<AppointmentTableData, String> appT_medicalOfficer;
    @FXML private TableColumn<AppointmentTableData, String> appT_medicalOfficerNIC;

    public void setAppointTable(ObservableList<AppointmentTableData> appointmentdata){
        appT_appid.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("AppointmentId"));
        appT_appStatus.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appStatus"));
        appT_appTime.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appTime"));
        appT_pateintName.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appPatientName"));
        appT_pateintPhoneNumber.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appPatientPhoneNumber"));
        appT_pateintNIC.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appPatientIdCardNumber"));
        appT_medicalOfficer.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appMedicalOfficerName"));
        appT_medicalOfficerNIC.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appMedicalOfficerIdNumber"));
        appT_appdate.setCellValueFactory(new PropertyValueFactory<AppointmentTableData, String>("appDate"));

        appTab_appointmentTable.setItems(appointmentdata);
    }

    //UserLogdata table
    @FXML private Tab appT_UserLogTableTab;
    @FXML private TableView<UserLogingLogTableData> appView_userLogT;
    @FXML private TableColumn<UserLogingLogTableData, String> uLogTable_date;
    @FXML private TableColumn<UserLogingLogTableData, String> uLogTable_time;
    @FXML private TableColumn<UserLogingLogTableData, String> uLogTable_userName;
    @FXML private TableColumn<UserLogingLogTableData, String> uLogTable_userRoll;

    public void setLoginLogtable(ObservableList<UserLogingLogTableData> loginLogdata){
        uLogTable_date.setCellValueFactory(new PropertyValueFactory<UserLogingLogTableData, String>("logdate"));
        uLogTable_time.setCellValueFactory(new PropertyValueFactory<UserLogingLogTableData, String>("loggingTime"));
        uLogTable_userName.setCellValueFactory(new PropertyValueFactory<UserLogingLogTableData, String>("userName"));
        uLogTable_userRoll.setCellValueFactory(new PropertyValueFactory<UserLogingLogTableData, String>("userRoll"));

        appView_userLogT.setItems(loginLogdata);

    }

    //Patient Logging Data Table

    @FXML private Tab patientLog_tableTab;
    @FXML private TableView<PatientloginTableData> appView_PatientDataT;
    @FXML private TableColumn<PatientloginTableData, String> patientTable_pname;
    @FXML private TableColumn<PatientloginTableData, String> patientTable_pUserName;
    @FXML private TableColumn<PatientloginTableData, String> patientTable_pPassword;

    public void setPatientLoginDatatable(ObservableList<PatientloginTableData> loginLogdata){
        patientTable_pname.setCellValueFactory(new PropertyValueFactory<PatientloginTableData, String>("patientName"));
        patientTable_pUserName.setCellValueFactory(new PropertyValueFactory<PatientloginTableData, String>("userName"));
        patientTable_pPassword.setCellValueFactory(new PropertyValueFactory<PatientloginTableData, String>("userPassword"));

        appView_PatientDataT.setItems(loginLogdata);

    }

    @FXML private Tab reportGen_tabPane_mediSelect;
    @FXML private TableView<ReportMedicalOfficerTableData> reportGen_docTable;
    @FXML private TableColumn<ReportMedicalOfficerTableData, String> doctorSelectT_id;
    @FXML private TableColumn<ReportMedicalOfficerTableData, String> doctorSelectT_name;
    @FXML private TableColumn<ReportMedicalOfficerTableData, String> doctorSelectT_contactNumber;
    @FXML private TableColumn<ReportMedicalOfficerTableData, String> doctorSelectT_nic;


    private void setDoctorSelectTable(ObservableList<ReportMedicalOfficerTableData> data) {
        doctorSelectT_id.setCellValueFactory(new PropertyValueFactory<ReportMedicalOfficerTableData, String>("staffId"));
        doctorSelectT_name.setCellValueFactory(new PropertyValueFactory<ReportMedicalOfficerTableData, String>("doctorName"));
        doctorSelectT_contactNumber.setCellValueFactory(new PropertyValueFactory<ReportMedicalOfficerTableData, String>("contactNumber"));
        doctorSelectT_nic.setCellValueFactory(new PropertyValueFactory<ReportMedicalOfficerTableData, String>("idNumber"));

        reportGen_docTable.setItems(data);
    }

}
