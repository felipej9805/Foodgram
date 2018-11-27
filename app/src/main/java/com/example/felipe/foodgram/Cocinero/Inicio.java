package com.example.felipe.foodgram.Cocinero;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.foodgram.FeedFragment;
import com.example.felipe.foodgram.Login;
import com.example.felipe.foodgram.R;
import com.example.felipe.foodgram.modelo.Usuario;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


public class Inicio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView img_PerfilMenu;
    TextView tv_CorreoMenu;
    TextView tv_nombreUsuario;

    FirebaseAuth auth;
    FirebaseDatabase db;

    DatabaseReference referencia;
    DatabaseReference usuario;
    Usuario actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        //img_PerfilMenu = findViewById(R.id.img_PerfilMenu);

        android.support.design.widget.NavigationView nav_view = findViewById(R.id.nav_view);
        View v = nav_view.getHeaderView(0);
        tv_nombreUsuario = v.findViewById(R.id.tv_nombreUsuario);
        tv_CorreoMenu = v.findViewById(R.id.tv_CorreoMenu);
        Log.e(">>>", "" + tv_nombreUsuario);

        //Token del inicio de sesión para el Inicio con Facebook
        int facebook = -1;
        Bundle datos = getIntent().getExtras();

        if (datos != null) {
            facebook = getIntent().getExtras().getInt("facebook");
        }
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            String accessToken = AccessToken.getCurrentAccessToken().getToken();
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d("response", response.toString());
                    obtenerDatosFacebook(object);
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,birthday,friends,first_name,last_name");
            request.setParameters(parameters);
            request.executeAsync();

            Toast.makeText(getApplicationContext(), "BIENVENIDO", Toast.LENGTH_SHORT).show();
        } else {
            if (facebook == 1) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "BIENVENIDO", Toast.LENGTH_SHORT).show();
            }
        }


        referencia = db.getReference().child("usuarios").child("cocinero").child(auth.getCurrentUser().getUid());


        // Lo que hace es identificar el usuario y agregar el nombre y el correo al TextView
        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e(">>>", dataSnapshot.toString());
                if (dataSnapshot.getValue() != null) {
                    Usuario cocinero = dataSnapshot.getValue(Usuario.class);
                    //Log.e("valores", usua.getNombre() + " ");
                    tv_nombreUsuario.setText(cocinero.getNombre());
                    tv_CorreoMenu.setText(cocinero.getEmail());
                } else {
                    DatabaseReference segundoIntento = db.getReference().child("usuarios").child("chef").child(auth.getCurrentUser().getUid());
                    segundoIntento.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(">>>", dataSnapshot.toString());
                            if (dataSnapshot.getValue() != null) {
                                Usuario chef = dataSnapshot.getValue(Usuario.class);
                                tv_nombreUsuario.setText(chef.getNombre());
                                tv_CorreoMenu.setText(chef.getEmail());

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

        // se llama al fragment que se va a mostrar de primero
        FragmentManager fmanager = getSupportFragmentManager();
        fmanager.beginTransaction().replace(R.id.contenedor, new FeedFragment()).commit();


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
        getMenuInflater().inflate(R.menu.inicio, menu);
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


    //Segun el fragment seleccionado, me lleva al Fragment correspondiente
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fmanager = getSupportFragmentManager();

        if (id == R.id.nav_miPerfil) {
            fmanager.beginTransaction().replace(R.id.contenedor, new PerfilFragment()).commit();

        } else if (id == R.id.nav_miChef) {
            fmanager.beginTransaction().replace(R.id.contenedor, new ChefFragment()).commit();

        } else if (id == R.id.nav_Feed) {
            fmanager.beginTransaction().replace(R.id.contenedor, new FeedFragment()).commit();
        } else if (id == R.id.nav_Canasta) {
            fmanager.beginTransaction().replace(R.id.contenedor, new CanastaFragment()).commit();

        } else if (id == R.id.nav_RecetaParaTi) {
            fmanager.beginTransaction().replace(R.id.contenedor, new RecetaFragment()).commit();

        } else if (id == R.id.nav_Categorias) {
            fmanager.beginTransaction().replace(R.id.contenedor, new CategoriaFragment()).commit();

        } else if (id == R.id.nav_Calificanos) {
            //  fmanager.beginTransaction().replace(R.id.contenedor, new FeedFragment()).commit();
        } else if (id == R.id.nav_CerrarSesion) {

            cerrarSesion();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Este metodo se encarga de obtener los datos de Facebook, como el correo, fecha, imagen y cantidad de amigos
    private void obtenerDatosFacebook(JSONObject object) {
        try {

            URL profile_picture = new URL("https://graph.facebook.com/" + object.get("id") + "/picture?width=150&height=150");
            Picasso.get().load(profile_picture.toString()).into(img_PerfilMenu);

            tv_nombreUsuario.setText(object.getString("first_name") + object.getString("last_name"));
            tv_CorreoMenu.setText(object.getString("email"));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Este metodo me permite ir a la pantalla de Login
    public void irALogin() {
        Intent intent = new Intent(Inicio.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Metodo que me permite cerrar sesion e ir a la pantalla de Login
    public void cerrarSesion() {
        LoginManager.getInstance().logOut();
        irALogin();

    }


}
