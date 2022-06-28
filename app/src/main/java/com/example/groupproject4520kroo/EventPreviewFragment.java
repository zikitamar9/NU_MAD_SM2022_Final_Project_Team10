package com.example.groupproject4520kroo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupproject4520kroo.Model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

@Keep
public class EventPreviewFragment extends Fragment {


    private FirebaseFirestore db;
    ArrayList<Event> events;
    ArrayList<String> eventKeys;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView recyclerViewEventPreview;
    private EventPreviewAdapter eventPreviewAdapter;
    private RecyclerView.LayoutManager recyclerViewEventListLayoutManager;
    private IEventViewFragmentAction mListener;
    private FloatingActionButton addEventButton;

    public EventPreviewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EventPreviewFragment newInstance() {
        EventPreviewFragment fragment = new EventPreviewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("demo", "on create hit");
        if (getArguments() != null) {

        }
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IEventViewFragmentAction){
            this.mListener = (IEventViewFragmentAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IChatAction");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_event_preview, container, false);

        LoadEvents();

        recyclerViewEventPreview = rootView.findViewById(R.id.event_preview_recycler_view);
        recyclerViewEventListLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewEventPreview.setLayoutManager(recyclerViewEventListLayoutManager);

//        eventPreviewAdapter = new EventPreviewAdapter(events, this.getContext());
//        recyclerViewEventPreview.setAdapter(eventPreviewAdapter);
//        eventPreviewAdapter = new EventPreviewAdapter(events, getContext());
//        recyclerViewEventPreview.setAdapter(eventPreviewAdapter);

        addEventButton = rootView.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.toCreateNewEvent();
            }
        });

        return rootView;
    }

    private void LoadEvents() {
        eventKeys = new ArrayList<>();
        db.collection("users")
                .document(mUser.getUid())
                .collection("events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
//                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
//                            retrieving all the elements from Firebase....
                            for(DocumentSnapshot document : value.getDocuments()){
//                                Log.d("demo", "docId"+document.getId());

                                Event eventKey = document.toObject(Event.class);
                                eventKey.setEventId(document.getId());
                                eventKey.setEventCreator(mUser.getUid());



                                eventKeys.add(eventKey.getEventId());

                            }
                            Log.d("demo", "event keys: + " + eventKeys.toString());
                            if (!eventKeys.isEmpty()) {
                                LoadEventsFromKeys(eventKeys);
                            }
                            Log.d("demo", "fragment changed?");
                        }
                    }


                });
    }

    private void LoadEventsFromKeys(ArrayList<String> eventKeys) {
        events = new ArrayList<>();

        db.collection("events")
                .whereIn("eventId", eventKeys)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Event event = document.toObject(Event.class);
                                events.add(event);
                            }

                        }

                        eventPreviewAdapter = new EventPreviewAdapter(events, getContext());
                        recyclerViewEventPreview.setAdapter(eventPreviewAdapter);
                        eventPreviewAdapter.setEvents(events);
                        eventPreviewAdapter.notifyDataSetChanged();


                    }
                });


    }

    public interface IEventViewFragmentAction{
        void toCreateNewEvent();
    }
}