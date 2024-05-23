package sample.model;

public class PatientloginTableData {
    private String patientName;
    private String userName;
    private String userPassword;

    public PatientloginTableData() {
    }

    public PatientloginTableData(String patientName, String userName, String userPassword) {
        this.patientName = patientName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
