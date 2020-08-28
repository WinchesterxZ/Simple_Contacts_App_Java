package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements ContactsFragment.OnDataPassListener , FavoriteFragment.OnData2PassListener {
    Toolbar toolbar;
    final static int STORAGE_PERMISSION_CODE = 1;
    final static int CONTACTS_PERMISSION_CODE = 1;
    FragmentAdapter fragmentAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    FavoriteFragment favoriteFragment = new FavoriteFragment();
    ContactsFragment contactsFragment = new ContactsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager =(ViewPager)findViewById(R.id.view_pager);
        setTabLayout();
    }

    public void setTabLayout(){
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        fragmentAdapter.addFragment(contactsFragment,"Contacts");
        fragmentAdapter.addFragment(favoriteFragment,"Favorite");
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rq_per1:
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You Have Already Granted This Permission!", Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission();
                }
                return true;
            case R.id.rq_per2:
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You Have Already Granted This Permission!", Toast.LENGTH_SHORT).show();
                }
                else{
                    requestContactsPermission();
                }
                return true;
            case R.id.settings:
                checkPermissions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void checkPermissions(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }
    public void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This Permission Is Needed To Access File Storage")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

        }
    }
    public void requestContactsPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Denied")
                    .setMessage("This Permission Is Needed To Access Contacts")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},CONTACTS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();

        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},CONTACTS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == CONTACTS_PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDataPass(String s, String p, int i) {
        favoriteFragment.contactsInfoArrayList.add(new ContactsInfo(s,p,i));
        favoriteFragment.setRecyclerView();
    }

    @Override
    public void onDataRemoved(String s, String p) {
        for(int i=0;i<favoriteFragment.contactsInfoArrayList.size();i++){
        favoriteFragment.contactsInfo = favoriteFragment.contactsInfoArrayList.get(i);
        String name = favoriteFragment.contactsInfo.getName();
        String phone =favoriteFragment.contactsInfo.getPhoneNumber();
        if(name.equals(s) && phone.equals(p)){
            favoriteFragment.contactsInfoArrayList.remove(i);
            favoriteFragment.favoriteRecyclerViewAdapter.notifyItemRemoved(i);
        }
        }
    }

    @Override
    public void onData2Pass(String s, String p , int id) {
        for (int i = 0; i < contactsFragment.contactsInfoArrayList.size(); i++) {
            ContactsInfo contactsInfo = contactsFragment.contactsInfoArrayList.get(i);
            String name = contactsInfo.getName();
            String phone = contactsInfo.getPhoneNumber();
            if (name.equals(s) && phone.equals(p)) {
                contactsFragment.contactsInfoArrayList.remove(i);
                contactsFragment.recyclerViewAdapter.notifyItemRemoved(i);
                contactsFragment.contactsInfoArrayList.add(i,new ContactsInfo(s,p,id,false));
                contactsFragment.recyclerViewAdapter.notifyItemInserted(i);
            }
        }
    }


}