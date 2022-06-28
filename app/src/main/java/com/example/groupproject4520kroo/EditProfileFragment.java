package com.example.groupproject4520kroo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;


@Keep
public class EditProfileFragment extends Fragment {

    private TextView userDisplayName;
    private EditText changeNameInput;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private Button changeNameButton;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public EditProfileFragment(String profilePicUri) {

    }


    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static EditProfileFragment newInstance(String profilePicUri) {
        EditProfileFragment fragment = new EditProfileFragment(profilePicUri);
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
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        changeNameButton = view.findViewById(R.id.EditFragmentChangeNameButton);
        changeNameInput = view.findViewById(R.id.EditProfileChangeNameEditText);
        userDisplayName = view.findViewById(R.id.CurrentNameView);
        userDisplayName.setText("Name: "+ mUser.getDisplayName());


        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newNameInput = changeNameInput.getText().toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newNameInput)
                        .build();

                mUser.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    userDisplayName.setText("Name: "+ mUser.getDisplayName());
                                }
                            }
                        });

                WriteBatch batch = db.batch();

                DocumentReference userRef = db.collection("users")
                        .document(mUser.getUid());
                batch.update(userRef, "name", newNameInput);


                // Commit the batch
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "name updated", Toast.LENGTH_LONG);

                    }
                });






            }
        });





        return view;
    }



}