package com.example.smartwallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Bussinesscardeta_update extends AppCompatActivity {


    EditText firstName,lastName,companyName,companyAddress,companyNumber,emaill;
    Button btnUpdate;
    FirebaseUser user;


    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bussinesscardeta_update );


        companyName= findViewById( R.id.txtcompanyName );
        companyAddress= findViewById( R.id.txtcompanyAddress );
        companyNumber= findViewById( R.id.txtcompanyPhone );
        btnUpdate= findViewById( R.id.btnUpdate );


        btnUpdate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                rootNode = FirebaseDatabase.getInstance();
                reference= rootNode.getReference().child( "Users" ).child( uid ).child( "Businesscard" );


                String companyNamee = companyName.getText().toString();
                String companyAddresss = companyAddress.getText().toString();
                String companyNumberr = companyNumber.getText().toString();


                Businesscard businesscard = new Businesscard(companyNamee,companyAddresss,companyNumberr);

                reference.setValue( businesscard );
                Toast.makeText( Bussinesscardeta_update.this, "The Details were added successfully ! ", Toast.LENGTH_SHORT ).show();
                Toast.makeText( Bussinesscardeta_update.this, "You can go back now! ! ", Toast.LENGTH_SHORT ).show();
            }
        } );

    }
}