package sample.model;

public class ReportMedicalOfficerTableData {
    private String staffId;
    private String doctorName;
    private String contactNumber;
    private String idNumber;

    public ReportMedicalOfficerTableData(String staffId, String doctorName, String contactNumber,String idNumber) {
        this.staffId = staffId;
        this.doctorName = doctorName;
        this.contactNumber = contactNumber;
        this.idNumber = idNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
