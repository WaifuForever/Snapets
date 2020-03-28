package com.example.snapets.view.menu.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snapets.R;
import com.example.snapets.controller.FollowController;
import com.example.snapets.controller.PostController;
import com.example.snapets.controller.UserController;
import com.example.snapets.view.ManageData;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment implements ManageData {

    private PostController postController;
    private FirebaseFirestore db;


    private String uid;

    private RecyclerView recyclerView;
    private UserController userControll;
    public ImageView image_profile_perfil, options;
    public TextView bio, post_profile, username_profile, fullname, followers, following;

    public ImageButton my_fotos;

    String profileId;

    public ProfileFragment(String uid) {
        this.uid = uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile_perfil = view.findViewById(R.id.image_profile_perfil);
        options = view.findViewById(R.id.options);
        username_profile = view.findViewById(R.id.username_profile);

        my_fotos = view.findViewById(R.id.my_fotos);
        bio = view.findViewById(R.id.bio);

        fullname = view.findViewById(R.id.fullname);

        followers = view.findViewById(R.id.followers_number);
        following = view.findViewById(R.id.following_number);

        UserController.getInstance().getUserData(this, uid, "nickname", username_profile);
        UserController.getInstance().getUserData(this, uid, "image", image_profile_perfil);
        UserController.getInstance().getUserData(this, uid, "username", fullname);
        UserController.getInstance().getUserData(this, uid, "biografy", bio);

        final Button bted = view.findViewById(R.id.edit_profile);
        if(UserController.getInstance().getAuth().getCurrentUser().getUid().equals(uid)){

            bted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditProfile edit = new EditProfile();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_profile, edit);
                    fragmentTransaction.commit();
                }

            });
        } else {
            FollowController.getInstance().checkFollow(uid, bted);



            bted.setText("Seguir");
            bted.setBackgroundColor(Color.parseColor("#379146"));
            bted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean follow;

                    if (bted.getText().toString().equals("Seguir")){
                        follow = true;
                        bted.setText("Seguindo");
                        bted.setBackgroundColor(Color.parseColor("#c0c4c1"));
                    }

                    else{
                        follow = false;
                        bted.setText("Seguir");
                        bted.setBackgroundColor(Color.parseColor("#379146"));
                    }

                    FollowController.getInstance().setFollow(uid, following, followers, follow);
                }
            });
        }
        return view;
    }

    @Override
    public void manageData(Object data, Object view) {
        if (view == null) {
            Log.d("fail change null case ", "ProfileFragment " +  data.toString());
            return;

        }

        if (view instanceof ImageView) {
            Glide.with(this.getContext())
                    .load(data)
                    .into((ImageView) view);
            Log.d("change imageView", "ProfileFragment " + data.toString() + " " + view.toString());

            //holder.postImage.setContentDescription("Teste");
        } else if (view instanceof TextView) {
            ((TextView) view).setText(data.toString());
            Log.d("change textView", "ProfileFragment " + data.toString() + " " + view.toString());
        } else {
            Log.d("failed change", "ProfileFragment " + view.toString() + " cannot be found " + data.toString() + " ");
        }
    }



}

