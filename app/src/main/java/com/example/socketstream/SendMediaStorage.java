package com.example.socketstream;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.recyclerView.SendSongsRecyclerView;

public class SendMediaStorage extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //fragment view
    View v;

    //make a recycler view instance
    SendSongsRecyclerView sendMediaStorage;

    //get send button
    Button sendButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.send_media_storage,container,false);

         //create an instance of recycler view
         sendMediaStorage=new SendSongsRecyclerView(getActivity());

         //intialize the loader
         getLoaderManager().initLoader(0,null,this);

         //create a recycler view object
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.send_media_recycler_view);

        //set layout to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //set adapter to recycler view
        recyclerView.setAdapter(sendMediaStorage);
         return v;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        //using the concept of asynck task loader
        return new CursorLoader(getActivity().getApplicationContext()
                ,MediaStore.Audio.Media.getContentUri("external")
                ,null
                ,null
                ,null
                ,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        sendMediaStorage.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //when cursor changes
        sendMediaStorage.changeCursor(null);
    }
}
