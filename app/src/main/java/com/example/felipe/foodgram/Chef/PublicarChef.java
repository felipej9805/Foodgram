package com.example.felipe.foodgram.Chef;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.felipe.foodgram.Chef.InicioChef;
import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.util.UtilDomi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PublicarChef extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 101;


    private Button btn_subir;
    private ProgressDialog dialog;
    FirebaseAuth auth;

    FirebaseStorage storage;


    ImageView img_atras;
    ImageView img_receta;

    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_chef);

        auth = FirebaseAuth.getInstance();
        btn_subir = findViewById(R.id.btn_subir);
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);

        img_atras = findViewById(R.id.img_atras);
        img_receta = findViewById(R.id.img_receta);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 11);


        img_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        btn_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                //i.setType("video/*");
                //i.setType("*/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, REQUEST_GALLERY);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {


            dialog.setTitle("Subiendo...");
            dialog.setMessage("Subiendo foto a tu perfil");
            dialog.setCancelable(false);
            dialog.show();

            path = UtilDomi.getPath(PublicarChef.this, data.getData());
            Bitmap m = BitmapFactory.decodeFile(path);
            img_receta.setImageBitmap(m);


            final StorageReference reference = storage.getReference().child("recetas").child(auth.getCurrentUser().getUid()).child(data.getData().getLastPathSegment());

            reference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(PublicarChef.this).load(uri).into(img_receta);

                        }
                    });

                    Toast.makeText(PublicarChef.this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
