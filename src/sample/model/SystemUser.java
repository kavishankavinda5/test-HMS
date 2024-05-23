package sample.model;

public class SystemUser {
    private UserRoll userRoll;
    private Admin admin;
    private MedicalOfficer medicalOfficer;
    private Patient patient;
    private Receptionist receptionist;


    public SystemUser() {
        this.admin = null;
        this.medicalOfficer = null;
        this.patient = null;
        this.receptionist = null;
    }

    public UserRoll getUserRoll() {
        return userRoll;
    }

    public void setUserRoll(UserRoll userRoll) {
        this.userRoll = userRoll;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public MedicalOfficer getMedicalOfficer() {
        return medicalOfficer;
    }

    public void setMedicalOfficer(MedicalOfficer medicalOfficer) {
        this.medicalOfficer = medicalOfficer;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Receptionist getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(Receptionist receptionist) {
        this.receptionist = receptionist;
    }

    @Override
    public String toString() {
        return "SystemUser{" +
                "userRoll=" + userRoll +
                ", admin=" + admin +
                ", medicalOfficer=" + medicalOfficer +
                ", patient=" + patient +
                ", receptionist=" + receptionist +
                '}';
    }
}
