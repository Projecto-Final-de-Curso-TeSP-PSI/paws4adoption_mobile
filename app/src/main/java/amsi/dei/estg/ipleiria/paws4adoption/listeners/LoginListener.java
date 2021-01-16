package amsi.dei.estg.ipleiria.paws4adoption.listeners;

import amsi.dei.estg.ipleiria.paws4adoption.models.Login;

/**
 * Interface for the Login Listener
 */
public interface LoginListener {
    void onValidLogin(Login login, String username);
    void onInvalidLogin();
}
