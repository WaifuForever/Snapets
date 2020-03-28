package com.example.snapets.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.snapets.model.User;
import com.example.snapets.view.ExpoList;
import com.example.snapets.view.ManageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UserController {

    private static UserController instance = null;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public FirebaseAuth getAuth(){
        return auth;
    }

    private UserController() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();

    }

    public static UserController getInstance(){
        if (instance == null){
            instance = new UserController();
        }

        return instance;
    }


    public void showList(final ExpoList expoList){
        db.collection("usuario")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<User> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               /* User user = new User(document.getId(), document.getData().get("username").toString(),"",
                                        document.getData().get("image").toString(), document.getData().get("nickname").toString(),"");
                                list.add(user);
*/
                            }
                            expoList.showList(list, true);
                            Log.d(TAG, "Sucesso");
                        } else {
                            expoList.showList(null, false);
                            Log.d(TAG, "Falha");
                        }
                    }
                });
    }


    public void getUserData(final ManageData mg, final String userId, final String data, final Object view){
        Log.d("Data", userId + " " + data);

        DocumentReference userRef = db.collection("usuario/").document(userId);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mg.manageData(document.getString(data), view);
                    } else {
                        Log.d(TAG, mg.toString() + " No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }



            }
        });
    }

    public void changeUserData (final User user, final String user_id, final Context ctx) {
        final Map userInfo = user.toMap();


        db.collection("usuario/").document(user_id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("1" + document);
                        List<String> data = new ArrayList<>();
                        if (user.getGenre() == null){
                           data.add("genre");
                        }
                        if (user.getUsername() == null){
                            data.add("username");
                        }

                        if (user.getNickname() == null){
                            data.add("nickname");
                        }

                        if (user.getImageUrl() == null){
                            data.add("image");
                            Log.d(TAG, "aqui é null");

                        }

                        if (user.getBio() == null){
                            data.add("biografy");
                        }

                        if (user.getEmail() == null){
                            data.add("email");
                        }

                        if (user.getId() == null){
                            data.add("login_id");
                        }

                        for (String string : data){
                            System.out.println(data + " " + string);

                            System.out.println((String)document.get(string));
                            userInfo.put(string, ((String)document.get(string)));

                        }
                        db.collection("usuario")
                                .document(user_id)
                                .set(userInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Mudanças realizadas com sucesso!");
                                        Toast.makeText(ctx, "Mudanças realizadas com sucesso!", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "As alterações não foram efetivadas");
                                        Toast.makeText(ctx, "As alterações não foram efetivadas", Toast.LENGTH_LONG).show();
                                    }
                                });

                        System.out.println("2" + document);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }



                }
            });



    }


    public void adicionar(final Map userInfo, final Context ctx) {


        db.collection("usuario")
                .document((String) userInfo.get("login_id"))
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Registro realizado com sucesso!");
                        Toast.makeText(ctx, "Registro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "O Registro falhou");
                        Toast.makeText(ctx, "O Registro falhou", Toast.LENGTH_LONG).show();
                    }
                });

    }



}
