package com.example.groupproject4520kroo.GroupActions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject4520kroo.Model.Group;
import com.example.groupproject4520kroo.Model.User;
import com.example.groupproject4520kroo.R;

import java.util.ArrayList;
@Keep
public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.ViewHolder>{


    private ICreateGroupAdapterAction mListener;
    private ArrayList<User> users;
    private ArrayList<User> toRemove;
    private ArrayList<String> toAdd;
    private String admin;
    private String mUserId;
    private Boolean viewGroup;

    public ArrayList<String> getToAdd() {
        return toAdd;
    }

    public void setToAdd(ArrayList<String> toAdd) {
        this.toAdd = toAdd;
    }


    public ArrayList<User> getRemoved() {
        return toRemove;
    }

    public void setRemoved(ArrayList<User> toRemove) {
        this.toRemove = toRemove;
    }


    public CreateGroupAdapter(ArrayList<User> users, Context context) {
        this.toRemove = new ArrayList<>();
        this.toAdd = new ArrayList<>();
        this.users = users;
        this.viewGroup = false;
    }

    public CreateGroupAdapter(ArrayList<User> users, String admin, String mUserId, Context context) {
        this.toRemove = new ArrayList<>();
        this.toAdd = new ArrayList<>();
        this.users = users;
        this.admin = admin;
        this.mUserId = mUserId;
        this.viewGroup = true;

        if(context instanceof ICreateGroupAdapterAction){

            this.mListener = (ICreateGroupAdapterAction) context;

        }else{
            throw new RuntimeException(context.toString()+ "must implement ICreateEventAdapterAction");
        }

    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView userTitle;
        private ImageButton removeUserButton;

        public ImageButton getRemoveUserButton() {
            return removeUserButton;
        }


//        private final ConstraintLayout clickUser;


        public TextView getUserTitle() {
            return userTitle;
        }

//        public ConstraintLayout getClickUser() {
//            return clickUser;
//        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTitle = itemView.findViewById(R.id.group_name_create_event);
            removeUserButton = itemView.findViewById(R.id.removeUserCreateGroup);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user_view_create_group, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CreateGroupAdapter.ViewHolder holder, int position) {
        if (users.get(position) == null){
            holder.getUserTitle().setText("");
        }
        else{
            holder.getUserTitle().setText(users.get(position).getName());

        }

        if (viewGroup) {
            if (!admin.equals(mUserId)) {
                holder.getRemoveUserButton().setVisibility(View.INVISIBLE);
            } else {
                holder.getRemoveUserButton().setVisibility(View.VISIBLE);
            }
        }

        holder.getRemoveUserButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "removed user", Toast.LENGTH_LONG).show();
                toRemove.add(users.get(holder.getAbsoluteAdapterPosition()));
                users.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
            }
        });


    }

    public interface ICreateGroupAdapterAction{
        void backToHomePage();
    }

}
