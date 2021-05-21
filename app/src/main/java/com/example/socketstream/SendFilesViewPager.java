package com.example.socketstream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SendFilesViewPager extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Uri> getAllUri;
    Button sendButton;
    public static String VIDEOS="videos";
    public static  String IMAGES="images";
    public  static  String APPS="apps";
    public static String FILES="files";
    public static String MUSIC="music";
    public  static  String SELECT_ITEMS="selected items";
    //use shared preference to store the selected files of every fragment
    static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_files_view_pager_activity);

        /*
        initialize all objects
         */
        initializeAllObjects();

        tabLayout.setupWithViewPager(viewPager);

        PagerAdapter fragmentPageAdapter=new SendFragmentPageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentPageAdapter);


        //set on click listner to send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all selected videos Uri
                ArrayList<Uri> getVideos=getAllUriValues(VIDEOS);
                //get all selected apps Uri
                ArrayList<Uri> getApps=getAllUriValues(APPS);
                //get all selected images Uri
                ArrayList<Uri> getImages=getAllUriValues(IMAGES);
                //get all selected Files Uri
                ArrayList<Uri> getFiles=getAllUriValues(FILES);
                //get all selected Songs Uri
                ArrayList<Uri> getSongs=getAllUriValues(MUSIC);
                //here i have to do work
                for(int i=0;i<getVideos.size();i++){
                    getAllUri.add(getVideos.get(i));
                }
                for(int i=0;i<getApps.size();i++){
                    getAllUri.add(getApps.get(i));
                }
                for(int i=0;i<getImages.size();i++){
                    getAllUri.add(getImages.get(i));
                }
                for(int i=0;i<getSongs.size();i++){
                    getAllUri.add(getSongs.get(i));
                }
                for(int i=0;i<getFiles.size();i++){
                    getAllUri.add(getFiles.get(i));
                }
                clearSharedPreference();
                //here is to use send button to send arraylist to permission required transfer activity
                Intent intent=new Intent(getApplicationContext(),PermissionRequiredTransfer.class);
                intent.setData(Uri.parse("9"));
                intent.putExtra("mylist",getAllUri);

                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Hi There "+Integer.toString(getAllUri.size()),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.progressDialog1!=null||MainActivity.progressDialog1.isShowing()){
            MainActivity.progressDialog1.dismiss();
        }
    }

    //editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));for arraylist
    private void initializeAllObjects(){
        viewPager=(ViewPager)findViewById(R.id.sendLayoutViewPager);
        tabLayout=(TabLayout) findViewById(R.id.sendtablayout);
        getAllUri=new ArrayList<>();
        sendButton=(Button)findViewById(R.id.send_button_main_fragment_view);
        sharedPreferences=getSharedPreferences(SELECT_ITEMS, Context.MODE_PRIVATE);
    }

    //function to get the uri arraylist for specific fragment
    public ArrayList<Uri> getAllUriValues(String val){
        return getArrayList(val,sharedPreferences);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearSharedPreference();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clearSharedPreference();
    }

    private void clearSharedPreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public static void saveArrayList(ArrayList<String> list, String key, SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public static ArrayList<Uri> getArrayList(String key,SharedPreferences sharedPreferences){

        Gson gson = new Gson();
        String json =sharedPreferences.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList=gson.fromJson(json, type);
        ArrayList<Uri> uris=new ArrayList<>();
        if(arrayList!=null) {
            for (int i = 0; i < arrayList.size(); i++) {
                uris.add(Uri.parse(arrayList.get(i)));
            }
        }
        return uris;
    }
    public ContentResolver getResolver(){
        return this.getContentResolver();
    }

}
