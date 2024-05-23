package sample.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserLoginLog {
    private LocalDate logDate;
    private LocalTime localTime;
    private String userName;
    private UserRoll userRoll;
    private String actionTaken;


    public UserLoginLog() {
    }

    public UserLoginLog(LocalDate logDate, LocalTime localTime, String userName, UserRoll userRoll) {
        this.logDate = logDate;
        this.localTime = localTime;
        this.userName = userName;
        this.userRoll = userRoll;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }


    @Override
    public String toString() {
        return  logDate +
                "~" + localTime +
                "~" + userName +
                "~" + userRoll ;
    }
}
