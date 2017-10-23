package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;

public class NuevoDesafioFragment extends Fragment {

    MainActivity actividadAnfitriona;
    Button btnPublicarDesafio;
    EditText txtDesafio;
    TextView displayCaracteresRestantes;
    AlertDialog alert;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_nuevo_desafio, container, false);
        actividadAnfitriona = (MainActivity)getActivity();
        btnPublicarDesafio = (Button)vista.findViewById(R.id.btnPublicarDesafio);
        txtDesafio = (EditText)vista.findViewById(R.id.txtDesafio);
        displayCaracteresRestantes = (TextView)vista.findViewById(R.id.displayCaracteresRestantes);
        txtDesafio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayCaracteresRestantes.setText(String.valueOf(140 - txtDesafio.length()) + " caracteres restantes");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnPublicarDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearDesafio();
            }
        });

        return vista;
    }


    void crearDesafio()
    {
        actividadAnfitriona.cerrarTeclado();
        AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
        builder.setMessage("Creando desafio...");
        alert = builder.create();



        if(!txtDesafio.getText().toString().trim().isEmpty())
        {
            alert.show();
            new buscarDesafiosOnline().execute(actividadAnfitriona.Usuario.Token, txtDesafio.getText().toString());
        }
    }

    // Definimos AsyncTask
    private class buscarDesafiosOnline extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", Toast.LENGTH_SHORT).show();
            }
            else if(datos.equals("error2"))
            {
                // El token está mal, asi que a borrarloo y que vuelva al inicio
                Toast.makeText(actividadAnfitriona, "Tu sesión expiró, vuelve a iniciar sesion.", Toast.LENGTH_SHORT).show();
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new BienvenidaFragment(), false);
                actividadAnfitriona.vaciarStackFragments();
            }
            else
            {
                //ok
                Toast.makeText(actividadAnfitriona, "Tu desafio fué creado con éxito!", Toast.LENGTH_SHORT).show();
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new DesafiosFragment(), true);
            }


        }

        @Override
        protected String doInBackground(String... parametros) {

            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .addFormDataPart("desafio", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/crearDesafio.php")
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String resultado = response.body().string();
                return resultado;
            } catch (IOException e) {
                Log.d("Debug", e.getMessage());
                //mostrarError(e.getMessage()); // Error de Network
                return "error";
            }

        }
    }

}
