package com.example.groupproject4520kroo.EventActions;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject4520kroo.Model.Group;
import com.example.groupproject4520kroo.R;

import java.util.ArrayList;
@Keep
public class CreateEventAdapter extends RecyclerView.Adapter<CreateEventAdapter.ViewHolder>{

    private int selectedIndex;
    private View prevElement;

    private ICreateEventAdapterAction mListener;
//    private
    private ArrayList<Group> groups;

    public CreateEventAdapter(ArrayList<Group> groups, Context context) {
        this.groups = groups;
        if(context instanceof ICreateEventAdapterAction){
            this.mListener = (ICreateEventAdapterAction) context;
        }else{
            throw new RuntimeException(context.toString()+ "must implement ICreateEventAdapterAction");
        }

    }

    public int getSelectedIndex() {
        return selectedIndex;
    }


    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setUsers(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView userTitle;


        private ConstraintLayout selectGroup;

        public ConstraintLayout getSelectGroup() {
            return selectGroup;
        }
        public TextView getUserTitle() {
            return userTitle;
        }


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTitle = itemView.findViewById(R.id.group_name_create_event);
            selectGroup = itemView.findViewById(R.id.click_group_preview);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group_view_create_event, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CreateEventAdapter.ViewHolder holder, int position) {
        if (groups.get(position) == null){
            holder.getUserTitle().setText("");
        }
        else{
            holder.getUserTitle().setText(groups.get(position).getName());
        }


        holder.getSelectGroup().setOnClickListener(v -> {
            // CODE TO INSERT
            if (prevElement != null)
                prevElement.setBackgroundColor(Color.YELLOW);
            v.setBackgroundColor(Color.GREEN);
            prevElement = v;
            selectedIndex = holder.getAbsoluteAdapterPosition();
        });


    }

    public interface ICreateEventAdapterAction{
        void backToHomePage();
    }

}
