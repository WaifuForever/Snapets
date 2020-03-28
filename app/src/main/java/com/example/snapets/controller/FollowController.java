package com.example.snapets.controller;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.snapets.model.User;
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

public class FollowController implements ManageData {

    private FirebaseFirestore db;

    private static FollowController instance = null;

    private FollowController() {
        this.db = FirebaseFirestore.getInstance();

    }

    public static FollowController getInstance(){
        if (instance == null){
            instance = new FollowController();
        }

        return instance;
    }


    public void checkFollow(final String userId, final Button button){

        final DocumentReference followUser = db.collection("usuario").document(userId).collection("followers")
                .document(UserController.getInstance().getAuth().getCurrentUser().getUid());

        followUser.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()) {
                            Log.d("Follows", "O usuario " + userId + " está sendo seguido");
                            button.setText("Seguindo");

                        }

                        else{

                            button.setText("Seguir");
                        }

                    }
                });


    }


    public void setFollow(final String user_id, final Object view, final Object view2, final boolean follow){
        //cria um documento se não existir com a id do usuário


        String current_user_id = UserController.getInstance().getAuth().getCurrentUser().getUid();

        final DocumentReference followerUsers = db.collection("usuario").document(user_id).collection("followers")
                .document(current_user_id);

        Map<String, Object> map = new HashMap<>();
        map.put("Follower", true);

        followerUsers.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(follow)
                    Log.d("Follows", "O usuario " + user_id + " foi seguido");

                else{
                    followerUsers.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("Unfollows", "O usuario " + user_id + " nao e mais seguido");
                        }
                    });
                }
                updateFollowingNumber(user_id, view);
            }
        });

        final DocumentReference followedUser = db.collection("usuario").document(current_user_id).collection("following")
                .document(user_id);
        map.clear();
        map.put("Following", true);

        followedUser.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(follow)
                    Log.d("Follows", "O usuario " + user_id + " está sendo seguido");

                else{
                    followedUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("Unfollows", "O usuario " + user_id + " não está mais sendo seguido");
                        }
                    });
                }
                updateFollowersNumber(user_id, view2);

            }
        });
    }

    private void updateFollowersNumber(final String user_id, final Object view){
        final User user = new User();
        db.collection("usuario").document(UserController.getInstance().getAuth().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            user.setGenre((String) document.get("genre"));
                            user.setImageUrl((String) document.get("image"));
                            user.setId(user_id);
                            user.setUsername((String) document.get("username"));
                            user.setNickname((String) document.get("nickname"));
                            user.setEmail((String) document.get("email"));
                            user.setBio((String) document.get("biografy"));
                        }
                    }
                });



        db.collection("usuario").document(user_id)
                .collection("followers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int followers_number = 0;
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                followers_number++;

                            }

                            //Agora no ManageData vms atualizar o número de curtidas
                            manageData(followers_number, view);

                            Map<String, Object> userMap = user.toMap();
                            userMap.put("following", followers_number);

                            db.collection("user").document(user_id)
                                    .set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Número de followers atualizado!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Atualizar o número de followers falhou");
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateFollowingNumber(final String user_id, final Object view){
        final User user = new User();
        db.collection("usuario").document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    user.setGenre((String) document.get("genre"));
                    user.setImageUrl((String) document.get("image"));
                    user.setId(user_id);
                    user.setUsername((String) document.get("username"));
                    user.setNickname((String) document.get("nickname"));
                    user.setEmail((String) document.get("email"));
                    user.setBio((String) document.get("biografy"));
                }
            }
        });



        db.collection("usuario").document(user_id)
                .collection("following")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int following_number = 0;
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                following_number++;

                            }

                            //Agora no ManageData vms atualizar o número de curtidas
                            manageData(following_number, view);

                            Map<String, Object> userMap = user.toMap();
                            userMap.put("following", following_number);

                            db.collection("user").document(user_id)
                                    .set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Número de following atualizado!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Atualizar o número de following falhou");
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