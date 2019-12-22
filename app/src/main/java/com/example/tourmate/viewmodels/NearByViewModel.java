package com.example.tourmate.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tourmate.nearby.NearByResponseBody;
import com.example.tourmate.repos.NearByRepos;

public class NearByViewModel extends ViewModel {


    private NearByRepos repository;

    public NearByViewModel(){
        repository = new NearByRepos();
    }

    public MutableLiveData<NearByResponseBody> getResponseFromRepo(String endUrl){
        return repository.getResponse(endUrl);
    }
}
