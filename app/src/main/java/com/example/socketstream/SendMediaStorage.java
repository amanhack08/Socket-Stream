package com.example.socketstream;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    View v;
    SendSongsRecyclerView sendMediaStorage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.send_media_storage,container,false);
         sendMediaStorage=new SendSongsRecyclerView(getActivity());
         getLoaderManager().initLoader(0,null,this);
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.send_media_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(sendMediaStorage);
         return v;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

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
        sendMediaStorage.changeCursor(null);
    }
}
