package com.example.socketstream;

import android.content.ContentResolver;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class SendFilesViewPager extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;


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


    }

    private void initializeAllObjects(){
        viewPager=(ViewPager)findViewById(R.id.sendLayoutViewPager);
        tabLayout=(TabLayout) findViewById(R.id.sendtablayout);
    }

    public ContentResolver getResolver(){
        return this.getContentResolver();
    }

}
