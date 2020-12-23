package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

public class LoginActivity extends AppCompatActivity implements LoginListener{

    private EditText etUsername;
    private EditText etPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        SingletonPawsManager.getInstance(getApplicationContext()).setLoginListener(this);
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
        return username.length() >= 8;
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

        if(isUsernameValid(username) == false) {
            etUsername.setError(getString(R.string.invalidUsername));
            return;
        }

        if(isPasswordValid(password) == false){
            etPassword.setError(getString(R.string.invalidPassword));
            return;
        }

        SingletonPawsManager.getInstance(getApplicationContext()).loginRequest(username, password, getApplicationContext());
    }

    /**
     * Method that implements an event for valid login of the loginListener
     * @param token
     * @param username
     */
    @Override
    public void onValidLogin(String token, String username) {
        if(token != null){
            Vault.saveUserPreferences(getApplicationContext(), token, username);

            Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onClickSignup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}