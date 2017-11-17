package volcovinskygwiazda.desafiosapp2;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import layout.BienvenidaFragment;
import layout.DesafiosFragment;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupFragment extends Fragment {

    View vista;
    MainActivity actividadAnfitriona;
    AlertDialog alert;
    EditText txtUsuario, txtEmail, txtPassword, txtPassword2;
    String usuario, pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_signup, container, false);

        actividadAnfitriona = (MainActivity)getActivity();



        Button btnCrearCuenta;

        txtUsuario = (EditText)vista.findViewById(R.id.txtUsuario);
        /*txtEmail = (EditText)vista.findViewById(R.id.txtEmail);*/
        txtPassword = (EditText)vista.findViewById(R.id.txtPassword);
        txtPassword2 = (EditText)vista.findViewById(R.id.txtPassword2);
        btnCrearCuenta = (Button)vista.findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registrar
                if(txtUsuario.getText().toString().trim().isEmpty()/* || txtEmail.getText().toString().trim().isEmpty()*/ || txtPassword.getText().toString().trim().isEmpty() || txtPassword2.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(actividadAnfitriona, "Ningun campo puede estar vacio", Toast.LENGTH_SHORT).show();
                }
                /*else if(!actividadAnfitriona.validarEmail(txtEmail.getText().toString()))
                {
                    Toast.makeText(actividadAnfitriona, "Ingrese un email válido", Toast.LENGTH_SHORT).show();
                }*/
                else if(!txtPassword.getText().toString().matches(txtPassword2.getText().toString()))
                {
                    Toast.makeText(actividadAnfitriona, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    usuario = txtUsuario.getText().toString();
                    pass = txtPassword.getText().toString();

                    //Validacion del cliente ok, registrar
                    AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
                    builder.setMessage("Registrando usuario...");
                    alert = builder.create();

                    new registrarTask().execute();



                }
            }
        });


        return vista;
    }


    // Definimos AsyncTask
    private class registrarTask extends AsyncTask<String, Void, String> {

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
            else if(datos.equals("ok"))
            {
                //ok
                Toast.makeText(actividadAnfitriona, "Usuario registrado con éxito!", Toast.LENGTH_SHORT).show();
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new LoginFragment(), false);
            }
            else
            {
                // El token está mal, asi que a borrarloo y que vuelva al inicio
                Toast.makeText(actividadAnfitriona, datos, Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(String... parametros) {

            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("usuario", usuario)
                    /*.addFormDataPart("email", parametros[1])*/
                    .addFormDataPart("password", pass)
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/registrar.php")
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
