package volcovinskygwiazda.desafiosapp;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText txtNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
    }

    private class BuscarDatosTask extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            txtNombre.setText(datos);
        }

        String resultado;

        @Override
        protected String doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://www.ayudavisas.com/backendEjemploPost.php")
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
                resultado = response.body().string();
            } catch (IOException e) {
                Log.d("Error",e.getMessage());             // Error de Network
            }
            String url = parametros[0];
            return resultado;
        }
    }

    public void ejecutar(View vista)
    {
        String url ="http://www.ayudavisas.com/backendEjemploPost.php";
        new BuscarDatosTask().execute(url);
    }
}
