package com.oslsoftware.clientapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;


public class MyAccountFragment extends Fragment {
Button settings,signOut,myBalance,discounts,contact,invite;
Spinner languageSpinner;
LinearLayout settingLayout,contactLayout;
RelativeLayout mainLayout;
CardView languageLayout;
Button french,english,arabic;
TextView txtChangeLanguage;
    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settings = view.findViewById(R.id.bt_settings);
        signOut = view.findViewById(R.id.bt_signOut);
        contact =  view.findViewById(R.id.bt_ContactUs);
        invite =  view.findViewById(R.id.bt_InviteFriends);
        settingLayout = view.findViewById(R.id.settingsLayout);
       languageLayout = (CardView) getLayoutInflater().inflate(R.layout.language_dialog,null,false);
        mainLayout = view.findViewById(R.id.mainAccountLayout);
        contactLayout = view.findViewById(R.id.contactLayout);
        txtChangeLanguage = view.findViewById(R.id.txtChangeLanguage);
        french = languageLayout.findViewById(R.id.bt_french);
        english = languageLayout.findViewById(R.id.bt_english);
        arabic = languageLayout.findViewById(R.id.bt_arabic);
        txtChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(languageLayout);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingLayout.getVisibility() == ViewGroup.GONE)
                {
                    TransitionManager.beginDelayedTransition(mainLayout);
                    settingLayout.setVisibility(ViewGroup.VISIBLE);
                    settings.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_arrow_up),null);
                }
                else
                {
                    TransitionManager.beginDelayedTransition(mainLayout);
                    settingLayout.setVisibility(ViewGroup.GONE);
                    settings.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_arrow_down),null);
                }
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactLayout.getVisibility() == ViewGroup.GONE)
                {
                    TransitionManager.beginDelayedTransition(mainLayout);
                    contactLayout.setVisibility(ViewGroup.VISIBLE);
                    contact.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_arrow_up),null);

                }
                else
                {
                    TransitionManager.beginDelayedTransition(mainLayout);
                    contactLayout.setVisibility(ViewGroup.GONE);
                    contact.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_arrow_down),null);

                }
            }
        });
        invite.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"link to playstore");
            startActivity(intent);
        });
        signOut.setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            getActivity().finishAndRemoveTask();
        });
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
            //    R.array.languages, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configuration.setLocale(new Locale("en"));
                getResources().updateConfiguration(configuration,metrics);
                getActivity().recreate();
            }
        });
        french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configuration.setLocale(new Locale("fr"));
                getResources().updateConfiguration(configuration,metrics);
                getActivity().recreate();
            }
        });
        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configuration.setLocale(new Locale("ar"));
                getResources().updateConfiguration(configuration,metrics);
                getActivity().recreate();
            }
        });




    }
}