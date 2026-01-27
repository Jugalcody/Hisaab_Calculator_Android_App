package hisaab.store.analyser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

        private final MutableLiveData<String> userName = new MutableLiveData<>();
        private final MutableLiveData<String> profileImage = new MutableLiveData<>();

        public LiveData<String> getUserName() {
            return userName;
        }

        public LiveData<String> getProfileImage() {
            return profileImage;
        }

        public void setUserName(String name) {
            userName.setValue(name);
        }

        public void setProfileImage(String imageUrl) {
            profileImage.setValue(imageUrl);
        }
    }

