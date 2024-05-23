package sample.model;

import javafx.beans.property.SimpleStringProperty;

public class AppointmentTableData {
    String AppointmentId;
    String appDate;
    String appTime;
    String appStatus;
    String appPatientName;
    String appPatientPhoneNumber;
    String appPatientIdCardNumber;
    String appMedicalOfficerName;
    String appMedicalOfficerIdNumber;
    String appSymtoms;


    public AppointmentTableData(String appointmentId,
                                String appDate,
                                String appTime,
                                String appStatus,
                                String appPatientName,
                                String appPatientPhoneNumber,
                                String appPatientIdCardNumber,
                                String appMedicalOfficerName,
                                String appMedicalOfficerIdNumber,
                                        String appSymtoms) {
        this.AppointmentId = appointmentId;
        this.appDate = appDate;
        this.appTime = appTime;
        this.appStatus = appStatus;
        this.appPatientName = appPatientName;
        this.appPatientIdCardNumber = appPatientIdCardNumber;
        this.appPatientPhoneNumber =appPatientPhoneNumber;
        this.appMedicalOfficerName = appMedicalOfficerName;
        this.appMedicalOfficerIdNumber =appMedicalOfficerIdNumber;
        this.appSymtoms = appSymtoms;
    }

    public AppointmentTableData() {

    }

    public String getAppPatientPhoneNumber() {
        return appPatientPhoneNumber;
    }

    public void setAppPatientPhoneNumber(String appPatientPhoneNumber) {
        this.appPatientPhoneNumber = appPatientPhoneNumber;
    }

    public String getAppPatientIdCardNumber() {
        return appPatientIdCardNumber;
    }

    public void setAppPatientIdCardNumber(String appPatientIdCardNumber) {
        this.appPatientIdCardNumber = appPatientIdCardNumber;
    }

    public String getAppMedicalOfficerIdNumber() {
        return appMedicalOfficerIdNumber;
    }

    public void setAppMedicalOfficerIdNumber(String appMedicalOfficerIdNumber) {
        this.appMedicalOfficerIdNumber = appMedicalOfficerIdNumber;
    }

    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppPatientName() {
        return appPatientName;
    }

    public void setAppPatientName(String appPatientName) {
        this.appPatientName = appPatientName;
    }

    public String getAppMedicalOfficerName() {
        return appMedicalOfficerName;
    }

    public void setAppMedicalOfficerName(String appMedicalOfficerName) {
        this.appMedicalOfficerName = appMedicalOfficerName;
    }

    public String getAppSymtoms() {
        return appSymtoms;
    }

    public void setAppSymtoms(String appSymtoms) {
        this.appSymtoms = appSymtoms;
    }
}
