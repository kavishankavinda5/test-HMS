package sample.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Visitor {
    private int visitorID;
    private String idNumber;
    private String name;
    private String purpose;
    private String phoneNumber;
    private Gender gender;
    private String note;
    private LocalDate date;
    private LocalTime inTime;
    private LocalTime outTime;
    private boolean isVisitorIn;
    private boolean isVisitorOut;

    public Visitor() {
        isVisitorIn=false;
        isVisitorIn=false;
    }

    public Visitor(int visitorID, String idNumber, String name, String purpose, String phoneNumber, Gender gender, String note, LocalTime inTime, LocalTime outTime) {
        this.visitorID = visitorID;
        this.idNumber = idNumber;
        this.name = name;
        this.purpose = purpose;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.note = note;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalTime inTime) {
        this.inTime = inTime;
    }

    public LocalTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalTime outTime) {
        this.outTime = outTime;
    }

    public boolean isVisitorIn() {
        return isVisitorIn;
    }

    public void setVisitorIn(boolean visitorIn) {
        isVisitorIn = visitorIn;
    }

    public boolean isVisitorOut() {
        return isVisitorOut;
    }

    public void setVisitorOut(boolean visitorOut) {
        isVisitorOut = visitorOut;
    }

    @Override
    public String toString() {
        return visitorID +
                "~" + date +
                "~" + idNumber +
                "~" + name +
                "~" + purpose +
                "~" + phoneNumber +
                "~" + gender +
                "~" + note +
                "~" + inTime +
                "~" + outTime +
                 "~" + isVisitorIn +
                "~" + isVisitorOut
        ;
    }
}
