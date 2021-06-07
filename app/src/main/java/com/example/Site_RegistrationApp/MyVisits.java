package com.example.Site_RegistrationApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static com.example.Site_RegistrationApp.FBref.refUserVisit;
import static com.example.Site_RegistrationApp.FBref.refUsers;
/**
 * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
 * @version	4.2.1
 * This class reads from firebase the user information about his visits on sites.
 */

public class MyVisits extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView TodayDate;
    String currentDateString = " ", date = " ";
    private ListView lV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_visits);
        TodayDate = findViewById(R.id.TodayDate);
        lV =  findViewById(R.id.lV);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentDateString = new SimpleDateFormat("MM:dd:yyyy", Locale.getDefault()).format(new Date());
            TodayDate.setText("Today is: " + currentDateString);
        }

        //  Reading and showing data
        if (date.equals(" ")) {

            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final ArrayList<String> list = new ArrayList<>();
            final ArrayAdapter Adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
            lV.setAdapter(Adapter);
            refUserVisit.child(currentFirebaseUser).child("Date").child(currentDateString + "")
                    .child("DateCounter").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dS) {
                    list.clear();
                    for (DataSnapshot snapshot : dS.getChildren()) {
                        UserVisit UserTmp = snapshot.getValue(UserVisit.class);
                        String txt = "Visited in: " + UserTmp.getWhereVisited() + " Hour: " + UserTmp.getWhenHour();
                        list.add(txt);
                    }
                    Adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }


    /**
     * This method open dialog that help the user to pick the date he wants to see his information.
     * <p>
     * @param view view is the basic building block of UI
     */

    public void OpenCalendar(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    /**
     * The information get the Year , day and month that the user choose to see and
     * showing the user from firebase the information on this day about his visits on sites.
     * @param view the basic building block.
     * @param year the year the user choose to see.
     * @param month the month the user choose to see.
     * @param dayOfMonth the day of the month the user choose to see.
     */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("MM:dd:yyyy", Locale.getDefault()).format(c.getTime());
        }
        TodayDate.setText("The date you have chosen to see is: " + date);

        String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter Adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        lV.setAdapter(Adapter);
        refUserVisit.child(currentFirebaseUser).child("Date").child(date + "")
                .child("DateCounter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                list.clear();
                for (DataSnapshot snapshot : dS.getChildren()) {
                    UserVisit UserTmp = snapshot.getValue(UserVisit.class);
                    String txt = "Visited in: " + UserTmp.getWhereVisited() + " Hour: " + UserTmp.getWhenHour();
                    list.add(txt);
                }
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * @param menu
     * @return the method return menu inflater
     * @since 1/12/2020
     * This method create option menu
     */
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


        menu.findItem(R.id.MyVisits).setVisible(false);
        return true;
    }

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
        else if (id == R.id.BarCode) {
            Intent si = new Intent(this, Barcode.class);
            startActivity(si);
        }
            if (id == R.id.MyVisitorsmenu) {
                Intent si = new Intent(this, MyVisitors.class);
                startActivity(si);

        }
        return true;
    }

}


