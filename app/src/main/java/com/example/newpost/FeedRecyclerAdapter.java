package com.example.newpost;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedRecyclerAdapter  extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    private ArrayList<String> emailList;
    private ArrayList<String> commentList;
    private ArrayList<String> imageList;

    public FeedRecyclerAdapter(ArrayList<String> emailList, ArrayList<String> commentList, ArrayList<String> imageList) {
        this.emailList = emailList;
        this.commentList = commentList;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_new,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.commentText.setText(commentList.get(position));
        holder.emailText.setText(emailList.get(position));
        Picasso.get().load(imageList.get(position)).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return emailList.size();

    }

    class PostHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView emailText;
        TextView commentText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
        imageView=itemView.findViewById(R.id.imageText);
        commentText=itemView.findViewById(R.id.commentText);
        emailText=itemView.findViewById(R.id.emailText);
        }
    }
}
