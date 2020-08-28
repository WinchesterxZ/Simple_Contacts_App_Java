package com.example.contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.recyclerViewHolder> {
    List<ContactsInfo> contactsInfoList;
    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        public void onItemClick(int position);
        public void onAnotherItemClick(int position);
        public void onDeleteClick(int position);
    }

    public RecyclerViewAdapter(List<ContactsInfo> contactsInfoList) {
        this.contactsInfoList = contactsInfoList;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public static class recyclerViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView , phoneNumberTextView;
        ImageView imageView;
        LikeButton likeButton;
        ContactsInfo contactsInfo;
        public recyclerViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumber);
            imageView = itemView.findViewById(R.id.contact_image);
            likeButton = itemView.findViewById(R.id.icon_contact);
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
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAnotherItemClick(position);
                        }
                    }
                }
                @Override
                public void unLiked(LikeButton likeButton) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_raw,parent,false);
        return new recyclerViewHolder(view , onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewHolder holder, int position) {
        ContactsInfo contactsInfo = contactsInfoList.get(position);
        holder.nameTextView.setText(contactsInfo.getName());
        holder.imageView.setImageResource(contactsInfo.getImageId());
        holder.phoneNumberTextView.setText(contactsInfo.getPhoneNumber());
        holder.likeButton.setLiked(contactsInfo.getLikedState());
    }

    @Override
    public int getItemCount() {
        return contactsInfoList.size();
    }


}
