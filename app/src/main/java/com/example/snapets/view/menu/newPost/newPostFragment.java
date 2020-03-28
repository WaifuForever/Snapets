package com.example.snapets.view.menu.newPost;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.snapets.R;
import com.example.snapets.controller.PostController;
import com.example.snapets.controller.UserController;
import com.example.snapets.model.Post;
import com.example.snapets.view.ManageData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class newPostFragment extends Fragment implements ManageData {

    private PostController postController;
    private FirebaseFirestore db;


    public ImageView image_profile, prev_post_image, prev_image_profile;
    public TextView new_post_description, prev_nickname, prev_description;
    public ImageButton select_image, new_post_back, new_post_confirm;

    private final String uid = UserController.getInstance().getAuth().getCurrentUser().getUid();
    String myUrl;
    StorageTask upLoadTask;
    private Post post = new Post();

    Uri imageUrl;

    String profileId;


    public newPostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);




        image_profile = view.findViewById(R.id.new_post_image_profile);
        prev_image_profile = view.findViewById(R.id.prev_image_profile);
        prev_nickname = view.findViewById(R.id.prev_post_nickname);

        //button
        select_image = view.findViewById(R.id.new_post_camera);
        new_post_back = view.findViewById(R.id.new_post_back);
        new_post_confirm = view.findViewById(R.id.new_post_confirm);


        new_post_description = view.findViewById(R.id.new_post_description);
        prev_post_image = view.findViewById(R.id.prev_post_image);
        prev_description = view.findViewById(R.id.prev_post_description);

        UserController.getInstance().getUserData(this, uid, "nickname", prev_nickname);
        UserController.getInstance().getUserData(this,uid, "image", image_profile);
        UserController.getInstance().getUserData(this,uid,"image",prev_image_profile);


        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                prev_description.setText(new_post_description.getText().toString());
                handler.postDelayed(this,200);
            }
        },200);


        new_post_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  FeedFragment ff = new FeedFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.new_post_header, ff);
                fragmentTransaction.commit();

                Log.d(TAG, "Clicou em voltar!");*/
            }
        });

        select_image.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
            }
        });


        new_post_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Post");
                UploadImage(storageReference);
            }
        });
        return view;
    }

    @Override
    public void manageData(Object data, Object view) {
        if (view == null){
            Log.d("fail change null case ", data.toString());
            return;

        }

        if (view instanceof ImageView){
            Glide.with(this.getContext())
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


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadImage(StorageReference storageReference){
        if (imageUrl != null){
            final StorageReference reference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUrl));

            upLoadTask = reference.putFile(imageUrl);


            upLoadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull final Task task) throws Exception {
                    if (!task.isSuccessful()){
                        Log.d("Url ref sucess", reference.getDownloadUrl().toString());

                        throw task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        myUrl = downloadUri.toString();


                        post.setDescription(new_post_description.getText().toString().trim());
                        post.setnComments("0");
                        post.setPublisher_id(uid);
                        post.setUid(uid);
                        post.setLikes("0");




                        if(myUrl != null){
                            post.setPostImage(myUrl);
                            Log.d("Url myUrl if", myUrl);
                        }

                        PostController.getInstance().adicionar(post, newPostFragment.this.getContext());
                    }

                }
            });
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            /*CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUrl = result.getUri();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            new_edit_profile_image.setImageBitmap(imageBitmap);
            //Log.d("RESULT", data.getStringExtra())*/
        }
        else if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            imageUrl = selectedImage;
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap( this.getContext().getContentResolver(), selectedImage);
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
                prev_post_image.setImageBitmap(bitmapReduzido);
                prev_post_image.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (IOException e) {
                e.printStackTrace();
            }




            /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
        }else{
            Log.d("ERROR", "Something gone wrong");
        }

    }
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    //private static final int REQUEST_IMAGE_CAPTURE = 1;

   /* private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity( getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            setResult(takePictureIntent, this);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

    }*/







}

