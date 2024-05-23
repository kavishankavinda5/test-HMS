package sample.model;

public class UserLogingLogTableData {
    private String logdate;
    private String loggingTime;
    private String userName;
    private String userRoll;

    public UserLogingLogTableData(String logdate, String loggingTime, String userName, String userRoll) {
        this.logdate = logdate;
        this.loggingTime = loggingTime;
        this.userName = userName;
        this.userRoll = userRoll;
    }

    public UserLogingLogTableData() {

    }

    public String getLogdate() {
        return logdate;
    }

    public void setLogdate(String logdate) {
        this.logdate = logdate;
    }

    public String getLoggingTime() {
        return loggingTime;
    }

    public void setLoggingTime(String loggingTime) {
        this.loggingTime = loggingTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRoll() {
        return userRoll;
    }

    public void setUserRoll(String userRoll) {
        this.userRoll = userRoll;
    }
}
