package com.example.tourmate.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.repos.FirebaseDBRepository;

import java.util.List;

public class EventViewModel extends ViewModel {
    private FirebaseDBRepository firebaseDBRepository;
    public MutableLiveData<List<TourMateEventPojo>> eventListLD;


    public EventViewModel() {

        firebaseDBRepository = new FirebaseDBRepository();
        eventListLD = firebaseDBRepository.eventListLD;
    }

    public void SaveEvent(TourMateEventPojo eventPojo)
    {
        firebaseDBRepository.saveNewEventToFirebaseRTDB(eventPojo);
    }


}
