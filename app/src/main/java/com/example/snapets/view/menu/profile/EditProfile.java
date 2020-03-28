package com.example.snapets.view.menu.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.snapets.R;
import com.example.snapets.controller.UserController;
import com.example.snapets.model.User;
import com.example.snapets.view.ManageData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class EditProfile extends Fragment implements ManageData {
    private User user = new User();
    private FirebaseFirestore db;
    ImageView new_edit_profile_image;
    ImageButton edit_profile_back, save;
    EditText nome_edit_profile, nickname_edit_profile, bio_edit_profile, email_edit_profile, genero_edit_profile;
    Button change_image;

    final String uid = UserController.getInstance().getAuth().getCurrentUser().getUid();

    String myUrl;
    StorageTask upLoadTask;

    Uri imageUrl;
    public EditProfile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        new_edit_profile_image = view.findViewById(R.id.new_edit_profile_image);
        edit_profile_back = view.findViewById(R.id.edit_profile_back);
        save = view.findViewById(R.id.new_edit_confirm);
        nome_edit_profile = view.findViewById(R.id.new_edit_profile_name);
        nickname_edit_profile = view.findViewById(R.id.new_edit_profile_nickname);


        email_edit_profile = view.findViewById(R.id.new_edit_profile_email);
        genero_edit_profile = view.findViewById(R.id.new_edit_profile_genero);
        bio_edit_profile = view.findViewById(R.id.new_edit_profile_bio);





        UserController.getInstance().getUserData(this, uid, "nickname", nickname_edit_profile);
        UserController.getInstance().getUserData(this, uid, "image", new_edit_profile_image);
        UserController.getInstance().getUserData(this, uid, "username", nome_edit_profile);
        UserController.getInstance().getUserData(this, uid, "email", email_edit_profile);
        UserController.getInstance().getUserData(this, uid, "genre", genero_edit_profile);
        UserController.getInstance().getUserData(this, uid, "biografy", bio_edit_profile);

        new_edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //   Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
                //startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Perfil");
                UploadImage(storageReference);
            }
        });

        edit_profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment pf = new ProfileFragment(uid);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.nav_profile, pf);
                fragmentTransaction.commit();

                Log.d(TAG, "Clicou em voltar!");

            }
        });



              //UserController.getInstance().alterar(this, nickname_edit_profile.setText(user.getNickname()));



        return view;
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


                        user.setUsername(nome_edit_profile.getText().toString());
                        user.setNickname(nickname_edit_profile.getText().toString());
                        user.setEmail(email_edit_profile.getText().toString());
                        user.setBio(bio_edit_profile.getText().toString());
                        user.setGenre(genero_edit_profile.getText().toString());

                        user.setId(UserController.getInstance().getAuth().getCurrentUser().getUid());

                        if(myUrl != null){
                            user.setImageUrl(myUrl);
                            Log.d("Url myUrl if", myUrl);
                        }


                        UserController.getInstance().changeUserData(user, uid, getContext());

                    }

                }
            });


        } else {
            user.setUsername(nome_edit_profile.getText().toString());
            user.setNickname(nickname_edit_profile.getText().toString());
            user.setEmail(email_edit_profile.getText().toString());
            user.setBio(bio_edit_profile.getText().toString());
            user.setGenre(genero_edit_profile.getText().toString());

            user.setId(uid);

            if(myUrl != null){
                user.setImageUrl(myUrl);
                Log.d("Url myUrl if", myUrl);
            }


            UserController.getInstance().changeUserData(user,uid, getContext());

        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUrl = result.getUri();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            new_edit_profile_image.setImageBitmap(imageBitmap);
            //Log.d("RESULT", data.getStringExtra())
        }
        else if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            imageUrl = selectedImage;
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap( this.getContext().getContentResolver(), selectedImage);
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
                new_edit_profile_image.setImageBitmap(bitmapReduzido);
                new_edit_profile_image.setScaleType(ImageView.ScaleType.FIT_XY);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity( getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }





    @Override
    public void manageData(Object data, Object view) {

        if (view == null){
            Log.d("fail change null case ", "EditProfile " + data.toString());
            return;

        }

        if (view instanceof ImageView){
            if (data instanceof Bitmap){

            }
            else{
                Glide.with(this.getContext())
                        .load(data)
                        .into((ImageView)view);
            }

            Log.d("change imageView", "EditProfile " + data.toString() + " " + view.toString());

            //holder.postImage.setContentDescription("Teste");
        }
        else if (view instanceof TextView){
            if(data == null)
                data = "";
            ((TextView) view).setText(data.toString());
            Log.d("change textView", "EditProfile " + data.toString() + " " + view.toString());
        }


        else{
            Log.d("failed change", "EditProfile " + view.toString() + " cannot be found " +  data.toString() + " ");
        }
    }
}


