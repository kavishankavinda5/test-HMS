package sample.model;


import java.time.LocalDate;
import java.util.Date;

public abstract class User extends SystemUser{
    private UserRoll userRoll;
    private String userName;
    private String name;
    private Gender gender;
    private String phoneNumber =null;
    private String idCardNumber;
    private LocalDate dob;
    private String address;
    private String maritalStatus;
    private String userPassword;

    private String photoPath;
    private String filePath;

    public String getPhotoPath() {
        return photoPath;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UserRoll getUserRoll() {
        return userRoll;
    }

    public void setUserRoll(UserRoll userRoll) {
        this.userRoll = userRoll;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return  userRoll + "~" +
                name + '~' +
                gender + '~' +
                maritalStatus + '~' +
                dob +"~"+
                phoneNumber + '~' +
                idCardNumber + '~' +
                address + '~' +
                userName + '~' +
                userPassword
                ;
    }
}
