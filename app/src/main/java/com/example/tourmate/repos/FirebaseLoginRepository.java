package com.example.tourmate.repos;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.pojos.UserInformationPojo;
import com.example.tourmate.viewmodels.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirebaseLoginRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference rootRef;
    DatabaseReference  userRef;
    DatabaseReference userInfo;


    private MutableLiveData<LoginViewModel.AuthenticationState> stateLiveData;
    private MutableLiveData<String> errMsg = new MutableLiveData<>();
    private MutableLiveData<UserInformationPojo> userInfoLD = new MutableLiveData<>();

    public FirebaseLoginRepository(MutableLiveData<LoginViewModel.AuthenticationState> stateLiveData) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        this.stateLiveData = stateLiveData;


    }

    public MutableLiveData<LoginViewModel.AuthenticationState> LoginFirebaseUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                firebaseUser = firebaseAuth.getCurrentUser();
                stateLiveData.postValue(LoginViewModel.AuthenticationState.AUTHENTICATED);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                stateLiveData.postValue(LoginViewModel.AuthenticationState.UNAUTHENTICATED);
                errMsg.postValue(e.getLocalizedMessage());


            }
        });

        return stateLiveData;
    }

    public MutableLiveData<LoginViewModel.AuthenticationState> RegisterFireBaseUser(final UserInformationPojo userInformationPojo) {
        firebaseAuth.createUserWithEmailAndPassword(userInformationPojo.getUserEmail(), userInformationPojo.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    stateLiveData.postValue(LoginViewModel.AuthenticationState.AUTHENTICATED);
                    Log.i(TAG, "onComplete: "+firebaseUser.getUid());

                    rootRef = FirebaseDatabase.getInstance().getReference();
                    userRef = rootRef.child(firebaseUser.getUid());
                    userInfo = userRef.child("Loginfo");

                    String userId = firebaseUser.getUid();
                    userInformationPojo.setUesrID(userId);

                    userInfo.setValue(userInformationPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });


                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                stateLiveData.postValue(LoginViewModel.AuthenticationState.UNAUTHENTICATED);
                errMsg.postValue(e.getLocalizedMessage());


            }
        });

        return stateLiveData;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public MutableLiveData<String> getErrMsg() {
        return errMsg;
    }


    public MutableLiveData<UserInformationPojo> getUserInformation() {

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        userInfo = userRef.child("Loginfo");

        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformationPojo userInfo = dataSnapshot.getValue(UserInformationPojo.class);

                userInfoLD.postValue(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return userInfoLD;
    }
}
