package sample.model;

public class LoginUser {
    private String userName;
    private String userPassword;

    public LoginUser() {
    }

    public LoginUser(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return userName + "~" + userPassword ;

    }
}
