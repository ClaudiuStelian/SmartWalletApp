package com.example.smartwallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


public class SettingsFragment extends Fragment {
    Button btnchangepassword;
    Button contactus;
    Button logout;
    private EditText confirmemailaddress;
    private Button passwordreset;


    FirebaseAuth auth;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        btnchangepassword = getView().findViewById( R.id.change_password);

        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog( getContext() )
                        .setContentHolder( new ViewHolder( R.layout.password_update_popup ) )
                        .setExpanded( true, 1360 )
                        .create();
                dialogPlus.show();

                confirmemailaddress= (EditText) dialogPlus.findViewById( R.id.confirmemailaddress );
                passwordreset = (Button) dialogPlus.findViewById( R.id.passwordreset );

                auth = FirebaseAuth.getInstance();

                passwordreset.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetPassword();

                    }
                } );

            }

            private void resetPassword() {
                String email = confirmemailaddress.getText().toString().trim();

                if(email.isEmpty()){
                    confirmemailaddress.setError( "Email is required" );
                    confirmemailaddress.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher( email).matches()){
                    confirmemailaddress.setError( "Please provide valid email!" );
                    confirmemailaddress.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( getActivity(), "Check your email to reset your password!",Toast.LENGTH_LONG ).show();
                            Intent i = new Intent( getActivity(), MainActivity.class );
                            startActivity( i );
                        }else
                            Toast.makeText( getActivity(), "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                    }
                } );
            }
        });
    }
}