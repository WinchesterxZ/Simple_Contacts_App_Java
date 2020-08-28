package com.example.contacts;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.recyclerViewHolder> {
    List<ContactsInfo> contactsInfoList;
    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        public void onItemClick(int position);
        public void onDeleteClick(int position);
    }

    public FavoriteRecyclerViewAdapter(List<ContactsInfo> contactsInfoList) {
        this.contactsInfoList = contactsInfoList;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public static class recyclerViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView , phoneNumberTextView;
        ImageView imageView;
        ImageView deleteView;
        public recyclerViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_fav);
            phoneNumberTextView = itemView.findViewById(R.id.fav_phoneNumber);
            imageView = itemView.findViewById(R.id.fav_image);
            deleteView = itemView.findViewById(R.id.icon_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public recyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_recycler_raw,parent,false);
        return new recyclerViewHolder(view , onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewHolder holder, int position) {
        ContactsInfo contactsInfo = contactsInfoList.get(position);
        holder.nameTextView.setText(contactsInfo.getName());
        holder.imageView.setImageResource(contactsInfo.getImageId());
        holder.phoneNumberTextView.setText(contactsInfo.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactsInfoList.size();
    }


}
