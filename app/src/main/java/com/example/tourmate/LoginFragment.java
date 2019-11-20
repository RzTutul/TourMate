package com.example.tourmate;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

private EditText emailET,passwordET;
private Button loginbtn,registerbtn;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailET = view.findViewById(R.id.inputEmail);
        passwordET = view.findViewById(R.id.inputPassword);

        loginbtn = view.findViewById(R.id.loginbtn);
        registerbtn = view.findViewById(R.id.registrationBtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString();
                String pass = passwordET.getText().toString();

                Navigation.findNavController(view).navigate(R.id.action_loginFragment4_to_mainDashBoard3);

            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String pass = passwordET.getText().toString();
            }
        });



    }
}
