package com.example.tourmate.repos;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.tourmate.pojos.MomentPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MomentRepository {
    private FirebaseUser firebaseUser;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference momentsRef;
    public MutableLiveData<List<MomentPojo>> momentsLD = new MutableLiveData<>();

    public MomentRepository() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(firebaseUser.getUid());
        momentsRef = userRef.child("Moments");


    }

    public void addNewMoment(MomentPojo moments){
        String momentId = momentsRef.push().getKey();
        moments.setMomentId(momentId);
        momentsRef.child(moments.getEventId())
                .child(momentId)
                .setValue(moments);
    }


    public MutableLiveData<List<MomentPojo>> getAllMoments(String eventId){
        momentsRef.child(eventId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MomentPojo> moments = new ArrayList<>();
                        for (DataSnapshot d : dataSnapshot.getChildren()){
                            moments.add(d.getValue(MomentPojo.class));
                        }
                        momentsLD.postValue(moments);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return momentsLD;
    }
}
