package com.example.snapets.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snapets.MainActivity;
import com.example.snapets.R;
import com.example.snapets.controller.UserController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editSenha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        editEmail = findViewById(R.id.campoEmail);
        editSenha = findViewById(R.id.campoSenha);
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            //Passar para proxima tela
            Intent a = new Intent(this, MainActivity.class);
            Toast.makeText(LoginActivity.this,"Usuário logado!", Toast.LENGTH_SHORT).show();
            startActivity(a);
        }
        else{
            Toast.makeText(LoginActivity.this,"Precisa fazer o login!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUsers = UserController.getInstance().getAuth().getCurrentUser();
        updateUI(currentUsers);

    }

    public void login(View view){
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        if(email.equals("")){
            editEmail.setError("Preencha este campo!");
            return;
        }
        if(senha.equals("")){
            editSenha.setError("Preencha este campo!");
            return;
        }
        UserController.getInstance().getAuth().signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI(UserController.getInstance().getAuth().getCurrentUser());
                    //Intent a = new Intent(this, LogarActivity.class);
                }else{
                    Toast.makeText(LoginActivity.this,"Usuário ou Senha incorreta!", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }

}
