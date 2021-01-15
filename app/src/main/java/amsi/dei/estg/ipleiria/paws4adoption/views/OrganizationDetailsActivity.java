package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;

public class OrganizationDetailsActivity extends AppCompatActivity {

    public static final String ORG_DETAILS = "organization";

    private ImageView ivOrgLogoDetails;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvPostalCode;
    private TextView tvDistrict;
    private TextView tvEmail;
    private TextView tvPhone;

    private int id_organization;
    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        id_organization = getIntent().getIntExtra(ORG_DETAILS, 0);
        organization = SingletonPawsManager.getInstance(getApplicationContext()).getOrganizationDB(id_organization);

        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPostalCode = findViewById(R.id.tvPostalCode);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);

        if(organization != null){
            setTitle("Detalhes: " + organization.getName());

            tvName.setText(organization.getName());
            tvAddress.setText(
                Wrench.encode("", organization.getStreet(), " ")+
                Wrench.encode("", organization.getDoor_number(), " ") +
                Wrench.encode("", organization.getFloor(), " ")
            );
            tvPostalCode.setText(
                Wrench.encode("", organization.getPostal_code() + "", " ") +
                Wrench.encode(" - ", organization.getStreet_code() + "", " ") +
                Wrench.encode("", organization.getCity() + "", " ")
            );
            tvDistrict.setText(organization.getDistrict_name());
            tvEmail.setText(organization.getEmail());
            tvPhone.setText(organization.getPhone());
        } else{
            finish();
        }
    }


    /**
     *
     * @param view
     */
    public void onClickGetAnimals(View view) {

    }

    private void dialogSearchByDistrict(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        builder
            .setTitle("Pesquisar por distrito")
            .setMessage("Selecione o distrito");




    }
}