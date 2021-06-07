package com.example.Site_RegistrationApp;

/**
 * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
 * @version	4.2.1
 * This class organize all the information for the user.
 */
public class Users {
     String FullName,Email,PhoneNumber,isAdmin,SiteName,UID;
    public Users(){

    }

    /**
     * Organize all the user information that is going to the database.
     * @param FullName The user fullname
     * @param Email the user email
     * @param PhoneNumber the user phone number
     * @param SiteName the site name (if he have one)
     * @param isAdmin User information if he is admin or not
     * @param UID user UID.
     */
    public Users(String FullName , String Email ,String PhoneNumber,String SiteName,String isAdmin,String UID) {
        this.FullName = FullName;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
        this.isAdmin = isAdmin;
        this.SiteName = SiteName;
        this.UID = UID;
    }

    /**
     * Organize specific information to send the manager.
     * <p>
     * @param FullName The user Full name
     * @param Email The user email
     * @param PhoneNumber The user phone number
     */
    public Users(String FullName, String Email ,String PhoneNumber) {
        this.FullName = FullName;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;

    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


}
