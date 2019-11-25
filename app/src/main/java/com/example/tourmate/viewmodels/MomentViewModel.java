package com.example.tourmate.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.pojos.MomentPojo;
import com.example.tourmate.repos.MomentRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

public class MomentViewModel extends ViewModel {
    private MomentRepository momentRepository;
  public MutableLiveData<List<MomentPojo>> momentsLD = new MutableLiveData<>();

    public MomentViewModel() {

        momentRepository = new MomentRepository();
    }

    public void uploadImageToFirebaseStorage(File file, final String eventId) {
        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
        Uri fileUri = Uri.fromFile(file);
        final StorageReference imageRef = rootRef.child("EventImages/" + fileUri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(fileUri);



        //For get URI Link of Image

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.e("moment", "upload completed");
                    Uri downloadUri = task.getResult();
                    MomentPojo moments = new MomentPojo(null, eventId, downloadUri.toString());
                    momentRepository.addNewMoment(moments);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    public void getMoments(String eventId){

        momentsLD = momentRepository.getAllMoments(eventId);
    }
}
