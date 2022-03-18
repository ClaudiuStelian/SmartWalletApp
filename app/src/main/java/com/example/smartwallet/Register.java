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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TAG";
    private TextView loginbtn;
    private EditText mFirstname,mLastname,mEmail,mPassword,mRePassword;
    private Button registerbtn;
    private ProgressBar mProgressBar;
    private FirebaseFirestore fStore;
    private String userID;


    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        registerbtn=(Button) findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener( this );

        loginbtn= (TextView) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener( this );


        mFirstname= (EditText) findViewById(R.id.firstname);
        mLastname=  (EditText)findViewById(R.id.lastname);
        mEmail= (EditText) findViewById(R.id.email);
        mPassword=  (EditText) findViewById(R.id.password);
        mRePassword= (EditText) findViewById(R.id.repassword);



        mProgressBar=(ProgressBar) findViewById(R.id.progressbar);

        if(mAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerbtn:
                registerUser();
                break;
            case R.id.loginbtn:
                startActivity( new Intent(this, Login.class) );
                break;
        }

    }

    private void registerUser() {
        String firstName = mFirstname.getText().toString();
        String lastName = mLastname.getText().toString();
        String email= mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String repassword = mRePassword.getText().toString().trim();


        if (TextUtils.isEmpty(firstName)){
            mFirstname.setError("First name si Required!");
            mFirstname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lastName)){
            mLastname.setError("Last name si Required!");
            mLastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){
            mEmail.setError("Email si Required!");
            mEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()){
            mEmail.setError( "Please provide valid email" );
            mEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            mPassword.setError("Password is Required!");
            mPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(repassword)){
            mRePassword.setError("Please repeat the Password!");
            mRePassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("The Password must contain more than 6 characters!");
            mPassword.requestFocus();
            return;
        }
        if (!password.equals(repassword)){
            mRePassword.setError("Please repeat the Password!");
            mRePassword.requestFocus();
            return;
        }

        mProgressBar.setVisibility( View.VISIBLE );
        mAuth.createUserWithEmailAndPassword( email,  password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    userID =mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("Users").document(userID);
                    Map<String,Object>User = new HashMap<>();
                    User.put("firstName", firstName);
                    User.put("lastName", lastName);
                    User.put("email", email);
                    User.put("password", password);
                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "OnnSuccess: user Profile is created for "+ userID);
                        }
                    });

                    User user = new User(firstName,lastName,email, password);


                    FirebaseDatabase.getInstance().getReference("Users")
                            .child( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                            .setValue( user ).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText( Register.this, "User has been registered successfully!", Toast.LENGTH_LONG ).show();
                                mProgressBar.setVisibility( View.GONE );


                                startActivity(new Intent(getApplicationContext(),Login.class));
                                mProgressBar.setVisibility(View.INVISIBLE);

                                //redirect to Login Layout!
                            }else {
                                Toast.makeText( Register.this,"Failed to register! Try again!", Toast.LENGTH_LONG ).show();
                                mProgressBar.setVisibility( View.GONE );
                            }

                        }
                    } );

                }else {
                    Toast.makeText( Register.this,"Failed to register!" + task.getException().getMessage(), Toast.LENGTH_LONG ).show();
                    mProgressBar.setVisibility( View.GONE );

                }


            }
        } );

    }
}