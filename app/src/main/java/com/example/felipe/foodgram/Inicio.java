package com.example.felipe.foodgram;

import android.os.Bundle;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;


public class Inicio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView prueba;
    ImageView img_facebook;
    ImageView img_PerfilMenu;
    TextView tv_CorreoMenu;
    TextView tv_nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        prueba = findViewById(R.id.prueba);
        img_facebook = (ImageView) findViewById(R.id.img_facebook);


        //Token del inicio de sesión


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

        } else {
            Toast.makeText(getApplicationContext(), "Error Toast", Toast.LENGTH_SHORT).show();
        }


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
            img_PerfilMenu = findViewById(R.id.img_PerfilMenu);
            tv_CorreoMenu = findViewById(R.id.tv_CorreoMenu);
            tv_nombreUsuario = findViewById(R.id.tv_nombreUsuario);

            URL profile_picture = new URL("https://graph.facebook.com/" + object.get("id") + "/picture?width=150&height=150");
            Picasso.get().load(profile_picture.toString()).into(img_PerfilMenu);

            tv_nombreUsuario.setText(object.getString("first_name") + object.getString("last_name"));
            tv_CorreoMenu.setText(object.getString("email"));


            //Mostramos la información de prueba en el Inicio
            /**
             prueba.setText("EMAIL : " + object.getString("email") + "\n" +
             "BIRTHDAY : " + object.getString("birthday") + "\n" +
             "FRIENDS : " + object.getJSONObject("friends").getJSONObject("summary").getString("total_count")
             );
             */
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
