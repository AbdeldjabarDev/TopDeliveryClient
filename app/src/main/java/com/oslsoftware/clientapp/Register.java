package com.oslsoftware.clientapp;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends Fragment {



    public Register()
    {
        // Required empty public constructor
    }
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private Retrofit retrofit;

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mAuth = FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();
    }
    public void showToastMessage(String text)
    {
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }


EditText email,password,phone_number,card_number,confirmPassword;
    Button register_button;

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String confpass = confirmPassword.getText().toString();
                String em = email.getText().toString();
                String phone = phone_number.getText().toString();
                String card = card_number.getText().toString();
                if(pass.equals("") || em.equals("") || phone.equals("") || card.equals("") || confpass.equals(""))
                {
                    showToastMessage("There are Empty Fields Please fill them ");
                }
                if(!confpass.equals(pass))
                {
                    showToastMessage("Passwords don't match");
                }
                else
                {
                    Userinfo info = new Userinfo();
                    info.setUser_email(em);
                    info.setPassword(pass);
                    Call<callStatus> registerCall = retrofit.create(MainActivity.ClientApi.class).Register(info);
                    Callback<callStatus> callback = new Callback<callStatus>() {
                        @Override
                        public void onResponse(Call<callStatus> call, Response<callStatus> response) {
                            if(response.body().getStatus() == 0)
                            {
                                showToastMessage("You have been registered successfully");
                                mAuth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful())
                                   {
                                    Intent intent = new Intent(getActivity(),content.class);
                                    startActivity(intent);
                                   }
                                   else
                                   {
                                       dialog.setMessage("Could not login " + task.getException().getMessage());
                                       OnCompleteListener<AuthResult> listener = this;
                                       dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry" , new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               mAuth.signInWithEmailAndPassword(em,pass).addOnCompleteListener(listener);
                                               dialog.dismiss();
                                           }
                                       });
                                   }
                                    }
                                });
                            }
                            if(response.body().getStatus() == -1)
                            {
                               dialog.setMessage("Something went wrong" + response.body().getMessage());
                               dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                               dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<callStatus> call, Throwable t) {

                            dialog.setMessage("Something went wrong please try again ");

                            Callback<callStatus> callback1 = this;
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    call.clone().enqueue(callback1);
                                }
                            });
                            dialog.show();
                        }
                    };
                    registerCall.enqueue(callback);
                }

                }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        email = root.findViewById(R.id.inputUserName);
        phone_number = root.findViewById(R.id.inputPhoneNumber);
        password = root.findViewById(R.id.inputPassword);
        card_number = root.findViewById(R.id.inputCardNumber);
        confirmPassword = root.findViewById(R.id.inputConfirmPassword);
        register_button = root.findViewById(R.id.register_button);


       return root;
    }

}