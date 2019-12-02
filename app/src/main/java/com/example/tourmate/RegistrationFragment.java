package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tourmate.pojos.UserInformationPojo;
import com.example.tourmate.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    TextInputEditText userNameET,userEmailET,userPassword;
    LoginViewModel loginViewModel;
    private Button registerBbtn;
    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userNameET = view.findViewById(R.id.userNameET);
        userEmailET = view.findViewById(R.id.userEmailEt);
        userPassword = view.findViewById(R.id.userPasswordET);
        registerBbtn = view.findViewById(R.id.button);

        registerBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userNameET.getText().toString();
                String email = userEmailET.getText().toString();
                String pass = userPassword.getText().toString();

                UserInformationPojo userInformationPojo = new UserInformationPojo(null,name,email,pass);

                loginViewModel.register(userInformationPojo);


            }
        });

    }
}
