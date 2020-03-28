package com.example.snapets.view.menu.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snapets.R;
import com.example.snapets.model.Post;
import com.example.snapets.view.ManageData;
import com.example.snapets.view.menu.OnListFragmentInteractionListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TimelineAdapter implements ManageData {

    private final List<Post> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context ctx;

    public TimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_timeline_item, parent, false);
        return new TimelineAdapter.ViewHolder(view);
    }


    public TimelineAdapter(Context ctx, List<Post> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.ctx = ctx;

    }


    public void onBindViewHolder(@NonNull final TimelineAdapter.ViewHolder holder, final int position) {

        if (mValues.get(position).getPostImage() != null) {


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("Posts");
            //Picasso.with(ctx).load("http://i.imgur.com/DvpvklR.png").into(holder.postImage);

            Glide.with(ctx)
                    .load(mValues.get(position).getPostImage())
                    .into(holder.postImage);
            //holder.postImage.setContentDescription("Teste");


        } else {
            Log.d("Deu ruim", "Não carregou");
        }

        //holder.img_post = (Post) mValues.get(position).getPostImage());
        // holder.img_profile = (Post) mValues.get(position).getUid(); //pegar imagem do usuario

        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(holder.mItem);

                }
            }
        });


    }



    public void manageData(Object data, Object view) {
        if (view == null) {
            Log.d("fail change null case ", data.toString());
            return;

        }

        if (view instanceof ImageView) {
            Glide.with(ctx)
                    .load(data)
                    .into((ImageView) view);
            Log.d("change imageView", data.toString() + " " + view.toString());

            //holder.postImage.setContentDescription("Teste");
        } else if (view instanceof TextView) {
            ((TextView) view).setText(data.toString());
            Log.d("change textView", data.toString() + " " + view.toString());
        } else {
            Log.d("failed change", view.toString() + " cannot be found " + data.toString() + " ");
        }
    }

    @NonNull



    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView postImage;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            postImage = view.findViewById(R.id.post_image);

        }
    }
}

