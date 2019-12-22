package com.example.tourmate;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tourmate.viewmodels.LoginViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText emailET,passwordET;
    private Button loginbtn;
    private LinearLayout registerbtn,forgotPassword;
    private TextView showError;
    private LoginViewModel loginViewModel;
    public static ProgressBar loginProgressBar;
    public static Context context;




    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        (getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        emailET = view.findViewById(R.id.inputEmail);
        passwordET = view.findViewById(R.id.inputPassword);

        showError = view.findViewById(R.id.showErroText);

        loginbtn = view.findViewById(R.id.loginbtn);
        registerbtn = view.findViewById(R.id.registrationBtn);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        loginProgressBar = view.findViewById(R.id.loginProgressbar);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterEmailDialog();
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString();
                String pass = passwordET.getText().toString();

                if (email.isEmpty() && pass.isEmpty())
                {
                    emailET.setError("Input a email Address");
                    passwordET.setError("Input a password");
                }
                else
                {
                    loginViewModel.login(email,pass);
                }



            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                v.startAnimation(shake);
              Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.action_loginFragment_to_registrationFragment);

            }
        });




        loginViewModel.stateLiveData.observe(this, new Observer<LoginViewModel.AuthenticationState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                switch (authenticationState)
                {
                    case AUTHENTICATED:
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragmnet).navigate(R.id.splashScreen);
                        break;
                    case UNAUTHENTICATED:
                        break;
                }
            }
        });


        loginViewModel.errMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showError.setText(s);
            }
        });


    }

    private void showEnterEmailDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Password Reset!");

        LayoutInflater  inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.password_reset_dialog,null);

        builder.setView(view);


        final EditText emailET = view.findViewById(R.id.emailAddress);



        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailAddress =emailET.getText().toString();

                if (emailAddress.isEmpty())
                {
                    emailET.setError("Enter your email!");
                }
                else
                {
                    loginViewModel.passwordReset(emailAddress);
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
