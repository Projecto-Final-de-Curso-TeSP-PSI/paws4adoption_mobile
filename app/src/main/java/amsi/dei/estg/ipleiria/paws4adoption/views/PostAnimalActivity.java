package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import amsi.dei.estg.ipleiria.paws4adoption.R;

public class PostAnimalActivity extends AppCompatActivity{

     private static final int CAM_PERMISSION_REQUEST_CODE = 1;
     private Button button;
     private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_animal);

        ivPhoto = findViewById(R.id.ivPhoto);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onTakePhoto_OnClick(View view) {

        int currentPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        if(currentPermission == PackageManager.PERMISSION_DENIED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this, "Permissão recusada", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAM_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAM_PERMISSION_REQUEST_CODE){
            int a = 0;
            Toast.makeText(this, "Ola", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == CAM_PERMISSION_REQUEST_CODE){
            if (resultCode == RESULT_OK && data.hasExtra("data")) {

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                ivPhoto.setImageBitmap(bitmap);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}