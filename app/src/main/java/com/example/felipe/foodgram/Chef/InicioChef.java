package com.example.felipe.foodgram.Chef;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.felipe.foodgram.Cocinero.Inicio;
import com.example.felipe.foodgram.FeedFragmentChef;
import com.example.felipe.foodgram.Login;
import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.modelo.Usuario;
import com.example.felipe.foodgram.util.UtilDomi;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InicioChef extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_GALLERY = 101;


    TextView tv_nombreUsuarioChef;
    TextView tv_CorreoMenuChef;
    ImageView img_PerfilMenuChef;
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage storage;

    private ProgressDialog mProgressDialog;

    Context context;


    DatabaseReference referencia;

    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_chef);

        android.support.design.widget.NavigationView nav_view = findViewById(R.id.nav_view);
        View v = nav_view.getHeaderView(0);


        tv_nombreUsuarioChef = v.findViewById(R.id.tv_nombreUsuarioChef);
        tv_CorreoMenuChef = v.findViewById(R.id.tv_CorreoMenuChef);
        img_PerfilMenuChef = v.findViewById(R.id.img_PerfilMenuChef);
        mProgressDialog = new ProgressDialog(this);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 11);


        referencia = db.getReference().child("usuarios").child("cocinero").child(auth.getCurrentUser().getUid());
        final StorageReference refer = storage.getReference().child("usuarios")
                .child(auth.getCurrentUser().getUid());


        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Usuario cocinero = dataSnapshot.getValue(Usuario.class);
                    //Log.e("valores", usua.getNombre() + " ");
                    tv_nombreUsuarioChef.setText(cocinero.getNombre());
                    tv_CorreoMenuChef.setText(cocinero.getEmail());

                    refer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(InicioChef.this).load(uri).into(img_PerfilMenuChef);
                            img_PerfilMenuChef.getAdjustViewBounds();

                        }
                    });
                } else {
                    DatabaseReference segundoIntento = db.getReference().child("usuarios").child("chef").child(auth.getCurrentUser().getUid());
                    segundoIntento.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(">>>", dataSnapshot.toString());
                            if (dataSnapshot.getValue() != null) {
                                Usuario chef = dataSnapshot.getValue(Usuario.class);
                                tv_nombreUsuarioChef.setText(chef.getNombre());
                                tv_CorreoMenuChef.setText(chef.getEmail());

                                refer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(InicioChef.this).load(uri).into(img_PerfilMenuChef);
                                        img_PerfilMenuChef.getAdjustViewBounds();

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio_chef, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fmanager = getSupportFragmentManager();

        if (id == R.id.nav_miPerfilChef) {
            fmanager.beginTransaction().replace(R.id.contenedor, new PerfilFragmentChef()).commit();


        } else if (id == R.id.nav_MiPosterChef) {
            fmanager.beginTransaction().replace(R.id.contenedor, new MiPosterChef()).commit();

        } else if (id == R.id.nav_CalificacionChef) {
            fmanager.beginTransaction().replace(R.id.contenedor, new CalificacionChef()).commit();

        } else if (id == R.id.nav_CerrarSesionChef) {

            cerrarSesion();
        } else if (id == R.id.nav_FeedChef) {
            fmanager.beginTransaction().replace(R.id.contenedor, new FeedFragmentChef()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Este metodo me permite ir a la pantalla de Login
    public void irALogin() {
        Intent intent = new Intent(InicioChef.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Metodo que me permite cerrar sesion e ir a la pantalla de Login
    public void cerrarSesion() {
        LoginManager.getInstance().logOut();
        irALogin();

    }


    public void cambiarImagen(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        //i.setType("video/*");
        //i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_GALLERY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {

            mProgressDialog.setTitle("Subiendo...");
            mProgressDialog.setMessage("Subiendo foto a tu perfil");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            path = UtilDomi.getPath(InicioChef.this, data.getData());
            Bitmap m = BitmapFactory.decodeFile(path);
            img_PerfilMenuChef.setImageBitmap(m);


            //Cargar las imagenes
            final StorageReference ref = storage.getReference().child("usuarios")
                    .child(auth.getCurrentUser().getUid());



            ref.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgressDialog.dismiss();


                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(InicioChef.this).load(uri).into(img_PerfilMenuChef);
                        }
                    });

                    Toast.makeText(InicioChef.this, "Se cambio exitosamente", Toast.LENGTH_SHORT).show();
                }
            });




            /**
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(img_PerfilMenuChef);


                }
            });

            */
        }

    }
}
