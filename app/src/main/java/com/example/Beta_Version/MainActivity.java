 package com.example.Beta_Version;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Dvir Dadon <dd2640@bs.amalnet.k12.il
 * @since 9/11/2021
 * @version 4.2.1
 * This class is the class where the user can login or choose to go to the register activity
 * */


// Don't forget to add Forget password method and stay connected.
public class MainActivity extends AppCompatActivity {
    EditText ETMail,ETPass;
    private FirebaseAuth fAuth;
    TextView TXTForgot,TXTRegister;
    private CheckBox remember;
    AlertDialog.Builder passwordResetDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TXTForgot = findViewById (R.id.TXTForgot);
        TXTRegister = findViewById (R.id.TXTRegister);
        ETMail = findViewById(R.id.ETMail);
        ETPass = findViewById(R.id.ETPass);
        fAuth = FirebaseAuth.getInstance();
        remember = findViewById(R.id.cBstayconnect);
        String textPass = "Forgot Password?";
        String textRegister = "New to the app? Register here";

        SpannableString SP = new SpannableString(textPass);//Spannable string for textPass
        SpannableString SR = new SpannableString(textRegister);//Spannable string for textRegister

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String RememberMe = preferences.getString("remember" , "");
        if (RememberMe.equals("true")){
            Intent intent = new Intent(MainActivity.this , MyVisits.class);
            startActivity(intent);
        }
        else if (RememberMe.equals("false")){

        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember" , "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Checked!", Toast.LENGTH_SHORT).show();
                }
                else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember" , "false");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "UnChecked!", Toast.LENGTH_SHORT).show();
                }
            }
        });

         // The method mark a part of the text as I want and move the user to the forgot password activity
        // if he clicked in the linked text.
        ClickableSpan ClickPass = new ClickableSpan() {
            @Override
            public void onClick( View v) {
                 EditText ResetMail = new EditText(v.getContext());
                 passwordResetDialog = new AlertDialog.Builder(v.getContext());
                 passwordResetDialog.setTitle("Reset Password?");
                 passwordResetDialog.setMessage("Enter your Email to receive the reset link");
                 passwordResetDialog.setView(ResetMail);
                 passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         //extract the email and send reset link
                         String mail = ResetMail.getText().toString();
                         if (Patterns.EMAIL_ADDRESS.matcher(mail).matches() || !mail.isEmpty()) {
                             fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void unused) {
                                     Toast.makeText(MainActivity.this, "Reset link sent to your email!", Toast.LENGTH_SHORT).show();
                                 }

                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(Exception e) {
                                     Toast.makeText(MainActivity.this, "Error! Reset Link is Not sent!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }
                         else {
                             Toast.makeText(MainActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
                 passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                         dialog.cancel();
                     }
                 });
                 passwordResetDialog.create().show();
            }
        };
        SP.setSpan(ClickPass,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TXTForgot.setText(SP);
        TXTForgot.setMovementMethod(LinkMovementMethod.getInstance());

         //The method mark a part of the text as I want and move the user to the register activity
         // if he clicked in the linked text.

        ClickableSpan ClickRegister = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent si = new Intent(MainActivity.this,Register_Activity.class);
                startActivity(si);
            }


        };
        SR.setSpan(ClickRegister,16,29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TXTRegister.setText(SR);
        TXTRegister.setMovementMethod(LinkMovementMethod.getInstance());

    }



    /**
     *  The method invites the method userLogin()
     * <p>
     *
     */
    public void SignIn(View view) {
        userLogin();
    }

    //Do a method that get the user phone.

    /**
     * @ This method check if the user information input is correct and if his email is verified
     * if his email isn't verified so the method send him email.
     <p>

     */
    private void userLogin() {
        String email = ETMail.getText().toString().trim();
        String password =ETPass.getText().toString().trim();
        if(email.isEmpty()){
            ETMail.setError("Email is required");
            ETMail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            ETPass.setError("Email is required");
            ETPass.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ETMail.setError("PLease enter a valid email!");
            ETMail.requestFocus();
            return;
        }
        if(password.length()<6){
            ETPass.setError("Password must be at least 6 characters!");
            ETPass.requestFocus();
            return;
        }
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = fAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, MyVisits.class));
                        finish();
                    }
                    else{
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                Toast.makeText(MainActivity.this, "Verification Email has been sent!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login! please check your credentials", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

}