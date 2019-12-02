package com.example.tourmate.pojos;

public class UserInformationPojo {
    private String uesrID;
    private String userName;
    private String userEmail;
    private String userPassword;

    public UserInformationPojo() {
    }

    public UserInformationPojo(String uesrID, String userName, String userEmail, String userPassword) {
        this.uesrID = uesrID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUesrID() {
        return uesrID;
    }

    public void setUesrID(String uesrID) {
        this.uesrID = uesrID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
