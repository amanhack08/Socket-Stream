package com.example.socketstream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SendFragmentPageAdapter extends FragmentPagerAdapter {


    public SendFragmentPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SendAppsStorage();
            case 1:
                return new SendFilesStorage();
            case 2:
                return new SendVideosGalley();
            case 3:
                return new SendMediaStorage();
            case 4:
                return new SendPhotosGallery();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "App";
            case 1:
                return "File";
            case 2:
                return "Video";
            case 3:
                return "Song";
            case 4:
                return "Photo";
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 5;
    }
}
