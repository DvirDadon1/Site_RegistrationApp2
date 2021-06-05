package com.example.Beta_Version;

/**
 * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
 * @version	4.2.1
 * This is organize all the information that is going to to the user
 */
public class UserVisit {
     private String WhereVisited, WhenHour,Date, UserId;

    public UserVisit() {

    }

    public UserVisit(String WhereVisited, String WhenHour, String userId ,String Date) {
        this.WhereVisited = WhereVisited;
        this.WhenHour = WhenHour;
        this.UserId = userId;
        this.Date = Date;
    }

    public String getDate() {
        return Date;
    }

    public String getUserId() {

        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getWhereVisited() {
        return WhereVisited;
    }

    public void setWhereVisited(String whereVisited) {
        WhereVisited = whereVisited;
    }

    public String getWhenHour() {
        return WhenHour;
    }

    public void setWhenHour(String whenHour) {
        WhenHour = whenHour;
    }




}
