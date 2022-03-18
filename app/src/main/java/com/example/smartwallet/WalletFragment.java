package com.example.smartwallet;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class WalletFragment extends Fragment {

    FloatingActionButton add_button, add_pdf;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    ListView listView;
    DatabaseReference databaseReference;
    List<pdfClass> uploads;
    FirebaseUser user;

    boolean isOpen = false;//by default it is false


    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        add_button = (FloatingActionButton) getView().findViewById( R.id.add_button );
        add_pdf = (FloatingActionButton) getView().findViewById( R.id.add_pdf );

        listView= getView().findViewById( R.id.listview );
        uploads= new ArrayList<>();

        //create method


        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                pdfClass pdfupload= uploads.get(i);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType( "application/pdf" );
                intent.setData( Uri.parse( pdfupload.getUrl()));
                startActivity( intent );
            }
        } );
        

        //animations
        fabOpen = AnimationUtils.loadAnimation( getActivity(), R.anim.fab_open );
        fabClose = AnimationUtils.loadAnimation( getActivity(), R.anim.fab_close );

        rotateForward = AnimationUtils.loadAnimation( getActivity(), R.anim.rotate_forward );
        rotateBackward = AnimationUtils.loadAnimation( getActivity(), R.anim.rotate_backward );

        //set the click lister on the MAIN FAB

        add_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateadd_buttom();
            }
        } );

        viewAllFiles();
    }

    private String viewAllFiles() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( uid ).child( "pdffiles" );
        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                    pdfClass pdfClass = postsnapshot.getValue( com.example.smartwallet.pdfClass.class );

                    uploads.add( pdfClass );

                }
                String[] Uploads = new String[uploads.size()];
                for (int i = 0; i < Uploads.length; i++) {
                    Uploads[i] = uploads.get( i ).getPdfname();
                }

                Object mActivity = getActivity();

                if (mActivity != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1, Uploads ) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView( position, convertView, parent );
                            TextView text = (TextView) view.findViewById( android.R.id.text1 );
                            text.setTextColor( Color.WHITE);
                            text.setTextSize( 22 );

                            return view;
                        }
                    };

                    listView.setAdapter( adapter );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return "viewAllFiles()";
    }



    private void animateadd_buttom() {
        if (isOpen) {
            add_button.startAnimation( rotateForward );
            add_pdf.startAnimation( fabClose );
            add_pdf.setClickable( false );
            isOpen = false;
        } else {
            add_button.startAnimation( rotateBackward );
            add_pdf.startAnimation( fabOpen );
            add_pdf.setClickable( true );

            isOpen = true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_wallet, container, false );


        FloatingActionButton add_flight = v.findViewById( R.id.add_pdf );
        add_flight.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getActivity(), upload_pdf.class );
                startActivity( i );
            }
        } );


        return v;

    }

}