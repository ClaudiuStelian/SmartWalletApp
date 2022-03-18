package com.example.smartwallet;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class BusinesscardFragment extends Fragment {

    Display mDisplay;
    public static final int READ_PHONE = 110;
    Button btnPrint;
    Button btnEdit;

    String imagesUri;
    String path;
    Bitmap bitmap;

    int totalHeight;
    int totalWidth;

    String file_name = "Screenshot";
    File myPath;




    public String getUserData(String callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();
            mDatabase.child("Users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        String firstName = task.getResult().child( "firstName" ).getValue(String.class);
                        String lastName = task.getResult().child( "lastName" ).getValue(String.class);
                        String companyName = task.getResult().child( "Businesscard" ).child( "companyName" ).getValue(String.class);
                        String companyAddress = task.getResult().child( "Businesscard" ).child( "companyAddress" ).getValue(String.class);
                        String companyNumber = task.getResult().child( "Businesscard" ).child( "companyPhone" ).getValue(String.class);
                        String email = task.getResult().child( "email" ).getValue(String.class);

                        if (callback == "showAllUserData") {
                            showAllUserData(firstName, lastName, companyName, companyAddress, companyNumber, email);
                        } else if (callback == "prefillFields") {
                            prefillFields(firstName, lastName, companyName, companyAddress, companyNumber, email);
                        }
                    }
                }
            });


        } else {
            // No user is signed in
            Log.d("User not found", "Username or password error");
        }

        return "getUserData called";
    }

    public Void prefillFields(
            String firstNameValue,
            String lastNameValue,
            String companyNameValue,
            String companyAddressValue,
            String phoneNumberValue,
            String emailValue
    ) {
        Log.d("Inside prefillFields", firstNameValue);

//        updateFirstName.setText(firstNameValue);
//        updateLastName.setText(lastNameValue);
//        updateEmail.setText(emailValue);
//        updateCompanyName.setText(companyNameValue);
//        updateCompanyAddress.setText(companyAddressValue);
//        updateCompanyPhone.setText(phoneNumberValue);

        return null;
    }

    TextView firstname,
            lastname,
            companyName,
            companyAddress,
            companyPhone,
            email;

    EditText updateFirstName,
            updateLastName,
            updateCompanyName,
            updateCompanyAddress,
            updateCompanyPhone,
            updateEmail;

    //global variables to hold user data inside this activity
    String _firstname, _lastname, _companyName, _companyAddress, _companyPhone, _email;
    public View rootView;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        btnPrint = getView().findViewById(R.id.btn_print);
        btnEdit = getView().findViewById( R.id.btn_edit );

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getActivity(), Bussinesscardeta_update.class );
                startActivity( i );
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrint.setVisibility(View.GONE);


                takeScreenShot();




                btnPrint.setVisibility(View.VISIBLE);
            }
        });


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_businesscard, container, false);
        // Inflate the layout for this fragment

        //Hooks
        firstname = rootView.findViewById( R.id.firstnameC );
        lastname = rootView.findViewById( R.id.lastnameC );
        email = rootView.findViewById( R.id.emailC );
        companyName = rootView.findViewById( R.id.companyName );
        companyAddress =  rootView.findViewById( R.id.companyAddress );
        companyPhone = rootView.findViewById( R.id.companyPhone );

        // Update screen fields
        updateCompanyName = rootView.findViewById( R.id.txtcompanyName );
        updateCompanyAddress =  rootView.findViewById( R.id.txtcompanyAddress );
        updateCompanyPhone = rootView.findViewById( R.id.txtcompanyPhone );

        //Show All Data
        getUserData("showAllUserData");

        return rootView;
    }

    private void showAllUserData(String firstNameValue,
                                 String lastNameValue,
                                 String companyNameValue,
                                 String companyAddressValue,
                                 String phoneNumberValue,
                                 String emailValue
    ) {

        firstname.setText(firstNameValue);
        lastname.setText(lastNameValue);
        email.setText(emailValue);
        companyName.setText(companyNameValue);
        companyAddress.setText(companyAddressValue);
        companyPhone.setText(phoneNumberValue);

    }



    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor( Color.WHITE);
        }

        view.draw(canvas);
        return returnedBitmap;
    }


    void takeScreenShot( ){
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(pdfPath + "/ScreenShot/");

        if(!folder.exists()){
            boolean success = folder.mkdir();
        }
        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";

        View u = getView().findViewById(R.id.businessCard);

        CardView z = getView().findViewById(R.id.businessCard);
        totalHeight = z.getChildAt(0).getHeight();
        totalWidth = z.getChildAt(0).getWidth();

        String extr = Environment.getExternalStorageDirectory() + "/Flight Ticket/";
        File file = new File(extr);
        if(!file.exists())
            file.mkdir();
        String fileName = file_name + ".jpg";
        myPath = new File(extr, fileName);
        imagesUri = myPath.getPath();
        bitmap = getBitmapFromView(u, totalHeight, totalWidth);
        try{
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        createPdf();
    }

    private void createPdf() {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        File filePath = new File(path);
        try{
            document.writeTo(new FileOutputStream(filePath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        document.close();

        if (myPath.exists())
            myPath.delete();

        openPdf(path);

    }

    private void openPdf(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType( Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open FIle");
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(getActivity(), "No Apps to read PDF FIle", Toast.LENGTH_SHORT).show();
        }
    }

}