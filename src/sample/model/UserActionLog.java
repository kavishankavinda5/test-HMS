package sample.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserActionLog {

    private LocalDate localDate;
    private LocalTime localTime;
    private UserRoll actionByUserRoll;
    private String actionByUserName;
    private UserRoll userRoll;
    private String userName;
    private String actionTaken;

    public UserActionLog(LocalDate localDate, LocalTime localTime, UserRoll actionByUserRoll, String actionByUserName,
                         UserRoll userRoll, String userName, String actionTaken) {
        this.localDate = localDate;
        this.localTime = localTime;
        this.actionByUserRoll = actionByUserRoll;
        this.actionByUserName = actionByUserName;
        this.userRoll = userRoll;
        this.userName = userName;
        this.actionTaken = actionTaken;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
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

    @Override
    public String toString() {
        return  localDate + "~" +
                localTime + "~" +
                actionByUserRoll+  "~" +
                actionByUserName+"~" +
                userRoll + "~" +
                userName+"~"+
                actionTaken +"By "+actionByUserName+"userRoll : "+actionByUserRoll;
    }
}
