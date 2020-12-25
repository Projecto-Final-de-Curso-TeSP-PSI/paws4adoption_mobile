package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.AttributeListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.Attribute;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.services.FetchAddressIntentService;
import amsi.dei.estg.ipleiria.paws4adoption.R;

public class PostAnimalActivity extends AppCompatActivity implements AttributeListener {

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int LOCATION_REQUEST = 3;

    public static final String SCENARIO = "scenario";
    private String scenario = "";

    public static final String ACTION = "action";
    private String action = "";

    private Animal newAnimalPost;

    private LinearLayout llMissingAnimal;
    private LinearLayout llFoundAnimal;

    private ImageView ivPhoto;
    private Button btnUploadPhoto;
    private Button btnLocation;
    private FloatingActionButton fab;

    private EditText etName;
    private EditText etChipId;
    private EditText etDescription;

    ArrayAdapter<Attribute> dataAdapterNature;
    private Spinner spNature;
    ArrayAdapter<Attribute> dataAdapterBreed;
    private Spinner spBreed;
    ArrayAdapter<Attribute> dataAdapterFurLength;
    private Spinner spFurLength;
    ArrayAdapter<Attribute> dataAdapterFurColor;
    private Spinner spFurColor;
    ArrayAdapter<Attribute> dataAdapterSize;
    private Spinner spSize;
    ArrayAdapter<Attribute> dataAdapterSex;
    private Spinner spSex;

    private EditText etMissingFoundDate;

    private EditText etLocationStreet;
    private EditText etLocationCity;
    ArrayAdapter<Attribute> dataAdapterLocationDistrict;
    private Spinner spLocationDistrict;

    private TextView tvLat;
    private TextView tvLong;
    private TextView tvAddress;

    private ResultReceiver resultReceiver;

    double latitude;
    double longitude;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_animal);

        scenario = getIntent().getStringExtra(SCENARIO);
        action = getIntent().getStringExtra(ACTION);

        importGraphicalElements();

        implementListeners();

        setScenario();

        SingletonPawsManager.getInstance(getApplicationContext()).setAttributeListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SPECIE, RockChisel.ATTR_SPECIE_SYMLINK, null);

        resultReceiver = new AddressResultReceiver(new Handler());
    }

    private void importGraphicalElements() {


        llFoundAnimal = findViewById(R.id.llFoundAnimal);

        ivPhoto = findViewById(R.id.ivPhoto);

        etName = findViewById(R.id.etName);
        etChipId = findViewById(R.id.etChipId);
        etDescription = findViewById(R.id.etDescription);

        spNature = findViewById(R.id.spNature);

        spBreed = findViewById(R.id.spBreed);
        spFurLength = findViewById(R.id.spFurLenght);
        spFurColor = findViewById(R.id.spFurColor);
        spSize = findViewById(R.id.spSize);

        spSex = findViewById(R.id.spSex);
        ArrayList<Attribute> sexArray = new ArrayList<>();
        sexArray.add(new Attribute(1, "M" ));
        sexArray.add(new Attribute(2, "F" ));
        initSpinner(dataAdapterSex, spSex, "Selecione o sexo", sexArray);


        etMissingFoundDate = findViewById(R.id.etMissingFoundDate);

        etLocationStreet = findViewById(R.id.etLocationStreet);
        etLocationCity = findViewById(R.id.etLocationCity);
        spLocationDistrict = findViewById(R.id.spLocationDistrict);

        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        tvAddress = findViewById(R.id.tvAddress);

        btnUploadPhoto = findViewById(R.id.btnUpload);
        fab = findViewById(R.id.fabSubmit);
        btnLocation = findViewById(R.id.btnLocation);

    }

    private void implementListeners(){

        spNature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Attribute att = (Attribute) spNature.getItemAtPosition(i);
                Integer att_id = att.getId();
                if(att_id == -1){
                    return;
                }

                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SUBSPECIE, RockChisel.ATTR_SUBSPECIE_SYMLINK, att_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostAnimalActivity.this);

                builder.setTitle("Selecione uma opção para carregar a imagem");
                String[] options = {"Galeria", "Camera"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= 23 &&
                                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(PostAnimalActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            GALLERY_REQUEST
                                    );

                                    return;
                                }

                                uploadPhotoGallery();
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= 23 &&
                                        (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

                                    ActivityCompat.requestPermissions(PostAnimalActivity.this,
                                            new String[]{
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA
                                            },
                                            CAMERA_REQUEST
                                    );

                                    return;
                                }

                                takePhotoCamera();
                                break;
                        }
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!getValidatedAnimal()){
                    return;
                }

                switch(scenario){
                    case RockChisel.SCENARIO_MISSING_ANIMAL:
                        SingletonPawsManager.getInstance(getApplicationContext()).insertAnimalAPI(newAnimalPost, RockChisel.MISSING_ANIMALS_API_SERVICE, getApplicationContext());
                        break;
                    case RockChisel.SCENARIO_FOUND_ANIMAL:
                        SingletonPawsManager.getInstance(getApplicationContext()).insertAnimalAPI(newAnimalPost, RockChisel.FOUND_ANIMALS_API_SERVICE, getApplicationContext());
                        break;
                }
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(PostAnimalActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                            },
                            LOCATION_REQUEST);
                }

                getLocation();
            }
        });
    }

    /**
     * Method that implements changes to the current activity according the Scenario an Action intended
     */
    private void setScenario() {
        switch(scenario){
            case RockChisel.SCENARIO_MISSING_ANIMAL:
                setTitle("Publicar animal desaparecido");
                etMissingFoundDate.setHint("Data do desaparecimento");
                break;
            case RockChisel.SCENARIO_FOUND_ANIMAL:
                setTitle("Publicar animal abandonado");
                etMissingFoundDate.setHint("Data do avistamento");
                llFoundAnimal.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadPhotoGallery();
                }
                break;

            case CAMERA_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoCamera();
                }
                break;

            case LOCATION_REQUEST:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }
                }
                break;
        }
    }

    private void uploadPhotoGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, GALLERY_REQUEST);
        }
    }

    private void takePhotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(PostAnimalActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    },
                    LOCATION_REQUEST);
        }

        LocationServices.getFusedLocationProviderClient(PostAnimalActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(PostAnimalActivity.this)
                                .removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            tvLat.setText("" + latitude);
                            tvLong.setText("" + longitude);

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);

                        } else {
                            //
                        }
                    }
                }, Looper.getMainLooper());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    String[] paths = new String[]{currentPhotoPath};
                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    ivPhoto.setImageBitmap(bitmap);

                    MediaScannerConnection.scanFile(this, paths, null,
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {
                                Log.d("Detalhes", "onScanCompleted");
                            }

                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("Detalhes", "onScanCompleted");
                            }
                        });
                }
                break;

            case CAMERA_REQUEST:

                if (resultCode == RESULT_OK) {
                    String[] paths = new String[]{currentPhotoPath};
                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    ivPhoto.setImageBitmap(bitmap);

                    MediaScannerConnection.scanFile(this, paths, null,
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {
                                Log.d("Detalhes", "onScanCompleted");
                            }

                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("Detalhes", "onScanCompleted");
                            }
                        });

                }
                break;
            case LOCATION_REQUEST:
                //getLocation();
                break;
        }
    }

    @Override
    public void onReceivedAttributes(ArrayList<Attribute> attributes, String attributeType) {

        switch(attributeType){
            case RockChisel.ATTR_SPECIE:
                initSpinner(dataAdapterNature, spNature, "Selecione a espécie", attributes);
                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_LENGTH, RockChisel.ATTR_FUR_LENGTH_SYMLINK, null);
                break;

            case RockChisel.ATTR_SUBSPECIE:
                initSpinner(dataAdapterBreed, spBreed, "Selecione a sub espécie", attributes);
                break;

            case RockChisel.ATTR_FUR_LENGTH:
                initSpinner(dataAdapterFurLength, spFurLength, "Selecione o tipo de pelagem", attributes);
                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_COLOR, RockChisel.ATTR_FUR_COLOR_SYMLINK, null);
                break;

            case RockChisel.ATTR_FUR_COLOR:
                initSpinner(dataAdapterFurColor, spFurColor, "Selecione a cor da pelagem", attributes);
                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SIZE, RockChisel.ATTR_SIZE_SYMLINK, null);
                break;

            case RockChisel.ATTR_SIZE:
                initSpinner(dataAdapterSize, spSize, "Selecione o porte", attributes);
                if(scenario.equals(RockChisel.SCENARIO_FOUND_ANIMAL))
                    SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_DISTRICT, RockChisel.ATTR_DISTRICT_SYMLINK, null);
                break;

            case RockChisel.ATTR_DISTRICT:
                initSpinner(dataAdapterLocationDistrict, spLocationDistrict, "Selecione o distrito", attributes);
                break;
        }

    }

    private class AddressResultReceiver extends ResultReceiver{

        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == RockChisel.SUCCESS_RESULT){
                tvAddress.setText(resultData.getString(RockChisel.RESULT_DATAKEY));
            } else{
                Toast.makeText(PostAnimalActivity.this, resultData.getString(RockChisel.RESULT_DATAKEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(RockChisel.RECEIVER, resultReceiver);
        intent.putExtra(RockChisel.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgNomeFich = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getParentFile(),"Animals");
        storageDir.mkdirs();
        File image = File.createTempFile(imgNomeFich, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void initSpinner(ArrayAdapter<Attribute> spinnerArrayAdapter, Spinner spinner, String promptMessage, @Nullable List<Attribute> listAttributes){
        Attribute promptObject = new Attribute(-1, promptMessage);
        if(listAttributes == null){
            ArrayList<Attribute> auxListAttributes= new ArrayList<>();
            auxListAttributes.add(promptObject);
            spinnerArrayAdapter = new ArrayAdapter<Attribute>(this, android.R.layout.simple_spinner_dropdown_item, auxListAttributes);
        }
        else{
            spinnerArrayAdapter = new ArrayAdapter<Attribute>(this, android.R.layout.simple_spinner_dropdown_item, listAttributes);
            spinnerArrayAdapter.add(promptObject);
        }
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(spinnerArrayAdapter.getPosition(promptObject));
    }

    public boolean getValidatedAnimal() {

        try{
            String name = etName.getText().toString();
            if(name.length() < 5){
                etName.setError("Introduza um nome válido (mínimo 2 letras)");
                return false;
            }

            String chipId = etChipId.getText().toString();
            if(chipId.length() != 15){
                etChipId.setError("Introduza um chip id válido (15 dígitos))");
                return false;
            }

            String description = etDescription.getText().toString();
            if(description.length() < 10){
                etChipId.setError("Introduza uma descrição válida (min. 10 caracteres))");
                return false;
            }

            Attribute specie = (Attribute)spNature.getSelectedItem();
            if(specie.getId() == -1){
                Toast.makeText(this, "Selecione uma espécie", Toast.LENGTH_SHORT).show();
                return false;
            }

            Attribute subSpecie = (Attribute)spBreed.getSelectedItem();
            if(subSpecie.getId() == -1){
                Toast.makeText(this, "Selecione uma espécie", Toast.LENGTH_SHORT).show();
                return false;
            }

            Attribute furColor = (Attribute)spFurColor.getSelectedItem();
            if(furColor.getId() == -1){
                return false;
            }

            Attribute furLength = (Attribute)spFurLength.getSelectedItem();
            if(furLength.getId() == -1){
                return false;
            }

            Attribute size = (Attribute)spSize.getSelectedItem();
            if(size.getId() == -1){
                return false;
            }

            Attribute sex = (Attribute)spSex.getSelectedItem();
            if(sex.getId() == -1){
                return false;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date missingFoundDate;
            String missingFoundDateStr = etMissingFoundDate.getText().toString();
            try {
                missingFoundDate = simpleDateFormat.parse(missingFoundDateStr);
                System.out.println(missingFoundDate);
            } catch (ParseException e) {
                etMissingFoundDate.setError("Data inválida");
                e.printStackTrace();
                return false;
            }

            if(missingFoundDate.after(Calendar.getInstance().getTime())){
                Toast.makeText(this, "A data não pode ser superior à atual", Toast.LENGTH_SHORT).show();
                return false;
            }

            newAnimalPost = new Animal();
            newAnimalPost.setName(name);
            newAnimalPost.setChipId(chipId);
            newAnimalPost.setDescription(description);
            newAnimalPost.setNature_id(specie.getId());
            newAnimalPost.setFur_length_id(subSpecie.getId());
            newAnimalPost.setFur_color_id(furColor.getId());
            newAnimalPost.setSize_id(size.getId());
            newAnimalPost.setSex(sex.getName());
            newAnimalPost.setMissingFound_date(missingFoundDate.toString());
            //animal.setPhoto();

            if(scenario.equals(RockChisel.SCENARIO_FOUND_ANIMAL)){

                String locationStreet = etLocationStreet.getText().toString();
                if(name.length() < 5){
                    etName.setError("Introduza uma rua válida (mínimo 2 letras)");
                    return false;
                }

                String location_city = etLocationCity.getText().toString();
                if(name.length() < 5){
                    etName.setError("Introduza uma rua válida (mínimo 2 letras)");
                    return false;
                }

                Attribute location_district = (Attribute)spLocationDistrict.getSelectedItem();
                if(location_district.getId() == -1){
                    return false;
                }

                newAnimalPost.setFoundAnimal_street(locationStreet);
                newAnimalPost.setFoundAnimal_city(location_city);
                newAnimalPost.setFoundAnimal_district_id(location_district.getId());
            }
        } catch(Exception e){
            return false;
        }

        return true;
    }

}

