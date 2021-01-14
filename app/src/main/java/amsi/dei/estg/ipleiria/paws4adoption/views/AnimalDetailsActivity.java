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

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;

import static amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel.SCENARIO;

public class AnimalDetailsActivity extends AppCompatActivity implements AnimalListener {

    //################ INTENT PARAMETERS ################
    public static final String ANIMAL_ID = "animal_id";
    private int animal_id = -1;

    public static final String SCENARIO = "scenario";
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

        animal_id = getIntent().getIntExtra(ANIMAL_ID, 0);
        scenario = getIntent().getStringExtra(SCENARIO);

        importGraphicalElements();

        implementListeners();

        SingletonPawsManager.getInstance(getApplicationContext()).setAnimalListener(this);
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

        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);

        fabAdd = findViewById(R.id.fabAdd);
        fabUp = findViewById(R.id.fabUp);
        fabDown = findViewById(R.id.fabDown);
        fabUpText = findViewById(R.id.fabUpText);
        fabDownText = findViewById(R.id.fabDownText);

        fabUp.setVisibility(View.GONE);
        fabUpText.setVisibility(View.GONE);
        fabDown.setVisibility(View.GONE);
        fabDownText.setVisibility(View.GONE);
    }

    public void implementListeners(){

        fabAdd.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!areFabsVisible) {
                        fabUp.show();
                        fabDown.show();
                        fabUpText.setVisibility(View.VISIBLE);
                        fabDownText.setVisibility(View.VISIBLE);

                        areFabsVisible = true;
                    } else {
                        fabUp.hide();
                        fabDown.hide();
                        fabUpText.setVisibility(View.GONE);
                        fabDownText.setVisibility(View.GONE);

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

                        case RockChisel.SCENARIO_ADOPTION_ANIMAL:
                            Toast.makeText(AnimalDetailsActivity.this, "Pedido de adoção não implementado!", Toast.LENGTH_SHORT).show();
                            break;

                        case RockChisel.SCENARIO_MISSING_ANIMAL:
                        case RockChisel.SCENARIO_FOUND_ANIMAL:
                            new Intent(getApplicationContext(), PostAnimalActivity.class);
                            intent.putExtra(PostAnimalActivity.SCENARIO, animal.getType());
                            intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_UPDATE);
                            intent.putExtra(PostAnimalActivity.ANIMAL_ID, animal_id);
                            break;

                        case RockChisel.SCENARIO_MY_ANIMAL:
                            new Intent(getApplicationContext(), PostAnimalActivity.class);
                            intent.putExtra(PostAnimalActivity.SCENARIO, RockChisel.SCENARIO_MY_ANIMAL);
                            intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_UPDATE);
                            intent.putExtra(PostAnimalActivity.ANIMAL_ID, animal_id);
                            break;

                    }

                    if(intent != null)
                        startActivity(intent);
                }
            });

        fabDown.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;

                    switch(scenario){

                        case RockChisel.SCENARIO_ADOPTION_ANIMAL:
                            Toast.makeText(AnimalDetailsActivity.this, "Pedido de FAT não implementado!", Toast.LENGTH_SHORT).show();
                            break;

                        case RockChisel.SCENARIO_MISSING_ANIMAL:
                        case RockChisel.SCENARIO_FOUND_ANIMAL:
                            Toast.makeText(AnimalDetailsActivity.this, "Peidod de contato não implementado!", Toast.LENGTH_SHORT).show();
                            break;

                        case RockChisel.SCENARIO_MY_ANIMAL:
                            SingletonPawsManager
                                    .getInstance(getApplicationContext())
                                    .deleteAnimalAPI(animal, animal.getType(), Vault.getAuthToken(getApplicationContext()), getApplicationContext());
                            break;

                    }

                    if(intent != null)
                        startActivity(intent);
                }
            });

}

    /**
     * Method that implements changes to the current activity according with the Scenario intended
     */
    public void setScenario(){

        switch (scenario){

            case RockChisel.SCENARIO_ADOPTION_ANIMAL:
                setTitle("Animal para Adoção");
                fabUp.setImageResource(R.drawable.ic_baseline_family_restroom_24);
                fabDown.setImageResource(R.drawable.ic_baseline_hourglass_top_24);
                layout_organization.setVisibility(LinearLayout.VISIBLE);
                textView_organization.setText(animal.getOrganization_name());
                break;

            case RockChisel.SCENARIO_MISSING_ANIMAL:
                setTitle("Animal desaparecido");
                fabDown.setImageResource(R.drawable.ic_baseline_phone_forwarded_24);
                fabDown.show();
                fabUp.setVisibility(View.INVISIBLE);
                fabAdd.setVisibility(View.INVISIBLE);
                layout_missingDate.setVisibility(LinearLayout.VISIBLE);
                textView_missingDate.setText(animal.getMissingFound_date());
                break;

            case RockChisel.SCENARIO_FOUND_ANIMAL:
                setTitle("Animal errante");
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

            case RockChisel.SCENARIO_MY_ANIMAL:
                setTitle("Meu Animal");
                fabUp.setImageResource(R.drawable.ic_baseline_family_restroom_24);
                fabDown.setImageResource(R.drawable.ic_baseline_delete_24);

                break;

            default:
                break;
        }

    }

    /**
     * Method that fills the screen with the animal
     */
    public void fillAnimal(){
        setTitle("Detalhes " + animal.getName());

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

    //################ ANIMAL LISTENER METHODS ################
    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList) {
        finish();
    }

    @Override
    public void onUpdateAnimalsList(Animal animal, int operation) {

    }

    @Override
    public void onGetAnimalAPI(Animal animal) {
        this.animal = animal;

        if(animal != null){
            fillAnimal();
            setScenario();
        }
        else
            Toast.makeText(this, getString(R.string.error_received_message), Toast.LENGTH_SHORT).show();
    }

    public void onClick_btnLeft(View view) {
    }

    public void onClick_btnRight(View view) {
    }
}