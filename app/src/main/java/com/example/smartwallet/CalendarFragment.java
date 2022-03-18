package com.example.smartwallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CalendarFragment extends Fragment {


    FloatingActionButton add_event;
    private View EventsView;
    private RecyclerView myEventsList;
    private DatabaseReference EventsRef;
    FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EventsView= inflater.inflate(R.layout.fragment_calendar, container, false);
        // Inflate the layout for this fragment

        myEventsList = (RecyclerView) EventsView.findViewById( R.id.rv );
        myEventsList.setLayoutManager( new LinearLayoutManager( getContext() ) );

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        EventsRef = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( uid ).child( "events" );


        return EventsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Events>()
                .setQuery(EventsRef,Events.class )
                .build();


        FirebaseRecyclerAdapter<Events,EventsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Events, EventsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull Events model) {

                String eventsIDs = getRef( position ).getKey();

                EventsRef.child( eventsIDs ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (snapshot.hasChild( "date" )){
                            String date = snapshot.child( "date" ).getValue().toString();
                            String hour = snapshot.child( "hour" ).getValue().toString();
                            String event = snapshot.child( "event" ).getValue().toString();

                            holder.date.setText(  date);
                            holder.hour.setText(  hour);
                            holder.event.setText(event);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );

                holder.deleteevent.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder( holder.event.getContext() );
                        builder.setTitle( "Are you sure that you want to delete?" );
                        builder.setMessage( "Deleted event can't be Undo" );

                        builder.setPositiveButton( "Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();
                                FirebaseDatabase.getInstance().getReference().child( "Users" ).child( uid ).child( "events" )
                                .child( eventsIDs ).removeValue();


                            }
                        } );
                        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText( holder.event.getContext(),"Cancelled.", Toast.LENGTH_LONG ).show();


                            }
                        } );
                        builder.show();
                    }
                } );


            }

            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.events_display,parent,false );
                EventsViewHolder viewHolder = new EventsViewHolder( view );
                return viewHolder;
            }
        };




        myEventsList.setAdapter( adapter );
        adapter.startListening();



    }


    public static class EventsViewHolder extends RecyclerView.ViewHolder{

        TextView date, hour, event;
        Button deleteevent;

        public EventsViewHolder(@NonNull View itemView) {

            super( itemView );

            date = itemView.findViewById( R.id.txtdate );
            hour = itemView.findViewById( R.id.txthour );
            event = itemView.findViewById( R.id.txtevent );
            deleteevent = itemView.findViewById( R.id.deleteevent );
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        add_event = getView().findViewById( R.id.add_event );
        add_event.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( getActivity(), add_event.class );
                startActivity( i );
            }
        } );
    }
}
