package com.example.groupproject4520kroo.EventActions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.groupproject4520kroo.Model.Event;
import com.example.groupproject4520kroo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
@Keep
public class ViewEventFragment extends Fragment {

    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventTime;
    private TextView eventNote;
    private TextView eventGroup;
    private Button homeButton;

    private static final String ARG_EVENT_ID = "eventId";
    private Event event;
    IViewEventFragmentAction mListener;
    private FloatingActionButton editButton;

    public ViewEventFragment() {
        // Required empty public constructor
    }

    public static ViewEventFragment newInstance() {
        ViewEventFragment fragment = new ViewEventFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public static ViewEventFragment newInstance(Event event) {
        ViewEventFragment fragment = new ViewEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_ID, event);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IViewEventFragmentAction){
            this.mListener = (IViewEventFragmentAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement IChatAction");
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            if (args.containsKey(ARG_EVENT_ID)) {
                event = (Event) args.getSerializable(ARG_EVENT_ID);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_view_event, container, false);

        editButton = rootView.findViewById(R.id.updateGroupButton);

        eventTitle = rootView.findViewById(R.id.viewEventTitle);
        eventTitle.setText(event.getName());

        eventNote = rootView.findViewById(R.id.viewEventNotes);
        eventNote.setText(event.getNotes());

        eventTime = rootView.findViewById(R.id.viewEventTime);
        eventTime.setText(event.getTime());

        eventLocation = rootView.findViewById(R.id.viewEventLocation);
        eventLocation.setText(event.getLocation());

        eventGroup = rootView.findViewById(R.id.groupTitle);
        eventGroup.setText(event.getGroup().getName());

        homeButton = rootView.findViewById(R.id.backToHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToHomePage();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toEditEventFragment(event);
            }
        });

        return rootView;

    }


    public interface IViewEventFragmentAction {
        void toEditEventFragment(Event event);
        void backToHomePage();
    }
}