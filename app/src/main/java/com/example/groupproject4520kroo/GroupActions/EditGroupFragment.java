package com.example.groupproject4520kroo.GroupActions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupproject4520kroo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

@Keep
public class EditGroupFragment extends Fragment {

    private IEditGroupFragmentAction mListener;
    private FloatingActionButton editButton;



    public EditGroupFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditGroupFragment newInstance() {
        EditGroupFragment fragment = new EditGroupFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IEditGroupFragmentAction){
            this.mListener = (IEditGroupFragmentAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IChatAction");
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_edit_group, container, false);


        editButton = rootView.findViewById(R.id.updateGroupButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editToViewGroupFragment();
            }
        });




        return rootView;
    }


    public interface IEditGroupFragmentAction {
        void editToViewGroupFragment();
    }


}