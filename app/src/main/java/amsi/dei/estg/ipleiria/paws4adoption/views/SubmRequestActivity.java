package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AdoptionListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UserProfileListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalDetailListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Adoption;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.models.UserProfile;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;

public class SubmRequestActivity extends AppCompatActivity implements UserProfileListener, AdoptionListener, AnimalDetailListener {

    public static final String REQUESTYPE = "requestType";
    private String requestType = null;
    public static final String ACTION = "action";
    private String action = null;
    public static final String ANIMAL_ID = "animal";
    private int animal_id = -1;

    private FloatingActionButton fab;

    private ImageView ivPhoto;
    private TextView tvName, tvAddress, tvContact, tvEmail, tvAdoptionTitle;
    private EditText etMotivation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subm_request);

        requestType = getIntent().getStringExtra(REQUESTYPE);
        action = getIntent().getStringExtra(ACTION);
        animal_id = getIntent().getIntExtra(ANIMAL_ID, 0);

        importGraphicalElements();

        implementListeners();
        SingletonPawsManager.getInstance(getApplicationContext()).setUserProfileListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).setAdoptionListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).setAnimalDetailListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).getUserAPI(getApplicationContext(), Vault.getIdLoggedUser(getApplicationContext()), Vault.getAuthToken(getApplicationContext()));
        SingletonPawsManager.getInstance(getApplicationContext()).getAnimalAPI(getApplicationContext(), animal_id);

    }

    private void importGraphicalElements() {
        ivPhoto = findViewById(R.id.imgAnimalPhoto);
        tvAdoptionTitle = findViewById(R.id.tvAdoptionTitle);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvContact = findViewById(R.id.tvContanct);
        tvEmail = findViewById(R.id.tvEmail);
        etMotivation = findViewById(R.id.etMotivation);
        fab = findViewById(R.id.fabSubmit);
    }

    private void implementListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Adoption newAdoptionPost = validateAdoption();
                if (newAdoptionPost == null) {
                    Toast.makeText(SubmRequestActivity.this, "Erro ao validar o formulario de adoção", Toast.LENGTH_SHORT).show();
                    return;
                }

                SingletonPawsManager.getInstance(getApplicationContext()).insertAdoptionAPI(newAdoptionPost, RockChisel.ADOPTIONS_API_SERVICE, Vault.getAuthToken(getApplicationContext()), getApplicationContext());

            }
        });
    }

    public void fillForm() {

    }

    public Adoption validateAdoption() {
        Adoption newAdoptionPost = new Adoption();
        try {
            String motivation = etMotivation.getText().toString();
            if (motivation.length() < 10) {
                etMotivation.setError("Introduza uma motivação valida (min. 10 caracteres)");
                return null;
            }
            newAdoptionPost.setAdopter_id(Vault.getIdLoggedUser(getApplicationContext()));
            newAdoptionPost.setAdopted_animal_id(animal_id);
            newAdoptionPost.setMotivation(motivation);
            newAdoptionPost.setType(requestType);
        } catch (Exception e) {
            return null;
        }
        return newAdoptionPost;
    }

    @Override
    public void onUserProfileRequest(UserProfile userProfile) {
        tvName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
                tvAddress.setText(Wrench.encode("", userProfile.getStreet(), " ") +
                Wrench.encode("", userProfile.getDoorNumber(), " ") +
                Wrench.encode("", userProfile.getFloor(), " "));
        tvContact.setText(userProfile.getPhone());
        tvEmail.setText(userProfile.getEmail());
    }

    @Override
    public void onRequestSuccess(String action, Adoption adoption) {

    }

    @Override
    public void onRequestError(String message) {

    }

    @Override
    public void onCreateAdoption() {
        Toast.makeText(this, "Formulario submetido com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onGetAnimal(Animal animal) {
        String type = null;
        if(requestType.equals("adoption")){
            type = "adoção";
        }else if(requestType.equals("fat")){
            type = "FAT";
        }
        tvAdoptionTitle.setText("Formulario de " + type + " do " + animal.getName());
        Glide.with(getApplicationContext())
                .load(animal.getPhoto())
                .placeholder(R.drawable.paws4adoption_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPhoto);
    }

    @Override
    public void onDeleteAnimalFromList() {

    }

    @Override
    public void onUpdateAnimalFromList() {

    }
}