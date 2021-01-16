package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AttributeListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Attribute;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;

import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.EMAIL;
import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.PASSWORD;
import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.USERNAME;

public class UserProfileActivity extends AppCompatActivity implements AttributeListener {

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
        initAttributesSpinners(); // Gets the list of districts from the API
        SingletonPawsManager.getInstance(getApplicationContext()).setAttributeListener(this);
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

        if(!isTxtFiledValid(firstName)){
            etFirstName.setError("Tem de ter pelo menos 2 caracteres.");
            return;
        }

        if(!isTxtFiledValid(lastName)){
            etLastName.setError("Tem de ter pelo menos 2 caracteres.");
            return;
        }

        if(!isNIFValid(nif)){
            etNif.setError("Tem de ter exactemente 9 dígitos.");
            return;
        }

        if(!isPhoneValid(phone)){
            etPhone.setError("Tem de ter exactemente 9 dígitos.");
            return;
        }

        if(!isTxtFiledValid(street)){
            etStreet.setError("Tem de ter pelo menos 2 caracteres.");
            return;
        }

        if(!isPostalCodeValid(postalCode)){
            etPostalCode.setError("Tem de ter exactamente 4 dígitos.");
            return;
        }

        if(!isStreetCodeValid(streetCode)){
            etStreetCode.setError("Tem de ter exactamente 3 dígitos.");
            return;
        }

        if(!isTxtFiledValid(city)){
            etCity.setError("Tem de ter pelo menos 2 caracteres.");
            return;
        }

        int districtId = spinnerDistricts.getSelectedItemPosition();

        if (districtId == 0) {
            Toast.makeText(getApplicationContext(), "Tem de escolher um distrito.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile userProfile = new UserProfile(
                email, username,
                firstName, lastName, nif, phone,
                street, doorNumber, floor, postalCode, streetCode, city, districtId);

        userProfile.setPassword(password);

        SingletonPawsManager.getInstance(getApplicationContext()).addUserAPI(userProfile, getApplicationContext());

        finish();
    }

    private boolean isTxtFiledValid(String txtField){
        if(txtField == null){
            return false;
        }
        return txtField.length() >= 2;
    }

    private boolean isNIFValid(String nif){
        if(nif == null){
            return false;
        }
        return nif.length() == 9;
    }

    private boolean isPhoneValid(String phone){
        if(phone == null){
            return false;
        }
        return phone.length() == 9;
    }

    private boolean isPostalCodeValid(String postalCode){
        if(postalCode == null){
            return false;
        }
        return postalCode.length() == 4;
    }

    private boolean isStreetCodeValid(String streetCode){
        if(streetCode == null){
            return false;
        }
        return streetCode.length() == 3;
    }

    public void initAttributesSpinners(){
        SingletonPawsManager.getInstance(
                getApplicationContext()).getAttributesAPI(getApplicationContext(),
                RockChisel.ATTR_DISTRICT,
                RockChisel.ATTR_DISTRICT_SYMLINK,
                null);
    }

    /**
     * Initializaes all the spinners for the
     * @param spinner
     * @param promptMessage
     * @param listAttributes
     */
    private void initSpinner(Spinner spinner, String promptMessage, @Nullable List<Attribute> listAttributes){
        Attribute promptObject = new Attribute(-1, promptMessage);
        if(listAttributes != null){
            listAttributes.add(0, promptObject);
        }

        ArrayAdapter<Attribute> dataAdapterSpinnerDistrict = new ArrayAdapter<Attribute>(this, android.R.layout.simple_spinner_dropdown_item, listAttributes);

        dataAdapterSpinnerDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterSpinnerDistrict);
        spinner.setSelection(dataAdapterSpinnerDistrict.getPosition(promptObject));
    }

    @Override
    public void onReceivedAttributes(ArrayList<Attribute> attributes, String attributeType) {
        initSpinner(spinnerDistricts, "Selecione o distrito", attributes);
    }
}