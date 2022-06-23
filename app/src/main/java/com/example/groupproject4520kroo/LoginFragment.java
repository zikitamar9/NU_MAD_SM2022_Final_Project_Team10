package com.example.groupproject4520kroo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonOpenRegister;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private IloginFragmentAction mListener;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextPassword = rootView.findViewById(R.id.editTextPassword);
        buttonLogin = rootView.findViewById(R.id.buttonLogin);
        buttonOpenRegister = rootView.findViewById(R.id.buttonOpenRegister);
        buttonLogin.setOnClickListener(this);
        buttonOpenRegister.setOnClickListener(this);
        
        
        
        
        
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonLogin){
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            if(email.equals("")){
                editTextEmail.setError("Must input email!");
            }
            if(password.equals("")){
                editTextPassword.setError("Password must not be empty!");
            }
            if(!email.equals("") && !password.equals("")){
//                    Sign in to the account....
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
//                            Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Login Failed!"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mListener.populateMainFragment(mAuth.getCurrentUser());

                                    db = FirebaseFirestore.getInstance();
                                    mAuth = FirebaseAuth.getInstance();
                                    mUser = mAuth.getCurrentUser();

                                    mUser = task.getResult().getUser();
                                    User user = new User(mUser.getEmail());
                                    db.collection("users")
                                            .document(mUser.getEmail())
                                            .set(user);


                                }
                            }
                        });
            }

        }else if(view.getId()== R.id.buttonOpenRegister){
            mListener.populateRegisterFragment();
        }
    }

    public interface IloginFragmentAction {
        void populateMainFragment(FirebaseUser mUser);
        void populateRegisterFragment();
    }
}