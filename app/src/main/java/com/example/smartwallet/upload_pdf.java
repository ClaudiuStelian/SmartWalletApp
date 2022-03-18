package com.example.smartwallet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upload_pdf extends AppCompatActivity {

    Button upload_btn;
    ImageView back_btn;
    EditText pdf_name;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_upload_pdf );

        back_btn = (ImageView) findViewById( R.id.back_btn );
        upload_btn = findViewById( R.id.upload_btn );
        pdf_name = findViewById( R.id.pdfname );

        //Database
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference();
        String uid = user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child( "pdffiles" );

        upload_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFiles();
            }
        } );

    }

    private void selectFiles() {

        Intent intent = new Intent();
        intent.setType( "application/pdf" );
        intent.setAction(Intent.ACTION_GET_CONTENT  );
        startActivityForResult( Intent.createChooser( intent,"Select PDF Files..." ),1 );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            UploadFilers(data.getData());

        }




    }

    private void UploadFilers(Uri data) {

        final ProgressDialog progressDialog= new ProgressDialog( this );
        progressDialog.setTitle( "Uploading..." );
        progressDialog.show();

        StorageReference reference = storageReference.child( "Uploads/"+System.currentTimeMillis()+".pdf" );
        reference.putFile( data )
                .addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri>uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri url = uriTask.getResult();

                        pdfClass pdfClass= new pdfClass(pdf_name.getText().toString(),url.toString());
                        databaseReference.child( databaseReference.push().getKey() ).setValue( pdfClass );

                        Toast.makeText( upload_pdf.this, "File Uploaded!", Toast.LENGTH_SHORT ).show();

                        progressDialog.dismiss();

                    }
                } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress =(100.0* snapshot.getBytesTransferred())/ snapshot.getBytesTransferred();
                progressDialog.setMessage( "Uploaded" +(int)progress+"%");

            }
        } );
    }

    public void BackBtn (View v){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}