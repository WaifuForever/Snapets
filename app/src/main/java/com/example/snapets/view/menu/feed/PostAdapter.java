package com.example.snapets.view.menu.feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snapets.R;
import com.example.snapets.controller.LikeController;
import com.example.snapets.controller.PageController;
import com.example.snapets.controller.UserController;
import com.example.snapets.model.Post;
import com.example.snapets.view.ManageData;
import com.example.snapets.view.menu.OnListFragmentInteractionListener;
import com.example.snapets.view.menu.profile.ProfileFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements ManageData  {

    private final List<Post> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context ctx;


    public PostAdapter(Context ctx, List<Post> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        this.ctx = ctx;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.ViewHolder holder, final int position) {



        UserController.getInstance().getUserData(this, mValues.get(position).getUid(),
                "nickname", holder.nickname);

        if (!mValues.get(position).getPublisher_Id().equals(mValues.get(position).getUid())){
            UserController.getInstance().getUserData(this, mValues.get(position).getPublisher_Id(),
                    "nickname", holder.publisherName); //pegar nome do publisher

            holder.publisherName.setText(holder.publisherName.getText().toString() + " " + mValues.get(position).getPublisher_description());
        }

        UserController.getInstance().getUserData(this, mValues.get(position).getUid(), "image", holder.post_image_profile);

        holder.description.setText(mValues.get(position).getDescription());

        holder.likes.setText(mValues.get(position).getLikes());


        holder.n_comments.setText(mValues.get(position).getnComments());

        LikeController.getInstance().checkLike(mValues.get(position), holder.btn_like);
        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.btn_like.getTag().equals("Like")){

                    LikeController.getInstance().setLike(mValues.get(position), holder.likes, false);
                    holder.btn_like.setImageResource(R.drawable.ic_feed_like2);
                    holder.btn_like.setTag("Liked");
                }
                else{
                    LikeController.getInstance().setLike(mValues.get(position), holder.likes, true);
                    holder.btn_like.setImageResource(R.drawable.ic_feed_like1);
                    holder.btn_like.setTag("Like");
                }

            }
        });

        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment pf = new ProfileFragment(mValues.get(position).getUid());
                FragmentManager fm = ((AppCompatActivity)ctx).getSupportFragmentManager();

                PageController.getInstance().changePage(fm, pf);

                Log.d("TAG", "Clicou no botão");
            }
        });

        if (mValues.get(position).getPostImage() != null){


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("Posts");
            //Picasso.with(ctx).load("http://i.imgur.com/DvpvklR.png").into(holder.postImage);

            Glide.with(ctx)
                    .load(mValues.get(position).getPostImage())
                    .into(holder.postImage);
            //holder.postImage.setContentDescription("Teste");



        }
        else{
            Log.d("Deu ruim", "Não carregou");
        }

        //holder.img_post = (Post) mValues.get(position).getPostImage());
       // holder.img_profile = (Post) mValues.get(position).getUid(); //pegar imagem do usuario

        holder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onListFragmentInteraction(holder.mItem);

                }
            }
        });


    }



    @Override
    public void manageData(Object data, Object view) {
        if (view == null){
            Log.d("fail change null case ", data.toString());
            return;

        }

        if (view instanceof ImageView){
            Glide.with(ctx)
                    .load(data)
                    .into((ImageView)view);
            Log.d("change imageView", data.toString() + " " + view.toString());

            //holder.postImage.setContentDescription("Teste");
        }
        else if (view instanceof TextView){
            ((TextView) view).setText(data.toString());
            Log.d("change textView", data.toString() + " " + view.toString());
        }

        else{
            Log.d("failed change", view.toString() + " cannot be found " +  data.toString() + " ");
        }
    }

    @NonNull



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public TextView nickname, time, description, publisherName, likes, n_comments;
        public ImageView postImage, post_image_profile;
        public Post mItem;
        public ImageButton btn_like;

        public ViewHolder(View view){
            super(view);
            mView = view;

            nickname = view.findViewById(R.id.post_nickname);
            time = view.findViewById(R.id.post_time);
            description = view.findViewById(R.id.post_description);
            publisherName = view.findViewById(R.id.post_publisherName);
            likes = view.findViewById(R.id.post_nLikes);
            n_comments = view.findViewById(R.id.post_nComments);


            postImage = view.findViewById(R.id.post_image);
            post_image_profile = view.findViewById(R.id.post_image_profile);

            btn_like = view.findViewById(R.id.post_like);
            btn_like.setTag("Like");

        }


    }




}




