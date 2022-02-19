package com.example.overcooked.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.overcooked.model.Model;
import com.example.overcooked.model.User;

import java.util.List;

public class BaseActivityViewModel extends ViewModel {

    LiveData<List<User>> users;

    public BaseActivityViewModel(){
        users = Model.instance.getUsers();
    }

    public LiveData<User> getUserById(String uid) {
        MutableLiveData<User> userToReturn = new MutableLiveData<>();
        if (users.getValue() != null) {
            for(User user : users.getValue()){
                if(user.getUid().equals(uid)){
                    userToReturn.setValue(user);
                    return userToReturn;
                }
            }
        }
        return userToReturn;
    }

}
