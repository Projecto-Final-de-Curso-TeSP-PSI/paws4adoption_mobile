package amsi.dei.estg.ipleiria.paws4adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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


    public void onClickSignup(View view) {
        String username = usernameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if(!isEmailValido(email)){
            emailTxt.setError("Email inválido");
        }

        if(!isPasswordValida(password)){
            passwordTxt.setError("A password não cumpre os requisitos");
        }

        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.putExtra(UserProfileActivity.USERNAME, username);
        intent.putExtra(UserProfileActivity.EMAIL, email);
        intent.putExtra(UserProfileActivity.PASSWORD, password);
        startActivity(intent);
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
}