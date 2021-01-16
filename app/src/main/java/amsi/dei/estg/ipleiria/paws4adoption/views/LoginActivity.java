package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.SignupListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Login;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

public class LoginActivity extends AppCompatActivity implements LoginListener, SignupListener {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SingletonPawsManager.getInstance(getApplicationContext()).setLoginListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).setSignupListener(this);
    }

    /**
     *Method that evaluates if the inserted Username is valid
     * @param username
     * @return true if username is valid
     */
    private boolean isUsernameValid(String username){
        if(username == null){
            return false;
        }
        return username.length() >= 4;
    }

    /**
     * Method that evaluates if the inserted password is valid
     * @param password
     * @return true if password is valid
     */
    private boolean isPasswordValid(String password){
        if(password == null){
            return false;
        }
        return password.length() >= 8;
    }

    /**
     * Method that validates the username and password and sends a request to the API for authentication
     *
     * @param view
     */
    public void onClickLogin(View view) {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(!isUsernameValid(username)) {
            etUsername.setError(getString(R.string.invalidUsername));
            return;
        }

        if(!isPasswordValid(password)){
            etPassword.setError(getString(R.string.invalidPassword));
            return;
        }

        SingletonPawsManager.getInstance(getApplicationContext()).tokenRequest(username, password, getApplicationContext());
    }

    /**
     * Method that implements an event for valid login of the loginListener
     * @param username
     */
    @Override
    public void onValidLogin(Login login, String username) {
        if(login != null){
            System.out.println("onLogin --->" + login.getId());
            Vault.saveUserPreferences(getApplicationContext(), login, username);

            Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onInvalidLogin() {
        etPassword.setError("username ou password inválidos");
    }

    public void onClickSignup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccessfullSignup(UserProfile userProfile) {
        Toast.makeText(getApplicationContext(), userProfile.getUsername() + ", foi registado com sucesso!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignupFail() {
        Toast.makeText(getApplicationContext(), "Erro ao criar utilizador. Tente novamente.", Toast.LENGTH_LONG).show();
    }
}