package sample.model;

public class UserLoginCredential {
    private String patientName;
    private String patientUserName;
    private String patientPassword;

    public UserLoginCredential(String patientName, String patientUserName, String patientPassword) {
        this.patientName = patientName;
        this.patientUserName = patientUserName;
        this.patientPassword = patientPassword;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientUserName() {
        return patientUserName;
    }

    public void setPatientUserName(String patientUserName) {
        this.patientUserName = patientUserName;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }
}
