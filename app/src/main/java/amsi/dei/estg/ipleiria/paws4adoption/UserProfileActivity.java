package amsi.dei.estg.ipleiria.paws4adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;

public class UserProfileActivity extends AppCompatActivity implements UserProfileListener {

    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }


    @Override
    public void onUserProfileRequest(UserProfile userProfile) {

    }
}