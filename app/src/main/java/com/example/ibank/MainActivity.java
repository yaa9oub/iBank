package com.example.ibank;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Intent intent ;
    int frag;
    ResFragment resfragment ;
    FragmentTransaction t ;

    private MapFragment mapFragment;
    private ResFragment resFragment;
    private CamFragment camFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //////////////////////////////////////////////////////////
            FragmentManager manager = getSupportFragmentManager();
            t = manager.beginTransaction();
            resfragment = new ResFragment();

        //////////////////////////////////////////////////////////

            intent = getIntent();
            frag = intent.getIntExtra("res",2);

         /////////////////////////////////////////////////////////

        mapFragment = new MapFragment();
        resFragment = new ResFragment();
        camFragment = new CamFragment();

        if(frag == 2){
            setFragment(mapFragment);
        }else{
            setFragment(resFragment);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_map :
                            setFragment(mapFragment); break ;

                        case R.id.nav_reservation :

                            setFragment(resfragment);

                            Bundle bundle = new Bundle();
                            //bundle.putInt("km",frag);
                            bundle.putString("km","fml");
                            resfragment.setArguments(bundle);
                            t.add(R.id.fragment_container,resfragment);
                            t.commit();

                            break ;
                        case R.id.nav_camera :

                            setFragment(camFragment); break ;
                    }
                    return true;
                }
            };


    private void setFragment (Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
}

