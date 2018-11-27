package com.example.felipe.foodgram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Galeria extends AppCompatActivity {

    private Button btn_subir;
    private ImageView img_subir;
    private StorageReference storage;
    private static final int GALERIA=1;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        btn_subir=findViewById(R.id.btn_subir);
        img_subir=findViewById(R.id.img_receta);
        storage= FirebaseStorage.getInstance().getReference();
        dialog=new ProgressDialog(this);

        btn_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALERIA);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALERIA && requestCode==RESULT_OK){
            dialog.setTitle("Subiendo foto");
            dialog.setMessage("Subiendo a firebase");
            dialog.setCancelable(false);
            dialog.show();
            Uri uri= data.getData();
            StorageReference filepath=storage.child("fotos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Uri descargaFoto=taskSnapshot.getUploadSessionUri();
                    Glide.with(Galeria.this).load(descargaFoto).into(img_subir);
                    Toast.makeText(Galeria.this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
