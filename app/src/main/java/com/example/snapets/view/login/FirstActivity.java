package com.example.snapets.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snapets.MainActivity;
import com.example.snapets.R;
import com.example.snapets.controller.UserController;

public class FirstActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        if(UserController.getInstance().getAuth().getCurrentUser() != null){
            Intent a = new Intent(this, MainActivity.class);
            Toast.makeText(FirstActivity.this,"Usuário logado!", Toast.LENGTH_SHORT).show();
            startActivity(a);
        }


    }

    public void login_view(View view){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);


    }
    public void register_view(View view){
        Intent a = new Intent(this, RegisterActivity.class);
        startActivity(a);
    }

}
