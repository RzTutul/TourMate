package com.example.tourmate.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.repos.FirebaseRepository;

public class LoginViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;
    public MutableLiveData<AuthenticationState> stateLiveData;
    public MutableLiveData<String> errMsg = new MutableLiveData<>();

    public enum AuthenticationState
    {
        AUTHENTICATED,
        UNAUTHENTICATED,
    }
    public LoginViewModel() {
        stateLiveData = new MutableLiveData<>();
        firebaseRepository = new FirebaseRepository(stateLiveData);

        errMsg = firebaseRepository.getErrMsg();

        if (firebaseRepository.getFirebaseUser() != null)
        {
            stateLiveData.postValue(AuthenticationState.AUTHENTICATED);
        }
        else
        {
            stateLiveData.postValue(AuthenticationState.UNAUTHENTICATED);
        }
    }

    public void login(String email,String pass)
    {
       stateLiveData = firebaseRepository.LoginFirebaseUser(email,pass);
    }
    public void register(String email,String pass)
    {
        stateLiveData =  firebaseRepository.RegisterFireBaseUser(email,pass);
    }

}
