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

public class add_event extends AppCompatActivity {

    EditText pickdate, pickhour, txtevent;
    Button btnAdd;
    FirebaseUser user;


    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_event );

        pickdate= findViewById( R.id.pickdate );
        pickhour= findViewById( R.id.pickhour );
        txtevent= findViewById( R.id.txtevent );
        btnAdd= findViewById( R.id.btnAdd );


        btnAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                rootNode = FirebaseDatabase.getInstance();
                reference= rootNode.getReference().child( "Users" ).child( uid ).child( "events" ).push();

                String pickadate = pickdate.getText().toString();
                String pickanhour = pickhour.getText().toString();
                String textevent = txtevent.getText().toString();

                Events events = new Events(pickadate,pickanhour,textevent);

                reference.setValue( events );
                Toast.makeText( add_event.this, "The Event was added successfully ! ", Toast.LENGTH_SHORT ).show();
                Toast.makeText( add_event.this, "You can go back now! ! ", Toast.LENGTH_SHORT ).show();
            }
        } );


    }


}