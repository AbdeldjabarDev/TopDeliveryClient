package com.oslsoftware.clientapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OrderStateFragment extends Fragment {

    ObjectAnimator animator;
TextView textView;
    public ObjectAnimator getAnimator() {
        return animator;
    }

    public OrderStateFragment() {
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
        return inflater.inflate(R.layout.fragment_order_state, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView IV = view.findViewById(R.id.waitView);
        textView = view.findViewById(R.id.txtOrderState);
        Path path = new Path();
        //path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true);
        path.setLastPoint(500,0);
        path.lineTo(500,500);
        path.lineTo(500,0);
        animator = ObjectAnimator.ofFloat(IV, View.X, View.Y, path);
        animator.setDuration(2000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();



    }
}