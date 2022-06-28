package com.example.groupproject4520kroo.EventActions;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groupproject4520kroo.Model.Event;
import com.example.groupproject4520kroo.Model.Group;
import com.example.groupproject4520kroo.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
@Keep
public class CreateEventFragment extends Fragment {

    private FirebaseFirestore db;
    ArrayList<Group> groups;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView recyclerViewCreateEvent;
    private CreateEventAdapter createEventAdapter;
    private RecyclerView.LayoutManager recyclerViewCreateEventLayoutManager;
    private Button postEventButton;
    CreateEventFragment.ICreateEventFragmentAction mListener;
    private EditText addTitle;
    private EditText addLocation;
    private EditText addTime;
    private EditText addNote;



    public CreateEventFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreateEventFragment newInstance() {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
//            LoadGroups();


        }

    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICreateEventFragmentAction){
            this.mListener = (ICreateEventFragmentAction) context;
        }
        else{
            throw new RuntimeException(context.toString()+ "must implement PopulateMainFragment");
        }

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);

         LoadGroups();

        addTitle = rootView.findViewById(R.id.editEventTitle);
        addNote =  rootView.findViewById(R.id.createEventNotes);
        addTime =  rootView.findViewById(R.id.createEventTime);
        addLocation =  rootView.findViewById(R.id.createEventLocation);



        recyclerViewCreateEvent = rootView.findViewById(R.id.select_user_recycler_view);
        postEventButton = rootView.findViewById(R.id.post_event_button);

        recyclerViewCreateEventLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCreateEvent.setLayoutManager(recyclerViewCreateEventLayoutManager);

        createEventAdapter = new CreateEventAdapter(groups, this.getContext());
        recyclerViewCreateEvent.setAdapter(createEventAdapter);


        //posting event
        postEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = addTitle.getText().toString();
                String eventNote = addNote.getText().toString();
                String eventTime = addTime.getText().toString();
                String eventLocation = addLocation.getText().toString();
                int idx = createEventAdapter.getSelectedIndex();



                if (groups.size() == 0 || eventTitle.isEmpty() || eventLocation.isEmpty() || eventTime.isEmpty()){
                    Toast.makeText(getContext(), "Please add a title, location, time, and a group", Toast.LENGTH_SHORT).show();
                }

                else{

                    DocumentReference thisEventRef = db.collection("events").document();
                    Event event = new Event(eventTitle,
                            groups.get(idx),
                            eventLocation,
                            mUser.getUid(),
                            eventTime,
                            eventNote,
                            thisEventRef.getId());

                    createEvent(event, groups.get(idx));
                }

            }
        });


        return rootView;
    }

    private void LoadGroups() {
        groups = new ArrayList<>();
        db.collection("users")
                .document(mUser.getUid())
                .collection("groups")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
//                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
//                            retrieving all the elements from Firebase....
                            for(DocumentSnapshot document : value.getDocuments()){
                                Group group = document.toObject(Group.class);
                                groups.add(group);
                            }


                            createEventAdapter.setUsers(groups);
                            createEventAdapter.notifyDataSetChanged();
                        }



                    }
                });
    }

    private void createEvent(Event event, Group group) {
        //get users of group
        db.collection("groups")
                .document(group.getId())
                .collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                User user = document.toObject(User.class);

                                publishEvent(event, document.getId());
                            }

                        } else {
                            Toast.makeText(getContext(), "unable to create event", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        }


    private void publishEvent(Event event, String userId){


        WriteBatch batch = db.batch();

        DocumentReference eventToUser = db.collection("users")
                .document(userId)
                .collection("events")
                .document(event.getEventId());
        batch.set(eventToUser, event);

        DocumentReference eventToEvents = db.collection("events")
                .document(event.getEventId());
        batch.set(eventToEvents, event);


        // Commit the batch
        batch.commit().addOnCompleteListener(task -> {
            mListener.backToHomePage();
        });
    }


    public interface ICreateEventFragmentAction{
        void backToHomePage();

    }



}