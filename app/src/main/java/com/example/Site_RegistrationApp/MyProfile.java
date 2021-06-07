package com.example.Site_RegistrationApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.Site_RegistrationApp.FBref.refUsers;

/**
 * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il>
 * @version	4.2.1
 * This is responsible for showing user his account information

 */
public class MyProfile extends AppCompatActivity {
    ListView ListUserInfo;
    String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String isAdmin = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ListUserInfo = (ListView) findViewById(R.id.ListUserInfo);
        refUsers.child(currentFirebaseUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ShowData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * The method get information from the database and show it on ListView.
     * <p>
     * @param snapshot contains data from a Database location.
     */
    private void ShowData(DataSnapshot snapshot) {
        String Email = " ",FullName = " ",PhoneNumber = " ";
        for (DataSnapshot ds : snapshot.getChildren()){
            FullName = snapshot.child("fullName").getValue().toString();
            PhoneNumber = snapshot.child("phoneNumber").getValue().toString();
            Email = snapshot.child("email").getValue().toString();
        }

        ArrayList <String> array = new ArrayList<>();
        array.add(FullName);
        array.add(Email);
        array.add(PhoneNumber);
        ArrayAdapter adapter = new ArrayAdapter(this , android.R.layout.simple_list_item_1,array);
        ListUserInfo.setAdapter(adapter);

    }


    public boolean onCreateOptionsMenu(Menu menu){
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



        return true;
    }

    private String ChecksAdmin(DataSnapshot snapshot) {
        String isAdmin = " ";
        isAdmin = snapshot.child("isAdmin").getValue().toString();
        return isAdmin;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.MyVisits){
            Intent si = new Intent(this , MyVisits.class);
            startActivity(si);
        }
        else if(id == R.id.Settings){
            Intent si = new Intent(this,Settings.class);
            startActivity(si);
        }
        else if(id == R.id.BarCode){
            Intent si = new Intent (this , Barcode.class);
            startActivity(si);
        }
        else if (id == R.id.MyVisitorsmenu){
            Intent si = new Intent(this , MyVisitors.class);
            startActivity(si);
        }
        return true;
    }
}