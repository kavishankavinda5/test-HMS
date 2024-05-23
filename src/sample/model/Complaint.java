package sample.model;

import java.time.LocalDate;

public class Complaint {
    private int complaintID;
    private LocalDate complaintDate;
    private String complaintType;
    private String complaintBy;
    private String phoneNumber;
    private String description;
    private String note;
    private String actiontaken;
    private String filePath;

    public Complaint() {
    }

    public LocalDate getComplaintDate() {
        return complaintDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public void setComplaintDate(LocalDate complaintDate) {
        this.complaintDate = complaintDate;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintBy() {
        return complaintBy;
    }

    public void setComplaintBy(String complaintBy) {
        this.complaintBy = complaintBy;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getActiontaken() {
        return actiontaken;
    }

    public void setActiontaken(String actiontaken) {
        this.actiontaken = actiontaken;
    }

    @Override
    public String toString() {
        return  complaintID+"~"+
                complaintDate+"~"+
                complaintType+"~"+
                complaintBy+"~"+
                phoneNumber+"~"+
                description+"~"+
                note+"~"+
                actiontaken;
    }

}
