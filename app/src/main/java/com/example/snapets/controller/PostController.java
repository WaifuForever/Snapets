package com.example.snapets.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.snapets.model.Post;
import com.example.snapets.view.ExpoList;
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

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PostController{

    private static PostController instance = null;



    private FirebaseFirestore db;


    private PostController() {
        this.db = FirebaseFirestore.getInstance();

    }

    public static PostController getInstance(){
        if (instance == null){
            instance = new PostController();
        }

        return instance;
    }





    public void showList(final ExpoList expoList){
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Post> list = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Post post = new Post();

                                post.setPublisher_id((String) document.getId());
                                post.setUid( (String) document.get("user_id"));
                                post.setPublisher_id((String) document.get("publisher_id"));

                                post.setPost_id((String) document.getId());
                                post.setPublisher_description((String) document.get("publisher_description"));
                                post.setDescription( (String)document.get("description"));
                                post.setLikes(String.valueOf(document.get("likes")));
                                post.setHashtags((String) document.get("hashtags"));
                                post.setnComments((String) document.get("nComments"));



                                post.setPostImage((String)document.get("image"));

                                list.add(post);

                            }
                            expoList.showList(list, true);
                            Log.d(TAG, "Sucesso");
                        } else {
                            expoList.showList(null,false);
                            Log.d(TAG, "Falha");
                        }
                    }
                });
    }




    public void getPostData(final ManageData mg, final String post_id, final String data, final Object view){


        DocumentReference postRef = db.collection("post/").document(post_id);
        postRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                       // mg.manageData(getPostData(document, data), view);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    public void adicionar(Post post, final Context ctx) {

        Map map = post.toMap();
        db.collection("post")
                .add(post.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(ctx, "Post cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx, "Falha ao cadastrar!", Toast.LENGTH_LONG).show();

                    }
                });

    }



}
