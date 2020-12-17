package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.LoginListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Whrench;

public class LoginActivity extends AppCompatActivity implements LoginListener{

    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String USER_PREFERENCES = "user_preferences";

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

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
        String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(isUsernameValid(email)){
            etUsername.setError(getString(R.string.invalidUsername));
        }

        if(isPasswordValid(password)){
            etPassword.setError(getString(R.string.invalidPassword));
        }

        SingletonPawsManager.getInstance(getApplicationContext()).loginRequest(email, password, getApplicationContext());
    }

    /**
     * Method that implements a method from loginListener
     * @param token
     * @param username
     */
    @Override
    public void onLoginResquest(String token, String username) {
        if(token != null){
            saveSharedPreferencesUserInfo(token, username);

            /* Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            intent.putExtra(USERNAME, username);*/
            //startActivity(intent);

            finish();

        }
    }

    /**
     * Method for saving the USERNAME and TOKEN on the shared preferences
     * @param token
     * @param username
     */
    public void saveSharedPreferencesUserInfo(String token, String username){
        SharedPreferences sharedPreferencesInfoUser =
                getSharedPreferences(USER_PREFERENCES,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesInfoUser.edit();
        editor.putString(USERNAME, username);
        editor.putString(TOKEN, token);
        editor.apply();
    }

}