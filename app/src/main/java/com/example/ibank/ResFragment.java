package com.example.ibank;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class ResFragment extends Fragment {

    ImageView bglogo ;
    Animation zoom;
    TextView dis , pers , num;
    Button ticketbtn , bookbtn ;
    private Dialog ticket ;
    int i , l , km;
    Bundle bundle = getArguments();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_res,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String aaaa = bundle.getString("km");
        Toast.makeText(getActivity(), aaaa, Toast.LENGTH_SHORT).show();


        initperson();
        ticket = new Dialog(getActivity());
        ticketbtn = getActivity().findViewById(R.id.ticketbtn);
        pers = getActivity().findViewById(R.id.numbtxt);
        bglogo = getActivity().findViewById(R.id.bglogo);
        zoom  = AnimationUtils.loadAnimation(getActivity(),R.anim.splash_in);
        bglogo.setAnimation(zoom);

        dis = getActivity().findViewById(R.id.distancetxt);
        dis.setText("1km");

        ticketbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        ticket.setContentView(R.layout.dialog_ticket);
        num = ticket.findViewById(R.id.numtxt);
        bookbtn = ticket.findViewById(R.id.bookBtn);
        num.setText(l+5+"");
        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Booked", Toast.LENGTH_SHORT).show();
                ticket.dismiss();
            }
        });

        ticket.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ticket.show();
    }

    private void initperson() {
        new Thread() {
            public void run() {
                Random rand = new Random();
                l = rand.nextInt(100);

                for (i=0;i<l;i++) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pers.setText(Integer.toString(i));
                            }
                        });
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
