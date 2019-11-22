package com.example.tourmate.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.pojos.TourMateEventPojo;
import com.example.tourmate.repos.EventDBRepository;

import java.util.List;

public class EventViewModel extends ViewModel {
    private EventDBRepository eventDBRepository;
    public MutableLiveData<List<TourMateEventPojo>> eventListLD;
    public MutableLiveData<TourMateEventPojo> eventDetailsLD = new MutableLiveData<>();

    public EventViewModel() {

        eventDBRepository = new EventDBRepository();
        eventListLD = eventDBRepository.eventListLD;
    }

    public void SaveEvent(TourMateEventPojo eventPojo)
    {
        eventDBRepository.saveNewEventToFirebaseRTDB(eventPojo);
    }

    public void updateEvent(TourMateEventPojo eventPojo)
    {
        eventDBRepository.UpdateEvent(eventPojo);
    }

    public void DeleteEvent(TourMateEventPojo eventPojo)
    {
        eventDBRepository.DeleteEventFromEventDB(eventPojo);
    }



    public void getEventDetails(String eventID)
    {
       eventDetailsLD = eventDBRepository.getEventDetialbyEventID(eventID);

    }



}
