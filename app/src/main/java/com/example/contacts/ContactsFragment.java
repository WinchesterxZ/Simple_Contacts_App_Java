package com.example.contacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<ContactsInfo> contactsInfoArrayList;
    ContactsInfo contactsInfo;
    String[] names = new String[] {"James" , "John" , "Robert" , "Michael" , "William" , "Joseph" , "Charles" , "Thomas" , "Anthony" , "Donald"};
    String[] numbers = new String[]{"+201100468211" , "+201100468251" , "+201100468212" , "+201100468254" , "+201100468287" , "+201100468278" , "+201100468285" , "+201100468282" , "+201100468274" , "+201100468297"};
    int [] imageId = new int[] {R.drawable.boy , R.drawable.girl , R.drawable.boy , R.drawable.girl , R.drawable.boy , R.drawable.girl , R.drawable.boy , R.drawable.girl , R.drawable.boy , R.drawable.girl};
    LikeButton likeButton;
    OnDataPassListener onDataPassListener;
    public interface OnDataPassListener{
        public void onDataPass(String s , String p , int i);
        public void onDataRemoved(String s ,String p);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
            try {
                onDataPassListener = (OnDataPassListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        loadData();
        if(contactsInfoArrayList==null) {
            fillArrayList();
        }
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_contacts);
        likeButton =(LikeButton) view.findViewById(R.id.icon_contact);
        setRecyclerView();

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ContactsInfo contactsInfo =contactsInfoArrayList.get(position);
                Toast.makeText(getActivity(), contactsInfo.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }

            public void onAnotherItemClick(int position) {
                ContactsInfo contactsInfo = contactsInfoArrayList.get(position);
                contactsInfo.setLikedState(true);
                String name = contactsInfo.getName();
                String phoneNumber = contactsInfo.getPhoneNumber();
                int imageD = contactsInfo.getImageId();
                onDataPassListener.onDataPass(name,phoneNumber,imageD);
            }
            @Override
            public void onDeleteClick(int position) {
                ContactsInfo contactsInfo = contactsInfoArrayList.get(position);
                contactsInfo.setLikedState(false);
                String name = contactsInfo.getName();
                String phoneNumber = contactsInfo.getPhoneNumber();
                onDataPassListener.onDataRemoved(name,phoneNumber);
            }
        });
        setMenuVisibility(true);
        return view;
    }

    public void setRecyclerView(){
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdapter = new RecyclerViewAdapter(contactsInfoArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    public void fillArrayList(){
        contactsInfoArrayList = new ArrayList<>();
        for(int i =0; i<names.length; i++){
            contactsInfoArrayList.add(new ContactsInfo(names[i],numbers[i],imageId[i],false));
        }
    }
    public void saveData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shareConPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json= gson.toJson(contactsInfoArrayList);
        editor.putString("conSaved",json);
        editor.apply();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shareConPref",Context.MODE_PRIVATE);
        Gson gson =new Gson();
        String json= sharedPreferences.getString("conSaved",null);
        Type type = new TypeToken<ArrayList<ContactsInfo>>() {}.getType();
        contactsInfoArrayList =gson.fromJson(json,type);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }
}