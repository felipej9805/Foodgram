package com.example.felipe.foodgram;

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

public class Inicio extends AppCompatActivity {

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
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

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
