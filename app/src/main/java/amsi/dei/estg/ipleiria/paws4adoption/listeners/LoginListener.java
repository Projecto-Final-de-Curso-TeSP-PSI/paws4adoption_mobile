package amsi.dei.estg.ipleiria.paws4adoption.listeners;

/**
 * Interface for the Login Listener
 */
public interface LoginListener {
    void onValidLogin(String token, String username);
    void onInvalidLogin();
}
