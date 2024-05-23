package sample.controller.actionTask;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class ReportAction {



    public  String logDataLog ="src/sample/fileStorage/logData/loginDataLog.txt";
    public  String userLog ="src/sample/fileStorage/logData/userLogData.txt";
    public  String appointmentData ="src/sample/fileStorage/moduleData/appointment/appointmentData.txt";

    public  void generateAppointmentReport(){

        ArrayList<Appointment> appointmentArray = AppointmentAction.getAppointmentArrayList();

    }

    public  void generateUserLogReport(){

    }

    public   void generatePatientLoginReport(){


    }

    public  List<ReportType> getReporttype(){

        List<ReportType> reportTypeData = Arrays.asList(new ReportType[]{
                ReportType.APPOINTMENT_DATA_REPORT,
                ReportType.PATIENT_LOGIN_DATA,
                ReportType.USERLOG_DATA_REPORT
        });


        return reportTypeData;
    }

    public  List<PrintType> getPrintType(){

        List<PrintType> printTypeFormat = Arrays.asList(new PrintType[]{
               PrintType.PDF,
                PrintType.CSV
        });


        return printTypeFormat;
    }

    public  List<UserRoll> getUserType(){

        List<UserRoll> userType = Arrays.asList(new UserRoll[]{
               UserRoll.ADMIN,
                       UserRoll.RECEPTIONIST,
                       UserRoll.MEDICALOFFICER,
                       UserRoll.PATIENT,
        });


        return userType;
    }

    public ObservableList<AppointmentTableData> getAppointmentTableData(
            LocalDate startdate,
            LocalDate enddate,
            String id){
        ArrayList<Appointment> allAppointments = filterAppointmentData(
                startdate,
                enddate,
                AppointmentAction.getAppointmentArrayList(),
                id
        );
        ObservableList<AppointmentTableData> appointmentData = FXCollections.observableArrayList();

        for (int i = 0; i < allAppointments.size(); i++) {
            Appointment selectedAppointment = allAppointments.get(i);

           // System.out.println("Selected Appointment " + selectedAppointment.getPatient().getPhoneNumber());
            appointmentData.add(new AppointmentTableData(
                   Integer.toString(selectedAppointment.getAppointmentID()),
                  selectedAppointment.getAppointmentDate().toString(),
                  selectedAppointment.getTime().toString(),
                selectedAppointment.getAppointmentStatus().toString() ,
                selectedAppointment.getPatient().getName() ,
                   selectedAppointment.getPatient().getPhoneNumber(),
                   selectedAppointment.getPatient().getIdCardNumber(),
                   selectedAppointment.getMedicalOfficer().getName(),
                    selectedAppointment.getMedicalOfficer().getIdCardNumber(),
                    selectedAppointment.getSymtomes()
                    ));
        }

        return appointmentData;
    }

    public ObservableList<UserLogingLogTableData> getLoginLogtableData(
            LocalDate start,
            LocalDate end,
            UserRoll roll
    ){
        ArrayList<UserLoginLog> allLoginLog = filterUserLoginLog(start,end, UserAction.getAllloginLog(),roll);
        ObservableList<UserLogingLogTableData> loginLogdata = FXCollections.observableArrayList();

        for (int i = 0; i < allLoginLog.size() ; i++) {
            UserLoginLog userloginLog = allLoginLog.get(i);
            loginLogdata.add(
                    new UserLogingLogTableData(
                            userloginLog.getLogDate().toString(),
                            userloginLog.getLocalTime().toString(),
                            userloginLog.getUserName(),
                            userloginLog.getUserRoll().toString()
                    )
            );
        }

        return loginLogdata;
    }

    public ObservableList<PatientloginTableData> generateuserLoginDataList(){
        ArrayList<UserLoginCredential> allLoginLog = UserAction.getAllUserCredentials();
        ObservableList<PatientloginTableData> patientLoginData = FXCollections.observableArrayList();

        for (int i = 0; i < allLoginLog.size(); i++) {
            UserLoginCredential selecteddata =allLoginLog.get(i);
            patientLoginData.add(
                    new PatientloginTableData(
                            selecteddata.getPatientName(),
                            selecteddata.getPatientName(),
                            selecteddata.getPatientPassword()
                    )
            );
        }

        return patientLoginData;
    }

    public ObservableList<ReportMedicalOfficerTableData> getMedicalOfficerdata(){
        ArrayList<MedicalOfficer> allMedicalData = UserAction.getAllMedicalOfficer();
        ObservableList<ReportMedicalOfficerTableData> medicaloffData = FXCollections.observableArrayList();

        for (int i = 0; i < allMedicalData.size(); i++) {
            MedicalOfficer selecteddata =allMedicalData.get(i);
            medicaloffData.add(
                   new ReportMedicalOfficerTableData(
                           Integer.toString( selecteddata.getStaffID()),
                           selecteddata.getName(),
                           selecteddata.getPhoneNumber(),
                           selecteddata.getIdCardNumber()
                   )
            );
        }

        return medicaloffData;
    }

    public ArrayList<Appointment> filterAppointmentData(LocalDate start,LocalDate end ,ArrayList<Appointment> array,String id){
        DateRange dateranges = new DateRange(start,end);
        List<LocalDate> dateList = dateranges.toList();

        ArrayList<Appointment> filteredAppointments =new  ArrayList();
        ArrayList<Appointment> finalfilteredAppointments =new  ArrayList();
        System.out.println("Selecte DoctorID "+id);

        for (int i = 0; i < array.size(); i++) {
            LocalDate appointmentDate = array.get(i).getAppointmentDate();
            for (int j = 0; j <dateList.size() ; j++) {
                if(appointmentDate.equals( dateList.get(j))){
                    filteredAppointments.add(array.get(i));
                }
            }
        }

        for (int k = 0; k < filteredAppointments.size(); k++) {
            Appointment secondFilter = filteredAppointments.get(k);
            if(secondFilter.getMedicalOfficer().getIdCardNumber().equals(id)){
                finalfilteredAppointments.add(filteredAppointments.get(k));
            }
        }
        System.out.println(finalfilteredAppointments);
        System.out.println(dateList);
    return finalfilteredAppointments;

    }

    public ArrayList<UserLoginLog> filterUserLoginLog(
            LocalDate start,
            LocalDate end,
            ArrayList<UserLoginLog> data,
            UserRoll userRoll){
        DateRange dateranges = new DateRange(start,end);
        List<LocalDate> dateList = dateranges.toList();
        ArrayList<UserLoginLog> filterdloginLog =new  ArrayList();
        ArrayList<UserLoginLog> finalfilteredlog =new  ArrayList();

        for (int i = 0; i < data.size() ; i++) {
            LocalDate logdate = data.get(i).getLogDate();
            for (int j = 0; j <dateList.size() ; j++) {
                if(logdate.equals( dateList.get(j))){
                    filterdloginLog.add(data.get(i));
                }
            }
        }

        for (int k = 0; k < filterdloginLog.size(); k++) {
            UserLoginLog secondFilter = filterdloginLog.get(k);
            if(secondFilter.getUserRoll().equals(userRoll)){
                finalfilteredlog.add(filterdloginLog.get(k));
            }
        }

        System.out.println("Filter Log data "+finalfilteredlog);
        return finalfilteredlog;
    }

}
