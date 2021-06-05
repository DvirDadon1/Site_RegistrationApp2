package com.example.Beta_Version;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author		Dvir dadon <dd2640@bs.amalnet.k12.il
 * @version 4.2.1
 * This class is a pointer on the Database
 */

public class FBref {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance(); // entry point for accessing a Firebase Database
    public static DatabaseReference refUsers = FBDB.getReference("Users"); //Gets a DatabaseReference for the database "Users".
    public static DatabaseReference refSite = FBDB.getReference("Site");//Gets a DatabaseReference for the database root node("Site").
    public static DatabaseReference refVisit = FBDB.getReference("Visit");//Gets a DatabaseReference for the database root node("Visit").
    public static DatabaseReference refUserVisit = FBDB.getReference("UserVisit");//Gets a DatabaseReference for the database root node("UserVisit").
}
