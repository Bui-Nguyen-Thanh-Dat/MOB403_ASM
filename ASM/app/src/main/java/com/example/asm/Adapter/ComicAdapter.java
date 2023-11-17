package com.example.asm.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.MainActivity;
import com.example.asm.Model.Comic;
import com.example.asm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {
    List<Comic> comicList;
    private MainActivity mainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }



    public ComicAdapter(List<Comic> comicList) {
        this.comicList = comicList;
    }
    public void setComicList(List<Comic> comicList) {
        this.comicList = comicList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comic comic=comicList.get(position);
        holder.titleTextView.setText(comic.getTitle());
        holder.tvname.setText(String.valueOf(comic.getName()));
        holder.tvchapter.setText(comic.getChapter());

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   mainActivity.deleteComic(comic.getId());
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   mainActivity.editComic(comic.getId(),comic);
            }
        });

        if (!comic.getImage().trim().equals("")) {
            Picasso.get().load(comic.getImage()).into(holder.thumbnailImageView);
        }


    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvname,tvchapter;
        public TextView titleTextView;
        public ImageView thumbnailImageView;
        public ImageView imgDelete,imgEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView= itemView.findViewById(R.id.tvtitle);
            thumbnailImageView=itemView.findViewById(R.id.imageV5);
            tvname=itemView.findViewById(R.id.tvname);
            tvchapter=itemView.findViewById(R.id.tvchapter);
            imgDelete=itemView.findViewById(R.id.imgDeleteS);
            imgEdit=itemView.findViewById(R.id.imgEdit);
        }
    }



}
