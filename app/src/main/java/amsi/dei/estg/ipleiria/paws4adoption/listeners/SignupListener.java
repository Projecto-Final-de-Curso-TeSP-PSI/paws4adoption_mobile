package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;

public interface SignupListener {
    void onSuccessfullSignup(UserProfile userProfile);

    void onSignupFail();
}
