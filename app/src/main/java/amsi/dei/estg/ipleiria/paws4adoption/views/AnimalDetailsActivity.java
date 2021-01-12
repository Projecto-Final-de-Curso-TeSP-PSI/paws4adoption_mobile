package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.text.Layout;
import android.widget.LinearLayout;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;

public class AnimalDetailsActivity extends AppCompatActivity {

    public static final String ANIMAL_DETAILS = "animal";

    private TextView textView_name, textView_chipId, textView_nature, textView_breed, textView_sex, textView_size, textView_furColor, textView_furLength, textView_type, textView_createAt, textView_missingDate, textView_organization, textView_foundDate, textView_location;
    private LinearLayout layout_missingDate, layout_organization, layout_foundDate, layout_location;

    private int id_animal;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        id_animal = getIntent().getIntExtra(ANIMAL_DETAILS, 0);
        animal = SingletonPawsManager.getInstance(getApplicationContext()).getAnimal(id_animal);

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



        if(animal != null){
            setTitle("Detalhes " + animal.getName());

            textView_name.setText(animal.getName());
            textView_chipId.setText(animal.getChipId());
            textView_nature.setText(animal.getNature_parent_name());
            textView_breed.setText(animal.getNature_name());
            textView_sex.setText(animal.getSex());
            textView_size.setText(animal.getSize());
            textView_furColor.setText(animal.getFur_color());
            textView_furLength.setText(animal.getFur_length());
            switch (animal.getType()){
                case "adoptionAnimal":
                    textView_type.setText("Animal para Adoção");
                    layout_organization.setVisibility(LinearLayout.VISIBLE);
                    textView_organization.setText(animal.getOrganization_name());
                    break;
                case "missingAnimal":
                    textView_type.setText("Animal Desaparcido");
                    layout_missingDate.setVisibility(LinearLayout.VISIBLE);
                    textView_missingDate.setText(animal.getMissingFound_date());
                    break;
                case "foundAnimal":
                    textView_type.setText("Animal Errante");
                    layout_foundDate.setVisibility(LinearLayout.VISIBLE);
                    textView_foundDate.setText(animal.getMissingFound_date());
                    layout_location.setVisibility(LinearLayout.VISIBLE);
                    textView_location.setText(
                            Wrench.encode("", animal.getOrganization_street(), " - ")+
                            Wrench.encode("", animal.getFoundAnimal_city(), " ") +
                            Wrench.encode("(", animal.getFoundAnimal_district_name(), ")")
                    );
                    break;
                default:
                    break;
            }

            textView_createAt.setText(animal.getCreateAt());
        }else {
            finish();
        }

    }


}