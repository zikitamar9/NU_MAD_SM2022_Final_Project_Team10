package com.example.groupproject4520kroo.EventActions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.groupproject4520kroo.Model.Event;
import com.example.groupproject4520kroo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

@Keep
public class EditEventFragment extends Fragment {
    private static final String ARG_EVENT_ID = "eventId";
    private Event event;
    private FloatingActionButton updateButton;
    private EditText editEventNotes;
    private EditText editEventTime;
    private EditText editEventName;
    private EditText editEventLocation;
    private IEditEventFragmentAction mListener;
    private Switch attendanceSwitch;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public EditEventFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditEventFragment newInstance() {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static EditEventFragment newInstance(Event event) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            if (args.containsKey(ARG_EVENT_ID)) {
                event = (Event) args.getSerializable(ARG_EVENT_ID);
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IEditEventFragmentAction){
            this.mListener = (IEditEventFragmentAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IChatAction");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_edit_event, container, false);

        updateButton = rootView.findViewById(R.id.updateGroupButton);

        editEventNotes = rootView.findViewById(R.id.createEventNotes);
        editEventNotes.setText(event.getNotes());


        editEventName =  rootView.findViewById(R.id.editEventTitle);
        editEventName.setText(event.getName());


        editEventTime =  rootView.findViewById(R.id.createEventTime);
        editEventTime.setText(event.getTime());


        editEventLocation =  rootView.findViewById(R.id.createEventLocation);
        editEventTime.setText(event.getLocation());




        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNote = editEventNotes.getText().toString();
                String newTime = editEventTime.getText().toString();
                String newName = editEventName.getText().toString();
                String newLocation = editEventLocation.getText().toString();

                event.setName(newName);
                event.setNotes(newNote);
                event.setTime(newTime);
                event.setLocation(newLocation);

                updateEvent(event);

                mListener.editToViewEventFragment(event);
                Toast.makeText(getContext(), "event updated", Toast.LENGTH_LONG).show();


            }
        });



        return rootView;
    }

    private void updateEvent(Event event) {

        DocumentReference eventRef = db.collection("events").document(event.getEventId());

        eventRef
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


    }


    public interface IEditEventFragmentAction{
        void editToViewEventFragment(Event event);
    }


}