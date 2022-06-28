package com.example.groupproject4520kroo;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.groupproject4520kroo.EventActions.CreateEventAdapter;
import com.example.groupproject4520kroo.EventActions.CreateEventFragment;
import com.example.groupproject4520kroo.EventActions.EditEventFragment;
import com.example.groupproject4520kroo.EventActions.ViewEventFragment;
import com.example.groupproject4520kroo.GroupActions.CreateGroupAdapter;
import com.example.groupproject4520kroo.GroupActions.CreateGroupFragment;
import com.example.groupproject4520kroo.GroupActions.EditGroupFragment;
import com.example.groupproject4520kroo.GroupActions.ViewGroupFragment;
import com.example.groupproject4520kroo.Model.Event;
import com.example.groupproject4520kroo.Model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;

@Keep
public class MainActivity extends AppCompatActivity implements
        LoginFragment.IloginFragmentAction,
        GroupPreviewFragment.IGroupViewFragmentAction,
        CreateGroupFragment.ICreateGroupFragmentAction,
        CreateEventFragment.ICreateEventFragmentAction,
        EventPreviewFragment.IEventViewFragmentAction,
        CreateEventAdapter.ICreateEventAdapterAction,
        CreateGroupAdapter.ICreateGroupAdapterAction,
        EventPreviewAdapter.IEventPreviewAdapterAction,
        ViewEventFragment.IViewEventFragmentAction,
        EditEventFragment.IEditEventFragmentAction,
        ViewGroupFragment.IViewGroupFragmentAction,
        EditGroupFragment.IEditGroupFragmentAction ,
        GroupPreviewAdapter.IGroupPreviewAdapterAction,
        RegisterFragment.IRegisterFragmentAction,
        HomeFragment.IHomeFragmentAction {

    private FrameLayout containerRoot;
    private FirebaseStorage storage;
    private User user;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private boolean auth;
    private String chatId;

    //checking network connection
    private static boolean isConnected(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isConnected(MainActivity.this)) {
            mAuth = FirebaseAuth.getInstance();
            storage = FirebaseStorage.getInstance();

        }
        else{
            Toast.makeText(MainActivity.this, "no internet connection", Toast.LENGTH_LONG);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        populateScreen();
    }


    private void populateScreen() {
        //      Check for Authenticated users ....
        if (currentUser != null) {
            //The user is authenticated, Populating The Main Fragment....
            Toast.makeText(MainActivity.this, "Loading Profile and Messages...", Toast.LENGTH_LONG);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.MainFragmentContainer, HomeFragment.newInstance(), "mainFragment")
                    .commit();

        } else {
            db = FirebaseFirestore.getInstance();
//            The user is not logged in, load the login Fragment....
            Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_LONG);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.MainFragmentContainer, LoginFragment.newInstance(), "loginFragment")
                    .commit();
        }
    }


    @Override
    public void populateMainFragment(FirebaseUser mUser) {
        this.currentUser = mUser;
        db = FirebaseFirestore.getInstance();
        populateScreen();
    }


    @Override
    public void toCreateNewGroup() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, CreateGroupFragment.newInstance(), "createGroup")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backToHomePage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, HomeFragment.newInstance(), "createGroup")
                .commit();
    }

    @Override
    public void toCreateNewEvent() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, CreateEventFragment.newInstance(), "createGroup")
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void previewToViewEventFragment(Event event) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, ViewEventFragment.newInstance(event), "createGroup")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void toEditEventFragment(Event event) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, EditEventFragment.newInstance(event), "createGroup")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void editToViewEventFragment(Event event) {
        Fragment fragment = ViewEventFragment.newInstance(event);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, fragment, "createGroup")
                .commit();

    }

    @Override
    public void previewToViewGroupFragment(Group group) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, ViewGroupFragment.newInstance(group), "createGroup")
                .addToBackStack(null)
                .commit();
    }
//
//    @Override
//    public void toEditGroupFragment() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.MainFragmentContainer, EditGroupFragment.newInstance(), "createGroup")
//                .addToBackStack(null)
//                .commit();
//    }



    @Override
    public void editToViewGroupFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, ViewGroupFragment.newInstance(), "createGroup")
                .commit();

    }

    @Override
    public void populateRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, RegisterFragment.newInstance(), "registerFragment")
                .commit();
    }

    @Override
    public void logoutPressed() {
        mAuth.signOut();
        currentUser = null;
        populateScreen();
    }

    @Override
    public void goToEditProfile() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragmentContainer, EditProfileFragment.newInstance(), "registerFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerDone(FirebaseUser mUser) {
        this.currentUser = mUser;
        populateScreen();
    }
}