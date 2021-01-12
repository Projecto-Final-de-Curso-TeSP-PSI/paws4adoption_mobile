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
import android.icu.number.NumberRangeFormatter;
import android.location.Location;
import android.media.MediaRouter2;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xml.sax.helpers.AttributesImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.AnimalListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.AttributeListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.RequestListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.Attribute;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.services.FetchAddressIntentService;
import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

public class PostAnimalActivity extends AppCompatActivity implements AttributeListener, AnimalListener {

    //################ PERMISSIONS REQUEST TYPES ################
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int LOCATION_REQUEST = 3;

    //################ EXTRA ################
    public static final String SCENARIO = "scenario";
    private String scenario = null;
    public static final String ACTION = "action";
    private String action = null;
    public static final String ANIMAL_ID = "animal";
    private int animal_id = -1;
    private Animal editAnimal;

    //################ GRAPHICAL ELEMENTS ################
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

    /**
     * On Create method of the Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_animal);

        scenario = getIntent().getStringExtra(SCENARIO);
        action = getIntent().getStringExtra(ACTION);
        animal_id = getIntent().getIntExtra(ANIMAL_ID, 0);

        importGraphicalElements();

        implementListeners();

        initAttributesSpinners();

        SingletonPawsManager.getInstance(getApplicationContext()).setAttributeListener(this);
        SingletonPawsManager.getInstance(getApplicationContext()).setAnimalListener(this);

        //SingletonPawsManager.getInstance(getApplicationContext()).setRequestListener(this);

        setScenario();

        resultReceiver = new AddressResultReceiver(new Handler());


    }

    /**
     * Method where we set all the graphical components variables
     */
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

    /**
     * Method where we implement all the buttons listeners
     */
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

                Animal newAnimalPost = validateAnimal();
                if( newAnimalPost == null){
                    Toast.makeText(PostAnimalActivity.this, "Erro ao validar o animal", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch(scenario){
                    case RockChisel.SCENARIO_MISSING_ANIMAL:
                        SingletonPawsManager.getInstance(getApplicationContext())
                                .insertAnimalAPI(newAnimalPost, RockChisel.MISSING_ANIMALS_API_SERVICE, Vault.getAuthToken(getApplicationContext()),  getApplicationContext());
                        break;
                    case RockChisel.SCENARIO_FOUND_ANIMAL:
                        SingletonPawsManager.getInstance(getApplicationContext())
                                .insertAnimalAPI(newAnimalPost, RockChisel.FOUND_ANIMALS_API_SERVICE, Vault.getAuthToken(getApplicationContext()), getApplicationContext());
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
     * Method that validades the animal input fields
     * @return the animal object
     */
    public Animal validateAnimal() {

        Animal newAnimalPost = new Animal();
        try{

            String name = etName.getText().toString();
            String chipId = etChipId.getText().toString();

            if(scenario.equals(RockChisel.SCENARIO_MISSING_ANIMAL)) {

                if (name.length() < 5) {
                    etName.setError("Introduza um nome válido (mínimo 2 letras)");
                    return null;
                }

                chipId = etChipId.getText().toString();
                if (chipId.length() > 15) {
                    etChipId.setError("Introduza um chip id válido (15 dígitos))");
                    return null;
                }
            }

            String description = etDescription.getText().toString();
            if(description.length() < 10){
                etDescription.setError("Introduza uma descrição válida (min. 10 caracteres))");
                return null;
            }

            Attribute specie = (Attribute)spNature.getSelectedItem();
            if(specie.getId() == -1){
                Toast.makeText(this, "Selecione uma espécie", Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute subSpecie = (Attribute)spBreed.getSelectedItem();
            if(subSpecie.getId() == -1){
                Toast.makeText(this, "Selecione uma espécie", Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute furColor = (Attribute)spFurColor.getSelectedItem();
            if(furColor.getId() == -1){
                return null;
            }

            Attribute furLength = (Attribute)spFurLength.getSelectedItem();
            if(furLength.getId() == -1){
                return null;
            }

            Attribute size = (Attribute)spSize.getSelectedItem();
            if(size.getId() == -1){
                return null;
            }

            Attribute sex = (Attribute)spSex.getSelectedItem();
            if(sex.getId() == -1){
                return null;
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
                return null;
            }

            if(missingFoundDate.after(Calendar.getInstance().getTime())){
                Toast.makeText(this, "A data não pode ser superior à atual", Toast.LENGTH_SHORT).show();
                return null;
            }

            newAnimalPost.setName(name);
            newAnimalPost.setChipId(chipId);
            newAnimalPost.setDescription(description);
            newAnimalPost.setNature_id(subSpecie.getId());
            newAnimalPost.setFur_length_id(furLength.getId());
            newAnimalPost.setFur_color_id(furColor.getId());
            newAnimalPost.setSize_id(size.getId());
            newAnimalPost.setSex(sex.getName());
            newAnimalPost.setMissingFound_date(missingFoundDate.toString());
            //animal.setPhoto();

            if(scenario.equals(RockChisel.SCENARIO_FOUND_ANIMAL)){
                String locationStreet = etLocationStreet.getText().toString();
                if(locationStreet.length() < 5){
                    etName.setError("Introduza uma rua válida (mínimo 2 letras)");
                    return null;
                }

                String location_city = etLocationCity.getText().toString();
                if(location_city.length() < 5){
                    etName.setError("Introduza uma rua válida (mínimo 2 letras)");
                    return null;
                }

                Attribute location_district = (Attribute)spLocationDistrict.getSelectedItem();
                if(location_district.getId() == -1){
                    return null;
                }

                newAnimalPost.setFoundAnimal_street(locationStreet);
                newAnimalPost.setFoundAnimal_city(location_city);
                newAnimalPost.setFoundAnimal_district_id(location_district.getId());
            }

        } catch(Exception e){
            return null;
        }

        return newAnimalPost;
    }    /**
     * Method that validades the animal input fields
     * @return the animal object
     */

    public void initAttributesSpinners(){
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_LENGTH, RockChisel.ATTR_FUR_LENGTH_SYMLINK, null);

        //SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SUBSPECIE, RockChisel.ATTR_SUBSPECIE_SYMLINK, null);

        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_DISTRICT, RockChisel.ATTR_DISTRICT_SYMLINK, null);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_COLOR, RockChisel.ATTR_FUR_COLOR_SYMLINK, null);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_COLOR, RockChisel.ATTR_FUR_COLOR_SYMLINK, null);
    }

    public void fillAnimal(Animal animal) {

        try{
            etName.setText(animal.getName());
            etChipId.setText(String.format("%s", animal.getChipId()));
            etDescription.setText(animal.getDescription());
            spNature.setSelection(getSpinnerPosition(animal.getNature_parent_id(), spNature));
            spBreed.setSelection(getSpinnerPosition(animal.getNature_id(), spBreed));
            spFurColor.setSelection(getSpinnerPosition(animal.getFur_color_id(), spFurColor));
            spFurLength.setSelection(getSpinnerPosition(animal.getFur_length_id(), spFurLength));
            spSize.setSelection(getSpinnerPosition(animal.getSize_id(), spSize));
            spSex.setSelection(getSpinnerPosition(animal.getSex().equals("M") ? 0 : 1, spSex));
            //animal.setPhoto();

            etMissingFoundDate.setText(animal.getMissingFound_date());

            if(scenario.equals(RockChisel.SCENARIO_FOUND_ANIMAL)){
                etLocationStreet.setText(animal.getFoundAnimal_street());
                etLocationCity.setText(animal.getFoundAnimal_city());
                spLocationDistrict.setSelection(getSpinnerPosition(animal.getNature_id(), spLocationDistrict));
            }

        } catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            Toast.makeText(this, "Erro ao preencher animal", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * Get's teh position of one Attribute on the spinner
     * @param item_id the item id to search for
     * @param spinner the spinner where to search
     * @return teh i
     */
    public static int getSpinnerPosition(int item_id, Spinner spinner) {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            Attribute auxAttribute = (Attribute)adapter.getItem(i);
            if(auxAttribute.getId() == item_id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method that implements changes to the current activity according the Scenario an Action intended
     */
    private void setScenario() {

        if(action.equals(RockChisel.ACTION_CREATE)){
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

        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SPECIE, RockChisel.ATTR_SPECIE_SYMLINK, null);

    }

    //################ PHOTO ################

    /**
     * Launches the activity for the gallery pick
     */
    private void uploadPhotoGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    /**
     * Lauches the activity for the camera photo shoot
     */
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

    /**
     * After the request of the permissions, if granted, redirects to the action
     * @param requestCode The code of the request type
     * @param permissions The permissions
     * @param grantResults The granted results or the permissions requests
     */
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

    /**
     * Creates one new temporary photo
     * @return
     * @throws IOException
     */
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


    //################ LOCATION ################

    /**
     * Launches the location service, gets the coordinates and fecths for the address
     */
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

    /**
     * Get's teh addres from the coordinates given
     * @param location The location coordinates
     */
    public void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(RockChisel.RECEIVER, resultReceiver);
        intent.putExtra(RockChisel.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    /**
     * On the result of the activity, get's the photos colected
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    InputStream imageStream = null;
                    try {
                        final Uri imageUri = data.getData();
                        imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        ivPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erro ao importar a foto da galeria", Toast.LENGTH_SHORT).show();
                        //TODO: lançar erro
                    }
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
        }
    }

    /**
     * On receiving the result of the address, set's the
     */
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


    //################ ATTRIBUTES REQUEST FOR THE COMBOBOX's ################

    /**
     * When getting an attribute type, asks fro teh next one, until filling all the combobox
     * @param attributes The attributes array
     * @param attributeType The type of attribute
     */
    @Override
    public void onReceivedAttributes(ArrayList<Attribute> attributes, String attributeType) {

        switch(attributeType){
            case RockChisel.ATTR_SPECIE:
                initSpinner(dataAdapterNature, spNature, "Selecione a espécie", attributes);
//                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_LENGTH, RockChisel.ATTR_FUR_LENGTH_SYMLINK, null);
                break;

            case RockChisel.ATTR_SUBSPECIE:
                initSpinner(dataAdapterBreed, spBreed, "Selecione a sub espécie", attributes);

//                if(action.equals(RockChisel.ACTION_UPDATE)){
//                    SingletonPawsManager.getInstance(getApplicationContext()).getAnimalAPI(getApplicationContext(), animal_id);
//                }
                break;

            case RockChisel.ATTR_FUR_LENGTH:
                initSpinner(dataAdapterFurLength, spFurLength, "Selecione o tipo de pelagem", attributes);
//                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_COLOR, RockChisel.ATTR_FUR_COLOR_SYMLINK, null);
                break;

            case RockChisel.ATTR_FUR_COLOR:
                initSpinner(dataAdapterFurColor, spFurColor, "Selecione a cor da pelagem", attributes);
//                SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SIZE, RockChisel.ATTR_SIZE_SYMLINK, null);
                break;

            case RockChisel.ATTR_SIZE:
                initSpinner(dataAdapterSize, spSize, "Selecione o porte", attributes);

//                if(scenario.equals(RockChisel.SCENARIO_FOUND_ANIMAL)){
//                    SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_DISTRICT, RockChisel.ATTR_DISTRICT_SYMLINK, null);
//                } else{
//                    if(action.equals(RockChisel.ACTION_UPDATE))
//                        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SUBSPECIE, RockChisel.ATTR_SUBSPECIE_SYMLINK, editAnimal.getNature_parent_id());
//                }

                break;

            case RockChisel.ATTR_DISTRICT:
                initSpinner(dataAdapterLocationDistrict, spLocationDistrict, "Selecione o distrito", attributes);

//                if(action.equals(RockChisel.ACTION_UPDATE)){
//                    SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SUBSPECIE, RockChisel.ATTR_SUBSPECIE_SYMLINK, editAnimal.getNature_parent_id());
//                }
                break;
        }

    }

    /**
     * Initializaes all the spinners for the attributes
     * @param spinnerArrayAdapter
     * @param spinner
     * @param promptMessage
     * @param listAttributes
     */
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



    @Override
    public void onRefreshAnimalsList(ArrayList<Animal> animalsList) {

    }

    @Override
    public void onUpdateAnimalsList(Animal animal, int operation) {

    }

    @Override
    public void onGetAnimalAPI(Animal animal) {
        this.editAnimal = animal;
        fillAnimal(editAnimal);
    }
}

