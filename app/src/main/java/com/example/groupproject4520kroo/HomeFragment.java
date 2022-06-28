package com.example.groupproject4520kroo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groupproject4520kroo.EventActions.CreateEventFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.components.Lazy;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

@Keep
public class HomeFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageButton logout;
    private FirebaseUser mUser;
    private IHomeFragmentAction mListener;
    private TextView welcomeText;
    private ImageButton editProfile;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IHomeFragmentAction){
            this.mListener = (IHomeFragmentAction) context;
        }
        else{
            throw new RuntimeException(context.toString()+ "must implement PopulateMainFragment");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        welcomeText =  rootView.findViewById(R.id.welcomeText);

        welcomeText.setText("Welcome "+ mUser.getDisplayName()+"!");

        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager2 = rootView.findViewById(R.id.view_pager_home_page_tabs);
        myViewPagerAdapter = new MyViewPagerAdapter(this.getActivity());
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


        logout = rootView.findViewById(R.id.logOutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.logoutPressed();
            }
        });


        editProfile = rootView.findViewById(R.id.goToEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.goToEditProfile();
            }
        });

        return rootView;
    }

    public interface IHomeFragmentAction {
        void logoutPressed();
        void goToEditProfile();
    }

}