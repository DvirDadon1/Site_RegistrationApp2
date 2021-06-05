package com.example.Beta_Version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.Beta_Version.FBref.refUsers;


/**
 * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
 * @version	4.2.1
 * This activity shows credits
 */

public class Credits extends AppCompatActivity {
    String isAdmin = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    @Override
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



        return true;
    }

    private String ChecksAdmin(DataSnapshot snapshot) {
        String isAdmin = " ";
        isAdmin = snapshot.child("isAdmin").getValue().toString();
        return isAdmin;
    }

    /**
     * @since  1/12/2020
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Settings){
            Intent si = new Intent(this,Settings.class);
            startActivity(si);
        }
        else if(id == R.id.BarCode){
            Intent si = new Intent (this , Barcode.class);
            startActivity(si);

        }
        else if (id == R.id.MyVisits){
            Intent si = new Intent(this , MyVisits.class);
            startActivity(si);
        }
        else if(id == R.id.MyVisitorsmenu){
            Intent si = new Intent(this , MyVisitors.class);
            startActivity(si);
        }

        return true;
    }
}