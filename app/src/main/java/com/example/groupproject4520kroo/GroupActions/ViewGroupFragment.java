package com.example.groupproject4520kroo.GroupActions;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.groupproject4520kroo.EventPreviewAdapter;
import com.example.groupproject4520kroo.Model.Event;
import com.example.groupproject4520kroo.Model.Group;
import com.example.groupproject4520kroo.Model.User;
import com.example.groupproject4520kroo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.HashMap;

@Keep
public class ViewGroupFragment extends Fragment {

    IViewGroupFragmentAction mListener;
    private Button updateGroupButton;
    private EditText groupTitle;
    private EditText addUserName;
    private Button addUserButton;

    private static final String ARG_GROUP_ID = "groupId";
    private Group group;
    private RecyclerView recyclerViewCreateGroup;
    private CreateGroupAdapter createGroupAdapter;
    private RecyclerView.LayoutManager recyclerViewCreateGroupLayoutManager;
    private ArrayList<User> users;
//    private ArrayList<String> toAdd;
    private ArrayList<User> toRemove;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    public ViewGroupFragment() {
        // Required empty public constructor
    }

    public static ViewGroupFragment newInstance() {
        ViewGroupFragment fragment = new ViewGroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ViewGroupFragment newInstance(Group group) {
        ViewGroupFragment fragment = new ViewGroupFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP_ID, group);
        fragment.setArguments(args);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IViewGroupFragmentAction){
            this.mListener = (IViewGroupFragmentAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IChatAction");
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            if (args.containsKey(ARG_GROUP_ID)) {
                group = (Group) args.getSerializable(ARG_GROUP_ID);
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_view_group, container, false);
        updateGroupButton = rootView.findViewById(R.id.updateGroupButton);

        groupTitle = rootView.findViewById(R.id.editGroupName);

        LoadUsers2();

        recyclerViewCreateGroup = rootView.findViewById(R.id.view_group_users_recycler_view);
        recyclerViewCreateGroupLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCreateGroup.setLayoutManager(recyclerViewCreateGroupLayoutManager);


        createGroupAdapter = new CreateGroupAdapter(users, group.getAdminId(), mUser.getUid(), this.getContext());
        recyclerViewCreateGroup.setAdapter(createGroupAdapter);
//           createGroupAdapter.setUsers(users);
//           createGroupAdapter.notifyDataSetChanged();


        groupTitle = rootView.findViewById(R.id.editGroupName);
        groupTitle.setText(group.getName());

        addUserName = rootView.findViewById(R.id.editTextInputUserName);
        addUserButton = rootView.findViewById(R.id.addUserToGroupButton);


        if (group.getAdminId().equals(mUser.getUid())) {
            updateGroupButton.setVisibility(View.VISIBLE);
            addUserButton.setVisibility(View.VISIBLE);
            addUserName.setVisibility(View.VISIBLE);

            addUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stageUser(addUserName.getText().toString());

                }
            });

            updateGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!users.isEmpty()){
                        updateGroup();
                        mListener.backToHomePage();
                    }
                    else{
                        Toast.makeText(getContext(), "can not remove all users", Toast.LENGTH_LONG);
                    }

                }
            });

        }

        else{

            updateGroupButton.setVisibility(View.GONE);
            addUserButton.setVisibility(View.GONE);
            addUserName.setVisibility(View.GONE);
        }
        return rootView;

    }

    private void LoadUsers() {
        users = new ArrayList<>();
        db.collection("groups")
                .document(group.getId())
                .collection("users")
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

                        users.remove(mUser.getUid());
                        createGroupAdapter.setUsers(users);
                        createGroupAdapter.notifyDataSetChanged();
                    }
                });


    }

    private void LoadUsers2(){
        users = new ArrayList<>();
        db.collection("groups")
                .document(group.getId())
                .collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
//                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
//                            retrieving all the elements from Firebase....
                            for(DocumentSnapshot document : value.getDocuments()){
                                User user = document.toObject(User.class);

                                if (!user.getUserId().equals(mUser.getUid())){
                                    users.add(user);
                                }

                            }


                            createGroupAdapter.setUsers(users);
                            createGroupAdapter.notifyDataSetChanged();
                        }



                    }
                });
    }



    //stages the user on the recyclerview
    private void stageUser(String username) {

        if (users.contains(username)) {
            Toast.makeText(this.getActivity(), "user already added", Toast.LENGTH_SHORT);
        } else {
//        Boolean isExisting;
            db.collection("users")
                    .document(username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    User user = document.toObject(User.class);
                                    users.add(user);
                                    createGroupAdapter.setUsers(users);
                                    createGroupAdapter.notifyDataSetChanged();
//

                                } else {


                                }
                            } else {
                            }
                        }
                    });
        }
    }

    private void updateGroup() {

        WriteBatch batch = db.batch();

        DocumentReference thisGroupRef = db.collection("groups").document(group.getId());

        batch.update(thisGroupRef, "name", group.getName());

        ArrayList<User> updatedUsers = createGroupAdapter.getUsers();

        toRemove = createGroupAdapter.getRemoved();

        for (User user:toRemove) {

            DocumentReference groupRef = db.collection("groups")
                    .document(group.getId())
                    .collection("users")
                    .document(user.getUserId());
            batch.delete(groupRef);

            DocumentReference userRef = db.collection("users")
                    .document(user.getUserId())
                    .collection("groups")
                    .document(group.getId());
            batch.delete(userRef);

            }

        for (User user : createGroupAdapter.getUsers()) {


            if (!users.contains(users)){

            DocumentReference userRef = db.collection("groups")
                    .document(group.getId())
                    .collection("users")
                    .document(user.getUserId());
            batch.set(userRef, user);

            }
        }


        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "group updated", Toast.LENGTH_LONG);

            }
        });


    }

    public interface IViewGroupFragmentAction{
        void backToHomePage();
    }



    }
