package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button signup;

    private EditText email;
    private EditText password;
    private Button login;

    private Button resendLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        signup=findViewById(R.id.lsignup);

        resendLink=findViewById(R.id.resend);

        email=findViewById(R.id.lemail);
        password=findViewById(R.id.lpassword);
        login=findViewById(R.id.login);

        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified())
        {
            Intent intent=new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stremail=email.getText().toString();
                String strpassword=password.getText().toString();

                if(!TextUtils.isEmpty(stremail) && !TextUtils.isEmpty(strpassword)){
                    firebaseAuth.signInWithEmailAndPassword(stremail,strpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                if(firebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    Intent intent=new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    resendLink.setVisibility(View.VISIBLE);
                                    resendLink.setClickable(true);
                                    Toast.makeText(Login.this,"Please verify your email\n and continue!",Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();

                                }
                            }
                            else
                            {
                                Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(Login.this,"Email and Password cannot be empty!",Toast.LENGTH_SHORT).show();
                }

                resendLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(firebaseAuth!=null)
                        {
                            if(firebaseAuth.getCurrentUser()!=null)
                            {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(Login.this,"Link has been sent to your Email\nplease verify and continue!",Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                            }
                        }
                    }
                });
            }
        });



    }
}