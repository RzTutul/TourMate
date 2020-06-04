package com.example.tourmate.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.pojos.DairyPojo;
import com.example.tourmate.repos.EventDairyRepos;

import java.util.List;

public class DairyViewModel extends ViewModel {

    public EventDairyRepos eventDairyRepos;
    public MutableLiveData<List<DairyPojo>> dairyLD = new MutableLiveData<>();
    public MutableLiveData<DairyPojo> dairyDetailsLD = new MutableLiveData<>();
    public DairyViewModel() {
        eventDairyRepos = new EventDairyRepos();

    }

    public void addDairy(DairyPojo dairyPojo)
    {
        eventDairyRepos.AddDairy(dairyPojo);
    }

    public void getAllDairy(String eventID)
    {
        dairyLD = eventDairyRepos.getAllDairyList(eventID);
    }

    public void getDairyDetails(String eventID, String dairyID) {

        dairyDetailsLD = eventDairyRepos.getDairyDetails(eventID,dairyID);
    }
}
