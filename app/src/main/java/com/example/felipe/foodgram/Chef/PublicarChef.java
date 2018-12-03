package com.example.felipe.foodgram.Chef;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.felipe.foodgram.Chef.InicioChef;
import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.util.UtilDomi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PublicarChef extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 101;

    public static final String CHANNEL_ID = "aplicaciones20182";
    public static final String CHANNEL_NAME = "Aplicaciones-2018-2";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    int notificacion = 1;
    NotificationManager manager;


    private Button btn_subir;
    private ProgressDialog dialog;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    ImageView img_atras;
    ImageView img_receta;
    Spinner spincat;
    Button btn_publicar;
    EditText txt_nombrereceta;
    EditText txt_preparacion;
    EditText txt_ingredientes;
    String[] categorias = {"Seleccione", "Arroces", "Pasta y Pizza", "Verduras", "Sopas", "Postres"};

    private String path;
    private Publicacion publicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_chef);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        btn_subir = findViewById(R.id.btn_subir);
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);

        img_atras = findViewById(R.id.img_atras);
        img_receta = findViewById(R.id.img_receta);
        txt_nombrereceta = findViewById(R.id.id_nombrereceta);
        txt_preparacion = findViewById(R.id.id_preparacion);
        txt_ingredientes = findViewById(R.id.id_ingredientes);
        spincat = findViewById(R.id.id_spincat);
        btn_publicar = findViewById(R.id.btn_publicar);
        spincat.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias));


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

        btn_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!txt_nombrereceta.getText().toString().equals("") && !spincat.getSelectedItem().toString().equals("Seleccione") && !txt_ingredientes.getText().toString().equals("") && !txt_preparacion.getText().toString().equals("")) {

                    publicacion = new Publicacion(path, txt_nombrereceta.getText().toString(), spincat.getSelectedItem().toString(), txt_preparacion.getText().toString(), txt_ingredientes.getText().toString());
                    DatabaseReference rf = db.getReference().child("publicaciones").push();
                    rf.setValue(publicacion);

                    Toast.makeText(PublicarChef.this, "Se ha subido tu publicación", Toast.LENGTH_SHORT).show();

                    actualizarCampos();

                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //Manejar notificaciones en OREO
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //Se tiene que definir un Notficacion Channel
                        //Crear 3 constantes: CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE
                        NotificationChannel canal = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
                        manager.createNotificationChannel(canal);
                    }


                    NotificationCompat.Builder builder;
                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                    InicioChef inicio = new InicioChef();

                    int icono = R.mipmap.ic_launcher_round;
                    Intent intent = new Intent(PublicarChef.this, InicioChef.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(PublicarChef.this, 0, intent, 0);

                    builder = new NotificationCompat.Builder(PublicarChef.this, CHANNEL_ID).setContentIntent(pendingIntent).
                            setSmallIcon(icono).
                            setContentTitle(txt_nombrereceta.getText().toString() + "").
                            setContentText("Deliciosa nueva receta, corre a enterarte").
                            setVibrate(new long[]{100, 250, 100, 500}).
                            setAutoCancel(true).
                            setPriority(NotificationCompat.PRIORITY_HIGH);
                    manager.notify(1, builder.build());


                } else {
                    Toast.makeText(PublicarChef.this, "Por favor, asegurese de que todos los campos estén llenos", Toast.LENGTH_SHORT).show();
                }
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

    public void actualizarCampos() {
        txt_nombrereceta.setText("");
        spincat.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias));
        txt_preparacion.setText("");
        txt_ingredientes.setText("");
        img_receta.setImageBitmap(null);
    }
}
