package com.example.Beta_Version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  @author Dvir Dadon
 *  @since 9/11/2021
 *  @version 4.2.1
 *  This class can help the user to register him to the application
 */
public class Register_Activity extends AppCompatActivity  {
    TextView TXTLogin;
    private EditText ETName;
    private EditText ETLastName;
    private EditText ETEmail;
    private EditText ETPhone;
    private EditText ETPassword;
    private FirebaseAuth fAuth;
    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"; // Variable that helps to find if a phone number is exist
    Matcher m; // matcher for phone numbers.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        TXTLogin = findViewById(R.id.TXTLogin);
        ETName = findViewById(R.id.ETName);
        ETLastName = findViewById(R.id.ETLastName);
        ETEmail = findViewById(R.id.ETEmail);
        ETPhone = findViewById(R.id.ETPhone);
        ETPassword = findViewById(R.id.ETPassword);
        fAuth = FirebaseAuth.getInstance();

        String HasAccount = "Already has account? Login here"; //String for the spannable string for Already has account? Login here.
        SpannableString SHA = new SpannableString(HasAccount);//Spannable string for HasAccount


        ClickableSpan ClickHas = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                finish();
            }
        };
        SHA.setSpan(ClickHas, 21, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TXTLogin.setText(SHA);
        TXTLogin.setMovementMethod(LinkMovementMethod.getInstance());


    }

    /**
     * The method invites method in name SignUp
     * @since 16/1/2021
     */
    public void SingUpAsUser(View view) {
        SignUp();
    }

    /**
     * @return The method return true if the phone number is valid else false.
     * @since 17/1/2021
     */
    public boolean CheckPhoneNumber() {
        Pattern r = Pattern.compile(pattern);
        if (!ETPhone.getText().toString().isEmpty()) {
            m = r.matcher(ETPhone.getText().toString().trim());
        } else {
            Toast.makeText(Register_Activity.this, "Please enter mobile number ", Toast.LENGTH_LONG).show();
        }
        return m.find();
    }



    /**
     * The method register the user to the application and checking that all the information he gave
     * is correct.
     * <p>
     */
    private void SignUp() {
        String Name = ETName.getText().toString();
        String LastName = ETLastName.getText().toString();
        String Email = ETEmail.getText().toString();
        String PhoneNumber = ETPhone.getText().toString();
        String password = ETPassword.getText().toString();

        if (Name.isEmpty()) {
            ETName.setError("Name is required");
            ETName.requestFocus();
            return;
        }

        if (LastName.isEmpty()) {
            ETLastName.setError("Last name is required");
            ETLastName.requestFocus();
            return;
        }

        if (Email.isEmpty()) {
            ETEmail.setError("Email is required");
            ETEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ETPassword.setError("Password is required");
            ETPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            ETEmail.setError("Please provide valid email");
            ETEmail.requestFocus();
            return;
        }

        if (PhoneNumber.isEmpty()) {
            ETPhone.setError("Please enter a phone number");
            ETPhone.requestFocus();
            return;
        }

        if (!CheckPhoneNumber()) {
            ETPhone.setError("Please enter a valid phone number");
            ETPhone.requestFocus();
            return;
        }

        if (password.length() < 6) {
            ETPassword.setError("Password must be at least 6 Characters!");
            ETPassword.requestFocus();
            return;
        }



        String FullName = Name + " " + LastName;


        fAuth.createUserWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = fAuth.getCurrentUser();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(Register_Activity.this, "Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                    }
                });
                    if (task.isSuccessful()) {
                        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Users users = new Users(FullName,Email,PhoneNumber,"Don't Have a site","0",UID);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(UID)
                                .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register_Activity.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext() , MainActivity.class));
                                    finish();
                                    //Move the user to the login activity
                                } else {
                                    Toast.makeText(Register_Activity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    } else {
                        Toast.makeText(Register_Activity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                    }

                }
        });

        
    }

}



