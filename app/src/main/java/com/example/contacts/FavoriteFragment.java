package com.example.contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FavoriteRecyclerViewAdapter favoriteRecyclerViewAdapter;
    ArrayList<ContactsInfo> contactsInfoArrayList;
    OnData2PassListener onData2PassListener;
    ContactsInfo contactsInfo;
    public interface OnData2PassListener{
        public void onData2Pass(String s , String p , int id);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.fav_recycler);
        loadData();
        return view;

    }
    public void removeItem(int position){
        contactsInfoArrayList.remove(position);
        favoriteRecyclerViewAdapter.notifyItemRemoved(position);
    }
    public void setRecyclerView(){
            layoutManager = new LinearLayoutManager(getActivity());
            favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(contactsInfoArrayList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(favoriteRecyclerViewAdapter);
            favoriteRecyclerViewAdapter.setOnItemClickListener(new FavoriteRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    ContactsInfo contactsInfo = contactsInfoArrayList.get(position);
                    Toast.makeText(getActivity(), contactsInfo.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDeleteClick(int position) {
                    ContactsInfo contactsInfo = contactsInfoArrayList.get(position);
                    String name = contactsInfo.getName();
                    String phone = contactsInfo.getPhoneNumber();
                    int id = contactsInfo.getImageId();
                    removeItem(position);
                    onData2PassListener.onData2Pass(name, phone, id);
                }
            });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnData2PassListener){
            onData2PassListener = (OnData2PassListener)context;
        }
        else{
            throw new RuntimeException(context.toString()+ " Must Implement OnDataPassListener");
        }
    }
    public void loadData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharePref",Context.MODE_PRIVATE);
        Gson gson =new Gson();
        String json= sharedPreferences.getString("favSaved",null);
        Type type = new TypeToken<ArrayList<ContactsInfo>>() {}.getType();
        contactsInfoArrayList =gson.fromJson(json,type);
        if(contactsInfoArrayList==null){
            contactsInfoArrayList = new ArrayList<>();
        }
    }
    public void saveData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharePref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json= gson.toJson(contactsInfoArrayList);
        editor.putString("favSaved",json);
        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(contactsInfoArrayList!=null) {
            setRecyclerView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }
}