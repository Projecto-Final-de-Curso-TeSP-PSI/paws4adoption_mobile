package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

public class SignupActivity extends AppCompatActivity {

    private ImageView iv;
    private EditText usernameTxt, emailTxt, passwordTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setTitle(this.getTitle()+" - Registo");
        initComponents();

    }

    private void initComponents() {
        iv = findViewById(R.id.ivPawsLogo);
        usernameTxt = findViewById(R.id.etUsername);
        emailTxt = findViewById(R.id.etEmail);
        passwordTxt = findViewById(R.id.etPassword);
    }

    private boolean isUsernameValido(String username){
        if(username == null){
            return false;
        }
        return username.length() >= 4;
    }

    private boolean isEmailValido(String email) {
        if(email == null){
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String password) {
        if(password == null){
            return false;
        }
        return password.length() >= 8;
    }

    public void onClickContinuar(View view) {
        String username = usernameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if(!isUsernameValido(username)){
            usernameTxt.setError("Username tem de ter 4 ou mais caracteres.");
            return;
        }

        if(!isEmailValido(email)){
            emailTxt.setError("Email inválido");
            return;
        }

        if(!isPasswordValida(password)){
            passwordTxt.setError("A password tem de ter 8 ou mais caracteres.");
            return;
        }

        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.putExtra(RockChisel.USERNAME, username);
        intent.putExtra(RockChisel.EMAIL, email);
        intent.putExtra(RockChisel.PASSWORD, password);
        startActivity(intent);
        finish();
    }
}