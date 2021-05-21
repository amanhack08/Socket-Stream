package com.example.socketstream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socketstream.recyclerView.SendImageRecyclerView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URLConnection;
import java.util.ArrayList;

public class SendPhotosGallery extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    ContentResolver cr;
    SendFilesViewPager sendFilesViewPager;
    ArrayList<Bitmap> imageUri;
    RecyclerView recyclerView;
    SendImageRecyclerView imageAdapter;
    View v;
    Button sendButton;
    ArrayList<Uri> sendArrayListImages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.send_photos_gallery, container, false);

        cr = getActivity().getContentResolver();
        imageUri = new ArrayList<Bitmap>();

        sendButton=(Button)v.findViewById(R.id.send_button_image_fragment_view);


//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendArrayListImages=SendImageRecyclerView.getArrayListUri();
//                Intent intent=new Intent( getActivity(),PermissionRequiredTransfer.class);
//                intent.setData(Uri.parse("9"));
//                intent.putExtra("mylist",sendArrayListImages);
//                startActivity(intent);
//            }
//        });
        getLoaderManager().initLoader(0, null, this);
        recyclerView=(RecyclerView)v.findViewById(R.id.send_image_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),3));
        imageAdapter=new SendImageRecyclerView(getActivity());
        recyclerView.setAdapter(imageAdapter);


        return v;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
               + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        return new CursorLoader(getActivity().getApplicationContext(),
                MediaStore.Files.getContentUri("external"),
                null,selection,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
       imageAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
       imageAdapter.changeCursor(null);
    }





}
