package sample.model;

import java.time.LocalDate;

//create a Postal class

public  class Postal {

    //Instance variable

    private PostalType postalType;
    private int referenceNo;
    private LocalDate date;
    private String name;
    private String address;
    private String note;
    private String filePath;

//getters and setters

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PostalType getPostalType() {
        return postalType;
    }

    public void setPostalType(PostalType postalType) {
        this.postalType = postalType;
    }

    public int getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(int referenceNo) {
        this.referenceNo = referenceNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    //toString method
    @Override
    public String toString() {
        return  postalType + "~" +
                referenceNo + '~' +
                date + '~' +
                name + '~' +
                address + '~' +
                note
                ;
    }
}
