package com.example.snapets.view.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snapets.R;
import com.example.snapets.controller.UserController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.show_image_content.activity_register);
    }*/

    private EditText editNome;
    private EditText editEmail;
    private EditText editSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        editNome = findViewById(R.id.field_username);
        editEmail = findViewById(R.id.field_email);
        editSenha = findViewById(R.id.field_password);

        final String msg =  null;
        final String msgErro = null;

    }

    public void cadastro(View view) {

        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if (nome.equals("")) {
            editNome.setError("Preencha este campo!");
            editNome.requestFocus();
            return;
        }
        if (email.equals("")) {
            editEmail.setError("Preencha este campo!");
            editEmail.requestFocus();
            return;
        }


        if (senha.equals("")) {
            editSenha.setError("Preencha este campo!");
            editSenha.requestFocus();
            return;
        }

        Log.d("SnaPet", "Validou campos!");

        final Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", nome);
        userInfo.put("email", email);
        userInfo.put("password", senha);
        userInfo.put("nickname", nome);
        userInfo.put("biografy", "");
        userInfo.put("image", "");
        userInfo.put("genre", "");




        Log.d("SnaPet", userInfo.toString());




        UserController.getInstance().getAuth().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userInfo.put("login_id", UserController.getInstance().getAuth().getUid());
                            UserController.getInstance().adicionar(userInfo, RegisterActivity.this);
                            finish();

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                editSenha.setError("Senha Fraca!");
                                editSenha.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                editEmail.setError("E-mail já existe!");
                                editEmail.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                editEmail.setError("E-mail inválido");
                                editEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e("Cadastro", e.getMessage());
                            }
                        }

                    }
                });
    }
}
