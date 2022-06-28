package com.example.groupproject4520kroo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.groupproject4520kroo.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;


@Keep
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private EditText editTextName, editTextEmail, editTextPassword, editTextRepPassword;
    private Button buttonRegister;
    private Button buttonOpenPhoto;
    private String name, email, password, rep_password;
    private IRegisterFragmentAction mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IRegisterFragmentAction){
            this.mListener = (IRegisterFragmentAction) context;
        }else{
            throw new RuntimeException(context
                    + "must implement RegisterRequest");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        editTextName = rootView.findViewById(R.id.editTextRegister_Name09);
        editTextEmail = rootView.findViewById(R.id.editTextRegister_Email09);
        editTextPassword = rootView.findViewById(R.id.editTextRegister_Password09);
        editTextRepPassword = rootView.findViewById(R.id.editTextRegister_Rep_Password09);
        buttonRegister = rootView.findViewById(R.id.registerFragmentRegisterButton09);

        buttonRegister.setOnClickListener(this);

//

        return rootView;

    }

    @Override
    public void onClick(View view) {

//        if(view.getId()== R.id.buttonOpenPhoto09) {
//            mListener.openCamera();
//
//        }

        if(view.getId()== R.id.registerFragmentRegisterButton09) {

            this.name = String.valueOf(editTextName.getText()).trim();
            this.email = String.valueOf(editTextEmail.getText()).trim();
            this.password = String.valueOf(editTextPassword.getText()).trim();
            this.rep_password = String.valueOf(editTextRepPassword.getText()).trim();

            try{
                if (name.equals("")) {
                    throw new Exception("blank_input");
                }
                if (email.equals("")) {
                    throw new Exception("blank_input");
                }
                if (password.equals("")) {
                    throw new Exception("blank_input");
                }
                if (!rep_password.equals(password)) {
                    throw new Exception("password_no_match");
                }

                if (!name.equals("") && !email.equals("")
                        && !password.equals("")
                        && rep_password.equals(password)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        db = FirebaseFirestore.getInstance();
                                        mAuth = FirebaseAuth.getInstance();
                                        mUser = mAuth.getCurrentUser();

                                        mUser = task.getResult().getUser();

                                        User user = new User(mUser.getUid(), name);

                                        db.collection("users")
                                                .document(mUser.getUid())
                                                .set(user);


//                                    Adding name to the FirebaseUser...
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();

                                        mUser.updateProfile(profileChangeRequest)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mListener.registerDone(mUser);
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Please ensure that all fields are correct", Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                }
            }
            catch (Exception e) {
                if (e.getMessage().equals("Please fill out all fields!")) {
                    Toast.makeText(getContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                } else if (e.getMessage().equals("password_no_match")) {
                    Toast.makeText(getContext(), "Passwords must match!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "please fill in the fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public interface IRegisterFragmentAction {
        void registerDone(FirebaseUser mUser);
    }


}