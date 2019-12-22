package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreen extends Fragment {


    int i;
    public SplashScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                doWOrk();
                StartApp();
            }

        });
        thread.start();
    }

    private void StartApp() {

        Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);

    }

    private void doWOrk() {

        for (i=1;i<=2;i=i+1) {
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
