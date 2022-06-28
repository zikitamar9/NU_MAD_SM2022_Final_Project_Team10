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

import com.example.groupproject4520kroo.Model.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

@Keep
public class GroupPreviewFragment extends Fragment {


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView recyclerViewGroupPreview;
    private GroupPreviewAdapter groupPreviewAdapter;
    private RecyclerView.LayoutManager recyclerViewGroupListLayoutManager;
    FloatingActionButton addGroupButton;
    IGroupViewFragmentAction mListener;
    ArrayList<Group> groups;

    public GroupPreviewFragment() {
        // Required empty public constructor
    }


    public static GroupPreviewFragment newInstance() {
        GroupPreviewFragment fragment = new GroupPreviewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IGroupViewFragmentAction){
            this.mListener = (IGroupViewFragmentAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IChatAction");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_group_preview, container, false);

        LoadGroups();

        recyclerViewGroupPreview = rootView.findViewById(R.id.group_preview_recycler_view);
        recyclerViewGroupListLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewGroupPreview.setLayoutManager(recyclerViewGroupListLayoutManager);

        groupPreviewAdapter = new GroupPreviewAdapter(groups, this.getContext());
        recyclerViewGroupPreview.setAdapter(groupPreviewAdapter);

        groupPreviewAdapter.setGroups(groups);
        groupPreviewAdapter.notifyDataSetChanged();

        addGroupButton = rootView.findViewById(R.id.addGroupButton);

        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.toCreateNewGroup();
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
                            groupPreviewAdapter.setGroups(groups);
                            groupPreviewAdapter.notifyDataSetChanged();
                        }
                    }


                });
    }

    public interface IGroupViewFragmentAction{
        void toCreateNewGroup();
    }
}