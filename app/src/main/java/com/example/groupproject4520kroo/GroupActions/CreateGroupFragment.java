package com.example.groupproject4520kroo.GroupActions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
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

import com.example.groupproject4520kroo.Model.Group;
import com.example.groupproject4520kroo.Model.User;
import com.example.groupproject4520kroo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;

@Keep
public class CreateGroupFragment extends Fragment {

    private FirebaseFirestore db;
    ArrayList<User> users;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText inputGroupName;
    private EditText inputUserName;
    private Button addUserButton;
    private Button createGroupButton;
    private RecyclerView recyclerViewCreateGroup;
    private CreateGroupAdapter createGroupAdapter;
    private RecyclerView.LayoutManager recyclerViewCreateGroupLayoutManager;
    ICreateGroupFragmentAction mListener;



    public CreateGroupFragment() {

    }


    public static CreateGroupFragment newInstance() {
        CreateGroupFragment fragment = new CreateGroupFragment();
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



        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICreateGroupFragmentAction){
            this.mListener = (ICreateGroupFragmentAction) context;
        }
        else{
            throw new RuntimeException(context.toString()+ "must implement PopulateMainFragment");
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        users = new ArrayList<>();

        recyclerViewCreateGroup = rootView.findViewById(R.id.view_group_users_recycler_view);
        recyclerViewCreateGroupLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCreateGroup.setLayoutManager(recyclerViewCreateGroupLayoutManager);
        createGroupAdapter = new CreateGroupAdapter(users, this.getContext());


        recyclerViewCreateGroup.setAdapter(createGroupAdapter);
        inputGroupName = rootView.findViewById(R.id.editGroupName);
        inputUserName = rootView.findViewById(R.id.editTextInputUserName);
        addUserButton = rootView.findViewById(R.id.addUserToGroupButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inputUserName.getText().toString().equals(mUser.getDisplayName())) {

                    stageUser(inputUserName.getText().toString());
                }
                else{
                    Toast.makeText(getContext(), "As the creator of this group, you are already added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createGroupButton = rootView.findViewById(R.id.createGroupButton1);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = inputGroupName.getText().toString();
                if (!groupName.equals("")) {
                    createGroup(users, groupName);
                }
                else{
                    Toast.makeText(getContext(), "please name this group", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    //creates the group by adding the users to a unique group under the "groups" collection
    //adds the group key to the user
    private void createGroup(ArrayList<User> users, String groupName) {
       User currentUser = new User(mUser.getUid(), mUser.getDisplayName());
        users.add(currentUser);
//        Get a new write batch
        WriteBatch batch = db.batch();

        DocumentReference thisGroupRef = db.collection("groups").document();

        Group group = new Group(groupName, thisGroupRef.getId(), mUser.getUid());
        batch.set(thisGroupRef, group);

        for (User user: users){
            DocumentReference groupToUserRef = thisGroupRef.collection("users").document(user.getUserId());
            batch.set(groupToUserRef, user);

            DocumentReference userToGroupRef = db.collection("users")
                    .document(user.getUserId())
                    .collection("groups")
                    .document(group.getId());

            batch.set(userToGroupRef, group);
        }



// Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mListener.backToHomePage();
            }
        });



    }


    //stages the user on the recyclerview
    private void stageUser(String username) {
        if (users.contains(username)){
            Toast.makeText(this.getActivity(),"user already added", Toast.LENGTH_SHORT);
        }
        else {

            db.collection("users")
                    .whereEqualTo("name", username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    User user = document.toObject(User.class);

                                    users.add(user);

                                }


                            } else {

                            }
                            createGroupAdapter.setUsers(users);
                            createGroupAdapter.notifyDataSetChanged();
                        }
                    });


//            Log.d("demo", "adding user"+ username+ "if exists");
////        Boolean isExisting;
//            db.collection("users")
//                    .document(username)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
////                                    users.add(username);
//                                    Log.d("demo", "DocumentSnapshot data: " + document.getId());
//                                    createGroupAdapter.setUsers(users);
//                                    createGroupAdapter.notifyDataSetChanged();
//                                    Log.d("demo", "adapter changed:" + users);
//                                } else {
//                                    Log.d("demo", "No such document");
//
//                                }
//                            } else {
//                                Log.d("demo", "get failed with ", task.getException());
//                            }
//                        }
//                    });

        }


    }



    public interface ICreateGroupFragmentAction{
        void backToHomePage();
    }


}