package com.example.tourmate;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourmate.pojos.UserInformationPojo;
import com.example.tourmate.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    TextInputEditText userNameET, userEmailET, userPassword, confirmPassword;
    LoginViewModel loginViewModel;
    private Button registerBbtn;
    private LinearLayout alreadyMember;
    public static ProgressBar registerProgressBar;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ///Move layouts up when soft keyboard is shown

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        userNameET = view.findViewById(R.id.userNameET);
        userEmailET = view.findViewById(R.id.userEmailEt);
        userPassword = view.findViewById(R.id.userPasswordET);
        confirmPassword = view.findViewById(R.id.userRetypePassword);
        registerBbtn = view.findViewById(R.id.button);
        alreadyMember = view.findViewById(R.id.alredyMemberBtn);
        registerProgressBar = view.findViewById(R.id.registerProgressBar);

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragmnet).navigate(R.id.loginFragment);
            }
        });

        registerBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userNameET.getText().toString();
                String email = userEmailET.getText().toString();
                String pass = userPassword.getText().toString();
                String retypePass = confirmPassword.getText().toString();

                if (name.isEmpty()) {
                    userNameET.setError("Provide a name");
                } else if (email.isEmpty()) {
                    userEmailET.setError("Provide a email");
                } else if (pass.isEmpty()) {
                    userPassword.setError("Provide a password");
                }
                else if (pass.length() < 6) {
                    userPassword.setError("Set minimum six length password");
                }

                else if (pass.equals(retypePass)) {
                    UserInformationPojo userInformationPojo = new UserInformationPojo(null, name, email, pass);
                    loginViewModel.register(userInformationPojo);

                }
                else {

                    confirmPassword.setError("Password not match!");

                }


            }
        });

        loginViewModel.stateLiveData.observe(getActivity(), new Observer<LoginViewModel.AuthenticationState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        Navigation.findNavController(view).navigate(R.id.eventListFragment);
                        break;
                    case UNAUTHENTICATED:
                        break;
                }
            }
        });

    }
}
