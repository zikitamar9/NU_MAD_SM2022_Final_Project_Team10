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

import com.example.groupproject4520kroo.Model.Group;

import java.util.ArrayList;

@Keep
public class GroupPreviewAdapter extends RecyclerView.Adapter<GroupPreviewAdapter.ViewHolder>{

    private ArrayList<Group> groups;
    private IGroupPreviewAdapterAction mListener;

    public GroupPreviewAdapter(ArrayList<Group> groups, Context context) {
        if(context instanceof IGroupPreviewAdapterAction){
            this.mListener = (IGroupPreviewAdapterAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement ICreateEventAdapterAction");
        }
        this.groups = groups;

    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventTitle;



        private ConstraintLayout clickGroupPreview;
        public TextView getLastSpeaker() {
            return eventTitle;
        }

        public ConstraintLayout getClickGroupPreview() {
            return clickGroupPreview;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.group_name_create_event);
            clickGroupPreview = itemView.findViewById(R.id.click_group_preview);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group_view_preview, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GroupPreviewAdapter.ViewHolder holder, int position) {
        if (groups.get(position) == null){
            holder.getLastSpeaker().setText("");
        }
        else{
            holder.getLastSpeaker().setText(groups.get(position).getName());

        }

        holder.getClickGroupPreview().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.previewToViewGroupFragment(groups.get(holder.getAbsoluteAdapterPosition()));
            }
        });

    }

    public interface IGroupPreviewAdapterAction{
        void previewToViewGroupFragment(Group group);
    }




}
