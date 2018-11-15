package com.example.felipe.foodgram;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.net.MalformedURLException;
import java.net.URL;


public class Inicio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView prueba;
    ImageView img_facebook;

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
@Override public void onCompleted(JSONObject object, GraphResponse response) {

Log.d("response", response.toString());
obtenerDatosFacebook(object);

}
});

 Bundle parameters = new Bundle();
 parameters.putString("fields", "id,email,birthday,friends");
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Este metodo se encarga de obtener los datos de Facebook, como el correo, fecha, imagen y cantidad de amigos


 private void obtenerDatosFacebook(JSONObject object) {
 try {
 URL profile_picture = new URL("https://graph.facebook.com/" + object.get("id") + "/picture?width=250&height=250");
 Picasso.get().load(profile_picture.toString()).into(img_facebook);

 //Mostramos la información de prueba en el Inicio
 prueba.setText("EMAIL : " + object.getString("email") + "\n" +
 "BIRTHDAY : " + object.getString("birthday") + "\n" +
 "FRIENDS : " + object.getJSONObject("friends").getJSONObject("summary").getString("total_count")
 );

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
 public void cerrarSesion(View view) {
 LoginManager.getInstance().logOut();
 irALogin();

 }


}
