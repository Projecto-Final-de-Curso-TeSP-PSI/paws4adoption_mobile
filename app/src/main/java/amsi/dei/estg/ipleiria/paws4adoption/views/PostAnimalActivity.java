package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;

import amsi.dei.estg.ipleiria.paws4adoption.MainActivity;
import amsi.dei.estg.ipleiria.paws4adoption.listeners.UploadPhotoListener;
import amsi.dei.estg.ipleiria.paws4adoption.models.SingletonPawsManager;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.services.FetchAddressIntentService;
import amsi.dei.estg.ipleiria.paws4adoption.R;

public class PostAnimalActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int LOCATION_REQUEST = 3;

    private ImageView ivPhoto;
    private Button btnUpload;

    private EditText etNome;
    private EditText etIdChip;
    private EditText etDescription;

    private TextView tvLat;
    private TextView tvLong;
    private TextView tvAddress;

    private ResultReceiver resultReceiver;

    double latitude;
    double longitude;

    private String currentPhotoPath;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_animal);

        ivPhoto = findViewById(R.id.ivPhoto);

        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostAnimalActivity.this);

                builder.setTitle("Selecione uma opção para carregar a imagem");

                String[] options = {"Galeria", "Camera"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                if (Build.VERSION.SDK_INT >= 23 &&
                                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                                    ActivityCompat.requestPermissions(PostAnimalActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST);
                                    return;
                                }

                                uploadPhotoGallery();
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= 23 &&
                                    (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)){

                                    ActivityCompat.requestPermissions(PostAnimalActivity.this,
                                            new String[]{
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.CAMERA
                                            },
                                            CAMERA_REQUEST);
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


        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        tvAddress = findViewById(R.id.tvAddress);


        resultReceiver = new AddressResultReceiver(new Handler());

//        //PERMISSAO LEITURA DE DADOS DA GALERIA
//        int readExternalPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE;
//        if (Build.VERSION.SDK_INT >= 23 && readExternalPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(PostAnimalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST);
//        }
//
//        //PERMISSAO ESCRITA DE DADOS NA GALERIA
//        int writeExternalPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE;
//        if (Build.VERSION.SDK_INT >= 23 && writeExternalPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST);
//        }
//
//        //PERMISSAO CAMERA
//        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
//        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
//        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onTakePhoto_OnClick(View view) {

        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if (cameraPermission == PackageManager.PERMISSION_DENIED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "Não tem permissão para utilizar a camara, altera nas definições da aplicação", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

//            case LOCATION_REQUEST:
//                if (grantResults.length > 0) {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        getLocation();
//                    }
//                }
//                break;
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
            startActivityForResult(intent, CAMERA_REQUEST);
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

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void getLocation() {
//        LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Toast.makeText(this, "Não tem permissão para utilizar a localização, altere nas definições da aplicação", Toast.LENGTH_SHORT).show();
//            } else{
//                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
//            return;
//        }
//
//
//        LocationServices.getFusedLocationProviderClient(PostAnimalActivity.this)
//                .requestLocationUpdates(locationRequest, new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        super.onLocationResult(locationResult);
//
//                        LocationServices.getFusedLocationProviderClient(PostAnimalActivity.this)
//                                .removeLocationUpdates(this);
//
//                        if (locationResult != null && locationResult.getLocations().size() > 0) {
//                            int latestLocationIndex = locationResult.getLocations().size() - 1;
//                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
//                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
//                            tvLat.setText("" + latitude);
//                            tvLong.setText("" + longitude);
//
//                            Location location = new Location("providerNA");
//                            location.setLatitude(latitude);
//                            location.setLongitude(longitude);
//                            fetchAddressFromLatLong(location);
//
//                        } else {
//                            //
//                        }
//                    }
//                }, Looper.getMainLooper());
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch(requestCode){
//            case GALLERY_REQUEST:
//                if (resultCode == RESULT_OK) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//                    ivPhoto.setImageBitmap(bitmap);
//
//                    MediaScannerConnection.scanFile(this, paths, null,
//                        new MediaScannerConnection.MediaScannerConnectionClient() {
//                            @Override
//                            public void onMediaScannerConnected() {
//                                Log.d("Detalhes", "onScanCompleted");
//                            }
//
//                            @Override
//                            public void onScanCompleted(String path, Uri uri) {
//                                Log.d("Detalhes", "onScanCompleted");
//                            }
//                        });
//                }
//                break;
//
////            case CAMERA_REQUEST:
////
////                if (resultCode == RESULT_OK) {
////                    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
////                    ivPhoto.setImageBitmap(bitmap);
////
////                    MediaScannerConnection.scanFile(this, paths, null,
////                        new MediaScannerConnection.MediaScannerConnectionClient() {
////                            @Override
////                            public void onMediaScannerConnected() {
////                                Log.d("Detalhes", "onScanCompleted");
////                            }
////
////                            @Override
////                            public void onScanCompleted(String path, Uri uri) {
////                                Log.d("Detalhes", "onScanCompleted");
////                            }
////                        });
////
////                }
////                break;
//
//        }
//    }

//
//    public void onGetLocation_OnClick(View view) {
//
//
//
//        int currentPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
//        if (currentPermission == PackageManager.PERMISSION_DENIED) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        LOCATION_REQUEST);
//            } else {
//                Toast.makeText(this, "Não tem permissão para utilizar a localização, altere nas definições da aplicação", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//
//
//        }
//    }

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
        File storageDir = new
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getParentFile(),"Animals");
        storageDir.mkdirs();
        File image = File.createTempFile(imgNomeFich, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}

