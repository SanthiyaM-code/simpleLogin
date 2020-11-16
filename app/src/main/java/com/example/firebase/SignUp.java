package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button signup;
    private Button login;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        email=findViewById(R.id.semail);
        password=findViewById(R.id.spassword);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.slogin);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail=email.getText().toString();
                String strPassword=password.getText().toString();

                if(!TextUtils.isEmpty(strEmail) && !TextUtils.isEmpty(strPassword))
                {
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this,"Account Created sucessfully!",Toast.LENGTH_SHORT).show();

                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "Account created successfully!\nverification link is sent to your id\nplease verify and login again!", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(SignUp.this,Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(SignUp.this,"Error in sending link\nTry again",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                firebaseAuth.signOut();
                            }
                            else{
                                Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });


                }else{
                    Toast.makeText(SignUp.this,"Email or password cannot be empty",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}