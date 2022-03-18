package com.example.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    TextInputLayout email, password;
    Button mLogin;
    TextView mRegister, mForgot;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    private EditText confirmemailaddresss;
    private Button passwordresett;

    FirebaseAuth authh;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        mForgot = findViewById(R.id.forgotpass);
        fAuth= FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressbar);



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email si Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("The Password must contain more than 6 characters.");
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()){
                                startActivity( new Intent(Login.this, MainActivity.class) );
                            }else {
                                user.sendEmailVerification();
                                Toast.makeText( Login.this,"Check your email to verify your account!", Toast.LENGTH_LONG ).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });



        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(Login.this )
                        .setContentHolder( new ViewHolder( R.layout.password_update_popup ) )
                        .setExpanded( true, 1360 )
                        .create();
                dialogPlus.show();

                confirmemailaddresss= (EditText) dialogPlus.findViewById( R.id.confirmemailaddress );
                passwordresett = (Button) dialogPlus.findViewById( R.id.passwordreset );

                authh = FirebaseAuth.getInstance();

                passwordresett.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetPassword();

                    }
                } );

            }

            private void resetPassword() {
                String email = confirmemailaddresss.getText().toString().trim();

                if(email.isEmpty()){
                    confirmemailaddresss.setError( "Email is required" );
                    confirmemailaddresss.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher( email).matches()){
                    confirmemailaddresss.setError( "Please provide valid email!" );
                    confirmemailaddresss.requestFocus();
                    return;
                }

                authh.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( Login.this, "Check your email to reset your password!",Toast.LENGTH_LONG ).show();

                        }else
                            Toast.makeText( Login.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                    }
                } );
            }
        });


    }


    public void loginUser(View view){
        isUser();
    }

    private void isUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();
            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    }
                }
            });


        } else {
            // No user is signed in
            Log.d("User not found", "Username or password error");
        }

        };

    }

