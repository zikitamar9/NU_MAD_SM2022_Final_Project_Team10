package com.example.groupproject4520kroo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject4520kroo.EventActions.ViewEventFragment;
import com.example.groupproject4520kroo.Model.Event;

import java.util.ArrayList;

@Keep
public class EventPreviewAdapter extends RecyclerView.Adapter<EventPreviewAdapter.ViewHolder>{

    private ArrayList<Event> events;
    private IEventPreviewAdapterAction mListener;

    public EventPreviewAdapter(ArrayList<Event> events, Context context) {
        if(context instanceof IEventPreviewAdapterAction){
            this.mListener = (IEventPreviewAdapterAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement ICreateEventAdapterAction");
        }
        this.events = events;
    }



    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventTitle;


        private final TextView displayTime;
        private final TextView displayLocation;
        private final TextView displayGroup;

        private ConstraintLayout clickEventPreview;

        public TextView getEventTitle() {
            return eventTitle;
        }

        public TextView getTime() {
            return displayTime;
        }

        public TextView getLocation() {
            return displayLocation;
        }

        public TextView getGroupName() {
            return displayGroup;
        }


        public ConstraintLayout getClickEventPreview() {
            return clickEventPreview;
        }




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.group_name_create_event);
            displayGroup = itemView.findViewById(R.id.displayGroup);
            displayTime = itemView.findViewById(R.id.displayTime);
            displayLocation = itemView.findViewById(R.id.displayLocation);
            clickEventPreview = itemView.findViewById(R.id.click_group_preview);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_event_view_preview, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EventPreviewAdapter.ViewHolder holder, int position) {
        if (events.get(position) == null){
            holder.getEventTitle().setText("");
        }
        else{
            holder.getEventTitle().setText(events.get(position).getName());
            holder.getGroupName().setText(events.get(position).getGroup().getName());
            holder.getLocation().setText(events.get(position).getLocation());
            holder.getTime().setText(events.get(position).getTime());
        }

        holder.getClickEventPreview().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.previewToViewEventFragment(events.get(holder.getAbsoluteAdapterPosition()));
            }
        });

    }

    public interface IEventPreviewAdapterAction{
        void previewToViewEventFragment(Event event);
    }

}
