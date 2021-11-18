package com.oslsoftware.clientapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Retrofit;


public class Login extends Fragment {
    private FirebaseAuth mAuth;


    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View root;
    TextView RegisterTextView;
    EditText loginUserName, loginPassword;
    Button loginBt;
    private Retrofit retrofit;

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    Register registerFragment = new Register();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);
        InitViews();
        return root;
    }

    public void InitViews() {
        RegisterTextView = root.findViewById(R.id.TextRegister);
        loginUserName = root.findViewById(R.id.loginUserName);
        loginPassword = root.findViewById(R.id.loginPassword);
        loginBt = root.findViewById(R.id.loginButton);
        RegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFragment.setRetrofit(retrofit);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_main, registerFragment).commit();
            }
        });
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = loginUserName.getText().toString();
                String PassWord = loginPassword.getText().toString();
                //TODO : pack data and make api call to athenticate

               /*Intent intent = new Intent (getActivity(),content.class);
               startActivity(intent);*/
               mAuth = FirebaseAuth.getInstance();
                //mAuth.getCurrentUser().getUid().hashCode();
                mAuth.signInWithEmailAndPassword(UserName,PassWord)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //TODO : make a modal dialog to show progress
                                    Toast.makeText(getContext(),"Signed  in  Successfully",Toast.LENGTH_SHORT);
                                    Intent intent = new Intent (getActivity(),content.class);
                                    startActivity(intent);
                                    //Intent intnet = new Intent(getActivity(),)
                                    //startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Signing in Failed",Toast.LENGTH_SHORT);
                                   Exception e = task.getException();
                                   //if(e == 1st exception )
                                    //if(e ==  2nd exception)
                                }
                            }
                        });
            }
        });

    }
}