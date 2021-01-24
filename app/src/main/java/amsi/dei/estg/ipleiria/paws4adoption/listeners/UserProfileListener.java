package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;

public interface UserProfileListener {
    void onUserProfileRequest(UserProfile userProfile);
}
