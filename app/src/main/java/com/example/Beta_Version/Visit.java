package com.example.Beta_Version;


/**
 *  @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
 *  @version	4.2.1
 *   This class organize all the information of the visition of the user.
 */
public class Visit {
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    private String WhereSite,Hour,UserName , UserPhone , UserEmail;
    public Visit(){

    }

    public Visit(String Hour , String UserName , String UserPhone , String UserEmail  , String WhereSite){
        this.WhereSite = WhereSite;
        this.UserName = UserName;
        this.UserPhone = UserPhone;
        this.UserEmail = UserEmail;
        this.Hour = Hour;
    }

    public String getWhereSite() {
        return WhereSite;
    }

    public void setWhereSite(String whereSite) {
        WhereSite = whereSite;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

}
