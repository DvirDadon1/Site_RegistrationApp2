package com.example.Beta_Version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import static com.example.Beta_Version.FBref.refUsers;

/**
 *  @author Dvir Dadon
 *  @since 9/11/2021
 *  @version 4.1.1
 *  This class give the user the option to see his profile/Credits or add his app to the application.
 *  Finished!!!
 */
public class Settings extends AppCompatActivity {
    TextView TXTSignSite;
    String isAdmin = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TXTSignSite =  findViewById(R.id.TXTSignSite);

        String SignSite = "Want to add your site to the Application? Click Here!"; //String for the spannable string for Adding user site to the application
        SpannableString SSS = new SpannableString(SignSite);//Spannable string adding use site to the application

        ClickableSpan AddSite = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent si = new Intent (Settings.this , RegisterYourSite.class);
                startActivity(si);

            }
        };

         // The method mark part of the text as I want , and take back the user for the login activity.


        SSS.setSpan(AddSite, 42, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TXTSignSite.setText(SSS);
        TXTSignSite.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     *
     * This method take the user to the credits activity
     */
    public void CreditsActivity(View view) {
       Intent si = new Intent(this , Credits.class);
       startActivity(si);
    }

    public void Go2MyProfile(View view) {
        Intent si = new Intent(this , MyProfile.class);
        startActivity(si);
    }

    public void LogOut(View view) {
        finishAffinity();
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember" , "false");
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Settings.this , MainActivity.class));
        Toast.makeText(this, "Successfully logout ", Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        refUsers.child(currentFirebaseUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (ChecksAdmin(snapshot).equals("0")){
                    menu.findItem(R.id.MyVisitorsmenu).setVisible(false);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        menu.findItem(R.id.Settings).setVisible(false);
        return true;
    }

    private String ChecksAdmin(DataSnapshot snapshot) {
        String isAdmin = " ";
        isAdmin = snapshot.child("isAdmin").getValue().toString();
        return isAdmin;
    }





    /**
     * @since  1/12/2020
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.MyVisits){
            Intent si = new Intent(this , MyVisits.class);
            startActivity(si);
        }
        else if(id == R.id.BarCode){
            Intent si = new Intent (this , Barcode.class);
            startActivity(si);

        }
        else if(id == R.id.MyVisitorsmenu){
            Intent si = new Intent(this , MyVisitors.class);
            startActivity(si);
        }
        return true;
    }
}