package com.example.socketstream;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.recyclerView.SendFilesRecyclerView;

import java.io.File;
import java.util.ArrayList;

public class SendFilesStorage extends Fragment {
    View v;
   // int count=0;
    ArrayList<File> filesUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.send_files_storage_fragment, container, false);
        filesUri=new ArrayList<>();
        getFilesFromStorage(Environment.getExternalStorageDirectory());

        //Toast.makeText(getActivity(), ""+count, Toast.LENGTH_SHORT).show();

        SendFilesRecyclerView sendFilesRecyclerView=new SendFilesRecyclerView(filesUri,getActivity());
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.send_file_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),5));
        recyclerView.setAdapter(sendFilesRecyclerView);
        return v;
    }


    public void getFilesFromStorage(File dir){

        String pdfPattern = ".pdf";
        String docPattern=".docx";
        String zipPattern=".zip";
        String pptPattern=".pptx";
        String odtPattern=".odt";

        File listFile[] = dir.listFiles();
        

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getFilesFromStorage(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)||
                            listFile[i].getName().endsWith(docPattern)||
                            listFile[i].getName().endsWith(zipPattern)||
                            listFile[i].getName().endsWith(pptPattern)
                    ||listFile[i].getName().endsWith(odtPattern)){

                       filesUri.add(listFile[i]);
                    }
                }
            }
        }

    }

}
