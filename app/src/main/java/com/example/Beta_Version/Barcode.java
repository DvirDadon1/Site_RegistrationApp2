 package com.example.Beta_Version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.Date;
import static com.example.Beta_Version.FBref.refSite;
import static com.example.Beta_Version.FBref.refUserVisit;
import static com.example.Beta_Version.FBref.refUsers;
import static com.example.Beta_Version.FBref.refVisit;

/**
 * @author Dvir Dadon <dd2640@bs.amalnet.k12.il
 * @version 4.2.1
 * @since 1.4.2021
 * @param <Public>
 * The class Allow the user to give his information to the site he is visiting by reading QR code.
 */
public class Barcode<Public> extends AppCompatActivity {
     String QrCodeValue;
     long DateCounter = 1 , UserDateCounter = 1;
    String  SiteName;
     String isAdmin = "0";
    AlertDialog.Builder HealthDeceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
    }

    /**
     *
     *  * This method give as a string Today date.
     *  * <p>
     * @return Today date as string.
     */
    public String storeDatetoFirebase(String strDate) {
        DateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("MM:dd:yyyy");
        }
        Date date = new Date();
        strDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strDate = dateFormat.format(date).toString();
        }
        return strDate;
    }

    /**
     *
     *  This method give as a string current hour(Hour,minutes,seconds).
     *  <p>
     * @return Current Hour as string.
     */
    public String storeHourtoFirebase(String strHour) {

        DateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }
        Date date = new Date();
        strHour = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            strHour = dateFormat.format(date).toString();
        }
        return strHour;
    }

    /**
     *
     *  This method open the qr code scanner.
     * <p>
     */

    public void Scan() {
        IntentIntegrator ig = new IntentIntegrator(this);
        ig.setCaptureActivity(CaptureAct.class);
        ig.setOrientationLocked(false);
        ig.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        ig.setPrompt("Scanning Code");
        ig.initiateScan();

    }
    /**
     *
     *  This method calling to the scan method.
     * <p>
     */
    public void ScanQrCode(View view) {
        Scan();
    }

    /**
     * This method open a dialog of Health Deceleration if the QR code isn't empty ,
     * and then check if the site exist and if all this approved the user information going
     * to his and for the manager.
     * @param requestCode identifies the return result when the result arrives
     * @param resultCode helps to identify from which Intent you came back
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            QrCodeValue = result.getContents();
            SiteName = " ";
        final boolean[] bo = new boolean[1];
            if (result.getContents()!=null) {
                refSite.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange (DataSnapshot snapshot) {
                        Toast.makeText(Barcode.this, snapshot.hasChild(QrCodeValue)+"", Toast.LENGTH_SHORT).show();
                        if (snapshot.hasChild(QrCodeValue)) {
                            SiteName = snapshot.child(QrCodeValue).child("SiteName").getValue().toString();
                            HealthDeceleration = new AlertDialog.Builder(Barcode.this);
                            HealthDeceleration.setTitle("Health Declaration");
                            HealthDeceleration.setMessage("1.Do you agree that you checked your body temperature and you found " +
                                    "out that the body temperature wasn't above 38 degrees"
                                    + '\n' + "2. You agree that you are not coughing and you don't have difficulty breathing");
                            HealthDeceleration.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            String DateString = " ", HourString = " ";
                                            DateString = storeDatetoFirebase(DateString);
                                            HourString = storeHourtoFirebase(HourString);
                                            refUserVisit.child(currentFirebaseUser).child("Date").child(DateString).child("DateCounter").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    for (DataSnapshot dS : snapshot.getChildren()) {
                                                        if (snapshot.exists()) {
                                                            UserDateCounter = snapshot.getChildrenCount() + 1;
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError error) {

                                                }
                                            });

                                            refVisit.child(QrCodeValue).child("Date").child(DateString).child("DateCounter").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        DateCounter = snapshot.getChildrenCount() + 1;
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            refSite.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    refUsers.child(currentFirebaseUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            GetandSendData(snapshot, DateCounter, UserDateCounter, SiteName); // Getting the data of the user and sending
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                    }

                            ).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Barcode.this, "If you don't agree to the Health Deceleration , do not enter the site.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                            HealthDeceleration.create().show();
                        }
                        else {
                            Toast.makeText(Barcode.this, "The QR code wrong or not working in our site!", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * This method get all the user information and send it to the manager and the user in the firebase.
     * <p>
     * @param Snapshot contains data from a Database location.
     * @param datecounter The number of the user that login to the site today.
     * @param userDateCounter The number of the site that the user logged in to sites today.
     * @param sitename The name of the site that the user is going to.
     */
    public void GetandSendData(DataSnapshot Snapshot , Long datecounter , Long userDateCounter , String sitename) {
        String Email = " ",FullName = " ",PhoneNumber = " ";
        String DateString = " ", HourString = " ";
        DateString = storeDatetoFirebase(DateString);
        HourString = storeHourtoFirebase(HourString);
        String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        for (DataSnapshot ds : Snapshot.getChildren()){
            FullName = Snapshot.child("fullName").getValue().toString();
            PhoneNumber = Snapshot.child("phoneNumber").getValue().toString();
            Email = Snapshot.child("email").getValue().toString();

        }

        UserVisit userVisit = new UserVisit(sitename , HourString , currentFirebaseUser , DateString);
        refUserVisit.child(currentFirebaseUser).child("Date").child(DateString).child("DateCounter").child
                (String.valueOf(UserDateCounter)).setValue(userVisit);

        Visit visitor = new Visit(storeHourtoFirebase(HourString),FullName ,PhoneNumber , Email ,SiteName); // Create user visit
        refVisit.child(QrCodeValue).child("Date").child(DateString).child("DateCounter")
                .child(String.valueOf(DateCounter)).setValue(visitor);

        Toast.makeText(this, "Scan completed!", Toast.LENGTH_SHORT).show();

    }


    /**
     * The method check if the QR code that the user as read is exist in the app or he faked it.
     *<p>
     * @param SiteID Contains the ID of the site.
     * @return true if the site exists else false.
     */
    private boolean CheckSite(String SiteID, String QrcodeValue) {
            if (SiteID.equals(QrcodeValue)){
                return true;
            }
        return false;
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


        menu.findItem(R.id.BarCode).setVisible(false);
        return true;
    }

    /**
     * The method check if the user isAdmin in the app so I will know what to show him in the menu.
     * @param snapshot contains data from a Database location.
     * @return 1 if the user is admin and 0 if he is a regular user.
     */
    private String ChecksAdmin(DataSnapshot snapshot) {
        String isAdmin = " ";
        isAdmin = snapshot.child("isAdmin").getValue().toString();
        return isAdmin;
    }


    /**
     * @param item
     * @return the item that selected
     * @since 1/12/2020
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Settings) {
            Intent si = new Intent(this, Settings.class);
            startActivity(si);
        }
        else if (id == R.id.MyVisits) {
            Intent si = new Intent(this, MyVisits.class);
            startActivity(si);
        }
        if (id == R.id.MyVisitorsmenu) {
            Intent si = new Intent(this, MyVisitors.class);
            startActivity(si);

        }
        return true;
    }
}



