package com.example.snapets.controller;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.snapets.R;
import com.example.snapets.model.Post;
import com.example.snapets.view.ManageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LikeController implements ManageData {

    private FirebaseFirestore db;

    private static LikeController instance = null;

    private LikeController() {
        this.db = FirebaseFirestore.getInstance();

    }

    public static LikeController getInstance(){
        if (instance == null){
            instance = new LikeController();
        }

        return instance;
    }


    public void checkLike(final Post post, final ImageButton imageButton){

        final DocumentReference likePost = db.collection("post").document(post.getPost_id()).collection("likes")
                .document(UserController.getInstance().getAuth().getCurrentUser().getUid());

        likePost.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()) {
                            Log.d("Likes", "O post " + post.getPost_id() + " está curtido");
                            imageButton.setImageResource(R.drawable.ic_feed_like2);
                            imageButton.setTag("Liked");
                        }

                    }
                });


    }


    public void setLike(final Post post, final Object view, final boolean liked){
        //cria um documento se não existir com a id do usuário
        String user_id = UserController.getInstance().getAuth().getCurrentUser().getUid();

        final DocumentReference likePost = db.collection("post").document(post.getPost_id()).collection("likes")
                .document(user_id);
        Map<String, Object> map = new HashMap<>();
        map.put("Liked", true);

        likePost.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!liked)
                    Log.d("Likes", "O post " + post.getPost_id() + " foi curtido");

                else{
                    likePost.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("Desliked", "O post " + post.getPost_id() + " foi descurtido");
                        }
                    });
                }
                updateLikesNumber(post, view);
            }
        });
    }


    private void updateLikesNumber(final Post post, final Object view){


        db.collection("post").document(post.getPost_id())
                .collection("likes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int likes_number = 0;
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                likes_number++;

                            }

                            //Agora no ManageData vms atualizar o número de curtidas
                            manageData(likes_number, view);

                            Map<String, Object> postMap = post.toMap();
                            postMap.put("likes", likes_number);

                            db.collection("post").document(post.getPost_id())
                                    .set(postMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Número de likes atualizado!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Atualizar o número de likes falhou");
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public void manageData(Object data, Object view) {
        if (view instanceof TextView){
            ((TextView) view).setText(data.toString());
            Log.d("change textView", data.toString() + " " + ((TextView) view).getId());
        }

        else{
            Log.d("failed change", view.toString() + " cannot be found " +  data.toString() + " ");
        }
    }



    }