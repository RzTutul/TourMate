package com.example.tourmate.repos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.tourmate.pojos.DairyPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EventDairyRepos {
    MutableLiveData<List<DairyPojo>> dairyLD =new MutableLiveData<>();
    MutableLiveData<DairyPojo> dairyDetailsLD =new MutableLiveData<>();

    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference dairyRef;

    public EventDairyRepos() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        dairyRef = userRef.child("EventDairy");

        dairyRef.keepSynced(true);
    }

    public void AddDairy(DairyPojo dairyPojo)
    {
        String dairyID = dairyRef.push().getKey();
        dairyPojo.setDairyID(dairyID);
        dairyRef.child(dairyPojo.getEventID()).child(dairyID).setValue(dairyPojo);

    }

    public MutableLiveData<List<DairyPojo>> getAllDairyList(String eventID)
    {
        dairyRef.child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DairyPojo> dairyPojoList = new ArrayList<>();
                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    dairyPojoList.add(d.getValue(DairyPojo.class));

                }
                dairyLD.postValue(dairyPojoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return dairyLD;
    }

    public MutableLiveData<DairyPojo> getDairyDetails(String eventID, String dairyID) {


        dairyRef.child(eventID).child(dairyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DairyPojo dairyPojoList = dataSnapshot.getValue(DairyPojo.class);
                dairyDetailsLD.postValue(dairyPojoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return dairyDetailsLD;
    }

    public void updateDairyRepos(DairyPojo dairyPojo) {

        dairyRef.child(dairyPojo.getEventID()).child(dairyPojo.getDairyID()).setValue(dairyPojo);

    }

    public void deleteDairyRepos(String eventID, String dairyID) {

        dairyRef.child(eventID).child(dairyID).removeValue();

    }
}
