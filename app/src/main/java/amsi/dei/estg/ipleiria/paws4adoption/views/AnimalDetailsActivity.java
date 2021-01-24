package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalDetailListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;

public class AnimalDetailsActivity extends AppCompatActivity implements AnimalDetailListener {

    //################ INTENT PARAMETERS ################
    private int animal_id = -1;
    private String scenario = null;

    private TextView textView_name, textView_chipId, textView_nature, textView_breed, textView_sex, textView_size, textView_furColor, textView_furLength, textView_type, textView_createAt, textView_missingDate, textView_organization, textView_foundDate, textView_location;
    private LinearLayout layout_missingDate, layout_organization, layout_foundDate, layout_location;
    private Button btnLeft, btnRight;

    private Animal animal;
    private ImageView imgRetrato;

    //################ FAB'S ################
    private FloatingActionButton fabAdd, fabUp, fabDown;
    private TextView fabUpText, fabDownText;
    private boolean areFabsVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        scenario = getIntent().getStringExtra(RockChisel.SCENARIO);
        animal_id = getIntent().getIntExtra(RockChisel.ANIMAL_ID, 0);

        importGraphicalElements();

        implementListeners();

        SingletonPawsManager.getInstance(getApplicationContext()).setAnimalDetailListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).getAnimalAPI(getApplicationContext(), this.animal_id);

    }

    public void importGraphicalElements(){
        textView_name = findViewById(R.id.textView_detailsName);
        textView_chipId = findViewById(R.id.textView_detailsChipId);
        textView_nature = findViewById(R.id.textView_detailsNature);
        textView_breed = findViewById(R.id.textView_detailsBreed);
        textView_sex = findViewById(R.id.textView_detailsSex);
        textView_size = findViewById(R.id.textView_detailsSize);
        textView_furColor = findViewById(R.id.textView_detailsFurColor);
        textView_furLength = findViewById(R.id.textView_detailsFurLength);
        textView_type = findViewById(R.id.textView_detailsType);
        textView_createAt = findViewById(R.id.textView_detailsCreateAt);

        textView_missingDate = findViewById(R.id.textView_missingDate);
        textView_organization = findViewById(R.id.textView_detailsOrganization);
        textView_foundDate = findViewById(R.id.textView_detailsFoundDate);
        textView_location = findViewById(R.id.textView_detailsLocation);


        layout_missingDate = findViewById(R.id.layout_missingDate);
        layout_organization = findViewById(R.id.layout_organization);
        layout_foundDate = findViewById(R.id.layout_foundDate);
        layout_location = findViewById(R.id.layout_location);

        imgRetrato = findViewById(R.id.imgRetrato);

        fabAdd = findViewById(R.id.fabAdd);
        fabUp = findViewById(R.id.fabUp);
        fabDown = findViewById(R.id.fabDown);

        fabUp.setVisibility(View.GONE);
        fabDown.setVisibility(View.GONE);
    }

    public void implementListeners(){

        fabAdd.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!areFabsVisible) {
                        fabUp.show();
                        fabDown.show();
                        areFabsVisible = true;
                    } else {
                        fabUp.hide();
                        fabDown.hide();
                        areFabsVisible = false;
                    }
                }
            });

        fabUp.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;

                    switch(scenario){

                        case RockChisel.SCENARIO_GENERAL_LIST:
                            if(animal.getType().equals(RockChisel.ADOPTION_ANIMAL)){
                                intent = new Intent(getApplicationContext(), SubmRequestActivity.class);
                                intent.putExtra(SubmRequestActivity.REQUESTYPE, RockChisel.ADOPTION_REQUEST);
                                intent.putExtra(SubmRequestActivity.ANIMAL_ID, animal_id);
                                finish();
                                startActivity(intent);
                            }
                            break;

                        case RockChisel.SCENARIO_MY_LIST:
                            intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                            intent.putExtra(PostAnimalActivity.SCENARIO, animal.getType());
                            intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_UPDATE);
                            intent.putExtra(PostAnimalActivity.ANIMAL_ID, animal_id);
                            finish();
                            startActivity(intent);
                            break;

                    }
                }
            });

        fabDown.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;

                    switch(scenario) {

                        case RockChisel.SCENARIO_GENERAL_LIST:

                            switch (animal.getType()){

                                case RockChisel.ADOPTION_ANIMAL:
                                    if(animal.getType().equals(RockChisel.ADOPTION_ANIMAL)){
                                        intent = new Intent(getApplicationContext(), SubmRequestActivity.class);
                                        intent.putExtra(SubmRequestActivity.REQUESTYPE, RockChisel.TFF_REQUEST);
                                        intent.putExtra(SubmRequestActivity.ANIMAL_ID, animal_id);
                                        finish();
                                        startActivity(intent);
                                    }
                                    break;


                                case RockChisel.MISSING_ANIMAL:
                                case RockChisel.FOUND_ANIMAL:
                                    Toast.makeText(AnimalDetailsActivity.this, "Pedido de contato não implementado!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;

                        case RockChisel.SCENARIO_MY_LIST:

                            switch (animal.getType()) {
                                case RockChisel.MISSING_ANIMAL:
                                    SingletonPawsManager
                                        .getInstance(getApplicationContext())
                                        .deleteAnimalAPI(animal, RockChisel.MISSING_ANIMALS_API_SERVICE , Vault.getAuthToken(getApplicationContext()), getApplicationContext());
                                    break;
                                case RockChisel.FOUND_ANIMAL:
                                    SingletonPawsManager
                                        .getInstance(getApplicationContext())
                                        .deleteAnimalAPI(animal, RockChisel.FOUND_ANIMALS_API_SERVICE , Vault.getAuthToken(getApplicationContext()), getApplicationContext());
                                    break;

                            }

                            finish();
                            break;
                    }
                }
            });

}

    /**
     * Method that implements changes to the current activity according with the Scenario intended
     */
    public void setScenario(){

        if(!FortuneTeller.isInternetConnection(getApplicationContext())){
            fabAdd.setVisibility(View.GONE);
            fabUp.setVisibility(View.GONE);
            fabDown.setVisibility(View.GONE);
            return;
        }


        switch (scenario){

            case RockChisel.SCENARIO_GENERAL_LIST:
                switch(animal.getType()){
                    case RockChisel.SCENARIO_ADOPTION_ANIMAL:
                        setTitle(getString(R.string.title_adoption_animal));
                        fabUp.setImageResource(R.drawable.ic_baseline_family_restroom_24);
                        fabDown.setImageResource(R.drawable.ic_baseline_hourglass_top_24);
                        layout_organization.setVisibility(LinearLayout.VISIBLE);
                        textView_organization.setText(animal.getOrganization_name());
                        break;

                    case RockChisel.SCENARIO_MISSING_ANIMAL:
                        setTitle(getString(R.string.title_missing_animal));
                        fabDown.setImageResource(R.drawable.ic_baseline_phone_forwarded_24);
                        fabDown.show();
                        fabUp.setVisibility(View.INVISIBLE);
                        fabAdd.setVisibility(View.INVISIBLE);
                        layout_missingDate.setVisibility(LinearLayout.VISIBLE);
                        textView_missingDate.setText(animal.getMissingFound_date());
                        break;

                    case RockChisel.SCENARIO_FOUND_ANIMAL:
                        setTitle(getString(R.string.title_found_animal));
                        fabDown.setImageResource(R.drawable.ic_baseline_phone_forwarded_24);
                        fabDown.show();
                        fabUp.setVisibility(View.INVISIBLE);
                        fabAdd.setVisibility(View.INVISIBLE);
                        layout_foundDate.setVisibility(LinearLayout.VISIBLE);
                        textView_foundDate.setText(animal.getMissingFound_date());
                        layout_location.setVisibility(LinearLayout.VISIBLE);
                        textView_location.setText(
                                Wrench.encode("", animal.getOrganization_street(), " - ")+
                                        Wrench.encode("", animal.getFoundAnimal_city(), " ") +
                                        Wrench.encode("(", animal.getFoundAnimal_district_name(), ")")
                        );
                        break;
                }
                break;

            case RockChisel.SCENARIO_MY_LIST:

                switch(animal.getType()){
                    case RockChisel.SCENARIO_MISSING_ANIMAL:
                        layout_missingDate.setVisibility(LinearLayout.VISIBLE);
                        textView_missingDate.setText(animal.getMissingFound_date());
                        break;

                    case RockChisel.SCENARIO_FOUND_ANIMAL:
                        layout_foundDate.setVisibility(LinearLayout.VISIBLE);
                        textView_foundDate.setText(animal.getMissingFound_date());
                        layout_location.setVisibility(LinearLayout.VISIBLE);
                        textView_location.setText(
                                Wrench.encode("", animal.getOrganization_street(), " - ")+
                                        Wrench.encode("", animal.getFoundAnimal_city(), " ") +
                                        Wrench.encode("(", animal.getFoundAnimal_district_name(), ")")
                        );
                        break;
                }

                setTitle(getString(R.string.title_my_animal));
                fabUp.setImageResource(R.drawable.ic_baseline_edit_24);
                fabDown.setImageResource(R.drawable.ic_baseline_delete_24);
        }

    }

    /**
     * Method that fills the screen with the animal
     */
    public void fillAnimal(){
        setTitle(getString(R.string.title_details) + animal.getName());

        textView_name.setText(animal.getName());
        textView_chipId.setText(animal.getChipId());
        textView_nature.setText(animal.getNature_parent_name());
        textView_breed.setText(animal.getNature_name());
        textView_sex.setText(animal.getSex());
        textView_size.setText(animal.getSize());
        textView_furColor.setText(animal.getFur_color());
        textView_furLength.setText(animal.getFur_length());

        Glide.with(getApplicationContext())
                .load(animal.getPhoto())
                .placeholder(R.drawable.paws4adoption_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgRetrato);

        textView_createAt.setText(animal.getCreateAt());

    }

    @Override
    public void onGetAnimal(Animal animal) {
        this.animal = animal;

        if(animal != null){
            fillAnimal();
            setScenario();
        }
        else
            Toast.makeText(this, getString(R.string.error_received_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteAnimalFromList() {
        finish();
    }

    @Override
    public void onUpdateAnimalFromList() {
        finish();
    }
}