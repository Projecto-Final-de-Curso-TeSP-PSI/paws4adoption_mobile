package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;

import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.EMAIL;
import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.PASSWORD;
import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.USERNAME;

public class UserProfileActivity extends AppCompatActivity implements UserProfileListener{

    private ImageView ivUserIcon;
    private EditText etFirstName, etLastName, etNif, etPhone,
                     etStreet, etDoorNumber, etFloor,
                     etPostalCode, etStreetCode, etCity;
    private Spinner spinnerDistricts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initComponents();

        SingletonPawsManager.getInstance(getApplicationContext()).setUserProfileListener(this);
    }

    private void initComponents(){
        ivUserIcon = findViewById(R.id.ivUserIcon);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etNif = findViewById(R.id.etNif);
        etPhone = findViewById(R.id.etPhone);
        etStreet = findViewById(R.id.etStreet);
        etDoorNumber = findViewById(R.id.etDoorNumber);
        etFloor = findViewById(R.id.etFloor);
        etPostalCode = findViewById(R.id.etPostalCode);
        etStreetCode = findViewById(R.id.etStreetCode);
        etCity = findViewById(R.id.etCity);
        spinnerDistricts = findViewById(R.id.spinnerDistricts);
    }

    public void onClickSignup(View view) {
        System.out.println("--> Click no registar");
        String username = getIntent().getStringExtra(USERNAME);
        String email = getIntent().getStringExtra(EMAIL);
        String password = getIntent().getStringExtra(PASSWORD);
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String nif = etNif.getText().toString();
        String phone = etPhone.getText().toString();
        String street = etStreet.getText().toString();
        String doorNumber = etDoorNumber.getText().toString();
        String floor = etFloor.getText().toString();
        String postalCode = etPostalCode.getText().toString();
        String streetCode = etStreetCode.getText().toString();
        String city = etCity.getText().toString();
        int districtId = spinnerDistricts.getSelectedItemPosition();

        UserProfile userProfile = new UserProfile(
                0, email, username,
                firstName, lastName, nif, phone,
                street, doorNumber, floor, postalCode, streetCode, city, districtId);

        userProfile.setPassword(password);

        SingletonPawsManager.getInstance(getApplicationContext()).addUserAPI(userProfile, getApplicationContext());
    }


    @Override
    public void onUserProfileRequest(UserProfile userProfile) {
        if(userProfile != null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}