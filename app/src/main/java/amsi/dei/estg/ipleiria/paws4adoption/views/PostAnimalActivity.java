package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import amsi.dei.estg.ipleiria.paws4adoption.listeners.AttributeListener;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.RequestListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.Animal;
import amsi.dei.estg.ipleiria.paws4adoption.models.Attribute;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.NetworkStateReceiver;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.services.FetchAddressIntentService;
import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Wrench;

public class PostAnimalActivity extends AppCompatActivity
        implements AttributeListener, RequestListener, NetworkStateReceiver.NetworkStateReceiverListener {

    private NetworkStateReceiver networkStateReceiver;
    //################ PERMISSIONS REQUEST TYPES ################
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int LOCATION_REQUEST = 3;

    private static final int MALE = 'M';
    private static final int FEMALE = 'F';

    //################ EXTRA ################
    public static final String ACTION = "action";
    private String action = null;
    public static final String ANIMAL_TYPE = "animal_type";
    private String animal_type = null;
    public static final String ANIMAL_ID = "animal";
    private int animal_id = -1;
    private Animal editAnimal;

    //################ GRAPHICAL ELEMENTS ################
    private LinearLayout llFoundAnimal;

    private ImageView ivPhoto;
    private Button btnUploadPhoto;
    private Button btnLocation;
    private FloatingActionButton fab;

    private NumberPicker npId;
    private EditText etName;
    private EditText etChipId;
    private EditText etDescription;

    ArrayAdapter<Attribute> dataAdapterNature = null;
    private Spinner spNature;
    ArrayAdapter<Attribute> dataAdapterBreed = null;
    private Spinner spBreed;
    ArrayAdapter<Attribute> dataAdapterFurLength = null;
    private Spinner spFurLength;
    ArrayAdapter<Attribute> dataAdapterFurColor = null;
    private Spinner spFurColor;
    ArrayAdapter<Attribute> dataAdapterSize = null;
    private Spinner spSize;
    ArrayAdapter<Attribute> dataAdapterSex = null;
    private Spinner spSex;

    private TextView tvMissingFoundDate;

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

    boolean onTouch = false;

    private String currentPhotoPath;
    private Bitmap photo;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    /**
     * On Create method of the Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_animal);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        animal_type = getIntent().getStringExtra(ANIMAL_TYPE);
        action = getIntent().getStringExtra(ACTION);
        animal_id = getIntent().getIntExtra(ANIMAL_ID, 0);

        importGraphicalElements();

        if(action.equals(RockChisel.ACTION_CREATE)){
            implementListeners();
        }

        initAttributesSpinners();

        SingletonPawsManager.getInstance(getApplicationContext()).setAttributeListener(this);

        SingletonPawsManager.getInstance(getApplicationContext()).setRequestListener(this);

        setScenario();

        resultReceiver = new AddressResultReceiver(new Handler());
    }

    /**
     * Method where we set all the graphical components variables
     */
    private void importGraphicalElements() {

        llFoundAnimal = findViewById(R.id.llFoundAnimal);

        ivPhoto = findViewById(R.id.ivPhoto);

        npId = findViewById(R.id.npId);

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
        initSpinner(dataAdapterSex, spSex, getString(R.string.select_sex), sexArray);


        tvMissingFoundDate = findViewById(R.id.tvMissingFoundDate);
        dateButton = findViewById(R.id.btnMissingFoundDate);
        dateButton.setText(Wrench.getTodaysDate());

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


        spNature.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouch = true;
                v.performClick();
                return false;
            }
        });


        spNature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!onTouch)
                    return;
                onTouch = false;

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


                switch(action){
                    case RockChisel.ACTION_CREATE:
                        switch(animal_type){
                            case RockChisel.SCENARIO_MISSING_ANIMAL:
                                SingletonPawsManager.getInstance(getApplicationContext()).insertAnimalAPI(newAnimalPost, RockChisel.MISSING_ANIMALS_API_SERVICE, Vault.getAuthToken(getApplicationContext()),  getApplicationContext());
                                break;
                            case RockChisel.SCENARIO_FOUND_ANIMAL:
                                SingletonPawsManager.getInstance(getApplicationContext()).insertAnimalAPI(newAnimalPost, RockChisel.FOUND_ANIMALS_API_SERVICE, Vault.getAuthToken(getApplicationContext()), getApplicationContext());
                                break;
                        }
                        break;
                    case RockChisel.ACTION_UPDATE:
                        switch(animal_type){
                            case RockChisel.SCENARIO_MISSING_ANIMAL:
                                SingletonPawsManager.getInstance(getApplicationContext()).updateAnimalAPI(newAnimalPost, RockChisel.MISSING_ANIMALS_API_SERVICE, Vault.getAuthToken(getApplicationContext()),  getApplicationContext());
                                break;
                            case RockChisel.SCENARIO_FOUND_ANIMAL:
                                SingletonPawsManager.getInstance(getApplicationContext()).updateAnimalAPI(newAnimalPost, RockChisel.FOUND_ANIMALS_API_SERVICE, Vault.getAuthToken(getApplicationContext()), getApplicationContext());
                                break;
                        }
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

            if(animal_type.equals(RockChisel.SCENARIO_MISSING_ANIMAL)) {

                if (name.length() < 2) {
                    etName.setError(getString(R.string.validate_msg_animal_name));
                    return null;
                }

                chipId = etChipId.getText().toString();
                if (chipId.length() > 15) {
                    etChipId.setError(getString(R.string.validate_msg_animal_chip_id));
                    return null;
                }
            }

            String description = etDescription.getText().toString();
            if(description.length() < 10){
                etDescription.setError(getString(R.string.validate_msg_animal_description));
                return null;
            }

            Attribute specie = (Attribute)spNature.getSelectedItem();
            if(specie.getId() == -1){
                Toast.makeText(this, getString(R.string.validate_msg_enter_specie), Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute subSpecie = (Attribute)spBreed.getSelectedItem();
            if(subSpecie.getId() == -1){
                Toast.makeText(this, getString(R.string.validate_msg_enter_subspecie), Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute furColor = (Attribute)spFurColor.getSelectedItem();
            if(furColor.getId() == -1){
                Toast.makeText(this, getString(R.string.msg_enter_fur_color), Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute furLength = (Attribute)spFurLength.getSelectedItem();
            if(furLength.getId() == -1){
                Toast.makeText(this, getString(R.string.msg_enter_fur_length), Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute size = (Attribute)spSize.getSelectedItem();
            if(size.getId() == -1){
                Toast.makeText(this, getString(R.string.validade_animal_enter_size), Toast.LENGTH_SHORT).show();
                return null;
            }

            Attribute sex = (Attribute)spSex.getSelectedItem();
            if(sex.getId() == -1){
                Toast.makeText(this, getString(R.string.validate_msg_enter_sex), Toast.LENGTH_SHORT).show();
                return null;
            }


            String missingFoundDate = Wrench.makeDateString_yyyyMMdd(dateButton.getText().toString());


            if(action.equals(RockChisel.ACTION_UPDATE)){
                newAnimalPost.setId(animal_id);
            }


            if(action.equals(RockChisel.ACTION_CREATE)){
                if(this.photo == null){
                    Toast.makeText(this, getString(R.string.msg_enter_photo), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

            String newPhoto = null;
            if(this.photo != null){
                newPhoto = Wrench.bmpToBase64(this.photo);
                newAnimalPost.setPhoto(newPhoto);
            }

            newAnimalPost.setName(name);
            newAnimalPost.setChipId(chipId);
            newAnimalPost.setDescription(description);
            newAnimalPost.setNature_id(subSpecie.getId());
            newAnimalPost.setFur_length_id(furLength.getId());
            newAnimalPost.setFur_color_id(furColor.getId());
            newAnimalPost.setSize_id(size.getId());
            newAnimalPost.setSex(sex.getName());
            newAnimalPost.setMissingFound_date(missingFoundDate);


            if(animal_type.equals(RockChisel.SCENARIO_FOUND_ANIMAL)){
                String locationStreet = etLocationStreet.getText().toString();
                if(locationStreet.length() < 2){
                    etName.setError(getString(R.string.validate_msg_invalid_street));
                    return null;
                }

                String location_city = etLocationCity.getText().toString();
                if(location_city.length() < 2){
                    etName.setError(getString(R.string.validate_msg_enter_city));
                    return null;
                }

                Attribute location_district = (Attribute)spLocationDistrict.getSelectedItem();
                if(location_district.getId() == -1){
                    Toast.makeText(this, getString(R.string.select_district), Toast.LENGTH_SHORT).show();
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
    }

    /**
     * Method that send's request forr all the spinners data from the API
     */
    public void initAttributesSpinners(){
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SPECIE, RockChisel.ATTR_SPECIE_SYMLINK, null);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_LENGTH, RockChisel.ATTR_FUR_LENGTH_SYMLINK, null);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_FUR_COLOR, RockChisel.ATTR_FUR_COLOR_SYMLINK, null);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SIZE, RockChisel.ATTR_SIZE_SYMLINK, null);
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_DISTRICT, RockChisel.ATTR_DISTRICT_SYMLINK, null);
    }

    /**
     * Fills the animal fields on the page
     * @param animal
     */
    public void fillAnimal(Animal animal) {

        try{
            etName.setText(animal.getName());
            etChipId.setText(String.format("%s", animal.getChipId()));
            etDescription.setText(animal.getDescription());

            int natureSelectedItem = getSpinnerAttributteID(spNature, animal.getNature_parent_id());
            spNature.setSelection(natureSelectedItem);

            int breedSelectedItem = getSpinnerAttributteID(spBreed, animal.getNature_id());
            spBreed.setSelection(breedSelectedItem);

            int furcolorSelectedItem = getSpinnerAttributteID(spFurColor, animal.getFur_color_id());
            spFurColor.setSelection(furcolorSelectedItem);

            int furlengthSelectedItem = getSpinnerAttributteID(spFurLength, animal.getFur_length_id());
            spFurLength.setSelection(furlengthSelectedItem);

            int sizeSelectedItem = getSpinnerAttributteID(spSize, animal.getSize_id());
            spSize.setSelection(sizeSelectedItem);

            if(animal.getSex().equals(MALE)){
                spSex.setSelection(0);
            } else {
                spSex.setSelection(1);
            }

            Glide.with(getApplicationContext())
                    .load(animal.getPhoto())
                    .placeholder(R.drawable.paws4adoption_logo)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivPhoto);

            String dateString = animal.getMissingFound_date();
            dateButton.setText(Wrench.makeDateString_ddMMyyyy(dateString));

            initDatePicker(
                    Wrench.getDatePart_yyyyMMdd(dateString, Wrench.DAY),
                    Wrench.getDatePart_yyyyMMdd(dateString, Wrench.MONTH),
                    Wrench.getDatePart_yyyyMMdd(dateString, Wrench.YEAR)
            );

            if(animal_type.equals(RockChisel.SCENARIO_FOUND_ANIMAL)){
                etLocationStreet.setText(animal.getFoundAnimal_street());
                etLocationCity.setText(animal.getFoundAnimal_city());

                int locationDistrictSelectedItem = getSpinnerAttributteID(spLocationDistrict, animal.getFoundAnimal_district_id());
                spLocationDistrict.setSelection(locationDistrictSelectedItem);
            }

        } catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            Toast.makeText(this, getString(R.string.error_filling_animal), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * Get's the spinner index whre the attribute is
     * @param spNature
     * @param item_id
     * @return
     */
    private int getSpinnerAttributteID(Spinner spNature, int item_id) {
        for (int i = 0; i < spNature.getCount(); i++) {
            Attribute auxAttribute = (Attribute)spNature.getItemAtPosition(i);
            if(auxAttribute.getId().equals(item_id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Method that implements changes to the current activity according the Scenario an Action intended
     */
    private void setScenario() {

        switch (action){

            case RockChisel.ACTION_CREATE:
                Calendar calendar =  Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                initDatePicker(day, month + 1, year);

                switch(animal_type){

                    case RockChisel.SCENARIO_MISSING_ANIMAL:
                        setTitle(getString(R.string.publish_missing_animal));
                        //datePickerDialog.setTitle(R.string.hint_date_missing_animal);
                        break;

                    case RockChisel.FOUND_ANIMAL:
                        setTitle(getString(R.string.publish_found_animal));
                        //datePickerDialog.setTitle(R.string.hint_date_found_animal);
                        break;
                }
                break;

            case RockChisel.ACTION_UPDATE:
                switch(animal_type){
                    case RockChisel.SCENARIO_MISSING_ANIMAL:
                        setTitle(getString(R.string.edit_missing_animal));
                        break;
                    case RockChisel.FOUND_ANIMAL:
                        setTitle(getString(R.string.edit_found_animal));
                        break;
                }
                break;

        }

        switch(animal_type){

            case RockChisel.SCENARIO_MISSING_ANIMAL:
                tvMissingFoundDate.setText(R.string.hint_date_missing_animal);
                llFoundAnimal.setVisibility(View.GONE);
                break;

            case RockChisel.SCENARIO_FOUND_ANIMAL:
                tvMissingFoundDate.setText(R.string.hint_date_found_animal);
                llFoundAnimal.setVisibility(View.VISIBLE);
                break;

        }

        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SPECIE, RockChisel.ATTR_SPECIE_SYMLINK, null);

    }

    //################ DATE ################

    /**
     * Method that intiate the date picker with the passed date by parameters
     * @param day
     * @param month
     * @param year
     */
    private void initDatePicker(int day, int month, int year) {

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = Wrench.makeDateString_ddMMyyyy(day, month, year);
                dateButton.setText(date);
            }
        };

        month = month - 1;
        datePickerDialog =  new DatePickerDialog(this, R.style.MyDatePickerDialogTheme, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setSpinnersShown(true);
    }

    /**
     * OnClick event for the Date Picker
     * @param view
     */
    public void openDatePicker(View view) {

        String date_yyyyMMdd = Wrench.makeDateString_yyyyMMdd(dateButton.getText().toString());

        datePickerDialog.getDatePicker().updateDate(
                Wrench.getDatePart_yyyyMMdd(date_yyyyMMdd, Wrench.YEAR),
                Wrench.getDatePart_yyyyMMdd(date_yyyyMMdd, Wrench.MONTH) - 1,
                Wrench.getDatePart_yyyyMMdd(date_yyyyMMdd, Wrench.DAY)
        );

        datePickerDialog.show();
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
     * Code section where are implemented methods related to getting the actual location,
     * firstly we get the coordinates using Geocoder, and afterwards, we get
     * an array with the respective address using an intent service by gms
     *
     * Base code obtain from: https://www.youtube.com/watch?v=ari3iD-3q8c
     */

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
         * Get's the address from the coordinates given
         * @param location The location coordinates
         */
        public void fetchAddressFromLatLong(Location location){
            Intent intent = new Intent(this, FetchAddressIntentService.class);
            intent.putExtra(RockChisel.RECEIVER, resultReceiver);
            intent.putExtra(RockChisel.LOCATION_DATA_EXTRA, location);
            startService(intent);
        }

    @Override
    public void networkAvailable() {

    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getApplicationContext(), "Sem Internet", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
         * On receiving the result of the address, set's the
         */
        private class AddressResultReceiver extends ResultReceiver {

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

    /**
     * End of code section related to getting actual location
     */


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
                        assert data != null;
                        if(data.getData() != null) {
                            final Uri imageUri = data.getData();
                            imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                            photo = bitmap;
                            ivPhoto.setImageBitmap(bitmap);
                        } else{
                            Toast.makeText(this, "Erro ao importar fot da galeria", Toast.LENGTH_SHORT).show();
                        }
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
                    photo = bitmap;
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


    //################ ATTRIBUTES REQUEST FOR THE COMBOBOX's ################

    /**
     * When getting an attribute type, asks for the next one, until filling all the combobox
     * @param attributes The attributes array
     * @param attributeType The type of attribute
     */
    @Override
    public void onReceivedAttributes(ArrayList<Attribute> attributes, String attributeType) {

        switch(attributeType){

            case RockChisel.ATTR_SPECIE:
                initSpinner(dataAdapterNature, spNature, getString(R.string.prompt_select_specie), attributes);
                afterAttributte();
                break;

            case RockChisel.ATTR_SUBSPECIE:

                boolean initAnimal = spBreed.getCount() == 0 && action.equals(RockChisel.ACTION_UPDATE);
                initSpinner(dataAdapterBreed, spBreed, getString(R.string.prompt_select_subspecie), attributes);
                if(initAnimal) {
                    fillAnimal(editAnimal);
                    implementListeners();
                }
                break;

            case RockChisel.ATTR_FUR_LENGTH:
                initSpinner(dataAdapterFurLength, spFurLength, getString(R.string.prompt_select_furlength), attributes);
                afterAttributte();
                break;

            case RockChisel.ATTR_FUR_COLOR:
                initSpinner(dataAdapterFurColor, spFurColor, getString(R.string.prompt_select_fur_color), attributes);
                afterAttributte();
                break;

            case RockChisel.ATTR_SIZE:
                initSpinner(dataAdapterSize, spSize, getString(R.string.prompt_select_size), attributes);
                afterAttributte();
                break;

            case RockChisel.ATTR_DISTRICT:
                initSpinner(dataAdapterLocationDistrict, spLocationDistrict, getString(R.string.prompt_select_district), attributes);
                afterAttributte();
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

    /**
     * Method that triggers the request for the animal data, only after all spinners are filled
     */
    private void afterAttributte(){
        boolean natureCount = spNature.getCount() > 0;
        boolean furcolorCount = spFurColor.getCount() > 0;
        boolean furlengthCount = spFurLength.getCount() > 0;
        boolean sizeCount = spSize.getCount() > 0;
        boolean locationDistrictCount = spLocationDistrict.getCount() > 0;

        if(action.equals(RockChisel.ACTION_UPDATE) &&
            natureCount &&
            furcolorCount &&
            furlengthCount &&
            sizeCount &&
            locationDistrictCount)
        {
            SingletonPawsManager.getInstance(getApplicationContext()).getAnimalAPI(getApplicationContext(), animal_id);
        }
    }


    //################ REQUEST LISTENER ################
    @Override
    public void onRequestSuccess(String action, Animal animal) {

    }

    @Override
    public void onRequestError(String message) {

    }

    @Override
    public void onReadAnimal(Animal animal) {
        this.editAnimal = animal;
        SingletonPawsManager.getInstance(getApplicationContext()).getAttributesAPI(getApplicationContext(), RockChisel.ATTR_SUBSPECIE, RockChisel.ATTR_SUBSPECIE_SYMLINK, animal.getNature_parent_id());
    }

    @Override
    public void onCreateAnimal() {
        finish();
        Toast.makeText(this, "Animal criado com sucesso", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateAnimal() {
        finish();
    }

    @Override
    public void onDeleteAnimal() {
        finish();
    }
}

