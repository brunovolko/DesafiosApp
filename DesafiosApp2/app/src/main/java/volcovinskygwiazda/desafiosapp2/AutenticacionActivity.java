package volcovinskygwiazda.desafiosapp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import layout.BienvenidaFragment;
import layout.DesafiosFragment;
import layout.HomeFragment;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AutenticacionActivity extends AppCompatActivity {

    EditText txtUsuarioLogin;
    EditText txtContrasenaLogin;

    AlertDialog alert;
    baseSQLiteHelper accesoBase;
    SQLiteDatabase baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment;
        fragment = new BienvenidaFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentBienvenidaContent, fragment);
        ft.commit();
        setContentView(R.layout.activity_autenticacion);


        accesoBase = new baseSQLiteHelper(getApplicationContext(),"dataBase",null,1);
        baseDatos = accesoBase.getWritableDatabase();

        //Quiero ver si hay ya algo guardado

        if(baseDatos!=null)
        {
            //ok
            Cursor conjuntoRegistros;
            conjuntoRegistros = baseDatos.rawQuery("select token from usuario", null);
            Toast toastResultados;

            if(conjuntoRegistros.moveToFirst() == true)
            {
                // Muestro cartelito
                AlertDialog.Builder builder = new AlertDialog.Builder(AutenticacionActivity.this).setCancelable(false);
                builder.setMessage("Ingresando...");
                alert = builder.create();
                alert.show();



                // Ver si el token esta bien
                new IngresarTokenTask().execute(conjuntoRegistros.getString(0));

            }
        }
        else
        {
            //miToast = Toast.makeText(AutenticacionActivity.this, "Algo salió mal, :( vuelve a intentarlo.", Toast.LENGTH_LONG);
        }


    }




    public void abrirLogin(View vista)
    {
        Fragment fragment;
        fragment = new LoginFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentBienvenidaContent, fragment);
        ft.commit();
    }


    public void abrirSignup(View vista)
    {
        Fragment fragment;
        fragment = new SignupFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentBienvenidaContent, fragment);
        ft.commit();
    }

    // Definimos AsyncTask
    private class IngresarTokenTask extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(AutenticacionActivity.this, "Comprueba tu conexión a Internet", Toast.LENGTH_LONG);
                miToast.show();
            }
            else if(datos.equals("error2"))
            {
                // El token está mal, asi que a borrarloo
                baseDatos.delete("usuario", "", null);
            }
            else
            {
                //ok
                try
                {
                    //Parseo el JSON
                    JSONObject jsonResultado = new JSONObject(datos);
                    Usuario Usuario;
                    Usuario = new Usuario(jsonResultado.getString("Token"), jsonResultado.getInt("IDUsuario"), jsonResultado.getString("Usuario"));


                    // Update nuevos datos por las dudas
                    ContentValues valores = new ContentValues();
                    valores.put("usuario", Usuario.Usuario);
                    baseDatos.update("usuario", valores, "id = "+Usuario.IDUsuario, null);


                    Intent accederIntent;
                    accederIntent = new Intent(getApplicationContext(), MainActivity.class);
                    accederIntent.putExtra("Usuario", Usuario);
                    startActivity(accederIntent);

                }
                catch (JSONException e)
                {
                    Toast miToast;
                    miToast = Toast.makeText(AutenticacionActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    miToast.show();
                }
            }


            //miToast.show();
        }

        @Override
        protected String doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/ingresarToken.php")
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
                String resultado = response.body().string();
                return resultado;
            } catch (IOException e) {
                Log.d("Debug", e.getMessage());
                //mostrarError(e.getMessage()); // Error de Network
                return "error";
            }

        }
    }



    // Definimos AsyncTask
    private class IngresarTask extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();
            Log.d("Estado", datos);

            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(AutenticacionActivity.this, "Comprueba tu conexión a Internet", Toast.LENGTH_LONG);
                miToast.show();
            }
            else if(datos.equals("error2"))
            {
                Toast miToast;
                miToast = Toast.makeText(AutenticacionActivity.this, "Datos erroneos", Toast.LENGTH_LONG);
                miToast.show();
            }
            else
            {
                //ok
                //miToast = Toast.makeText(AutenticacionActivity.this, datos, Toast.LENGTH_LONG);
                baseSQLiteHelper accesoBase;
                SQLiteDatabase baseDatos;
                accesoBase = new baseSQLiteHelper(getApplicationContext(),"dataBase",null,1);
                baseDatos = accesoBase.getWritableDatabase();
                if(baseDatos!=null)
                {
                    //ok
                    try
                    {
                        //Parseo el JSON
                        JSONObject jsonResultado = new JSONObject(datos);
                        Usuario Usuario;
                        Usuario = new Usuario(jsonResultado.getString("Token"), jsonResultado.getInt("IDUsuario"), jsonResultado.getString("Usuario"));



                        ContentValues guardarToken;
                        guardarToken = new ContentValues();
                        guardarToken.put("token", Usuario.Token);
                        guardarToken.put("id", Usuario.IDUsuario);
                        guardarToken.put("usuario", Usuario.Usuario);
                        baseDatos.insert("usuario", null, guardarToken);


                        Intent accederIntent;
                        accederIntent = new Intent(getApplicationContext(), MainActivity.class);
                        accederIntent.putExtra("Usuario", Usuario);
                        startActivity(accederIntent);

                    }
                    catch (JSONException e)
                    {
                        Toast miToast;
                        miToast = Toast.makeText(AutenticacionActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                        miToast.show();
                    }




                }
                else
                {
                    Toast miToast;
                    miToast = Toast.makeText(AutenticacionActivity.this, "Algo salió mal, :( vuelve a intentarlo.", Toast.LENGTH_LONG);
                    miToast.show();
                }
            }


            //miToast.show();
        }

        @Override
        protected String doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("usuario", parametros[0])
                    .addFormDataPart("password", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/ingresar.php")
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();  // Llamo al API Rest servicio1 en ejemplo.com
                String resultado = response.body().string();
                return resultado;
            } catch (IOException e) {
                Log.d("Debug", e.getMessage());
                //mostrarError(e.getMessage()); // Error de Network
                return "error";
            }

        }
    }

    public void ejecutarIngresar(View vista)
    {
        Log.d("Estado", "Entro al void");
        txtUsuarioLogin = (EditText)findViewById(R.id.txtUsuarioLogin);
        txtContrasenaLogin = (EditText)findViewById(R.id.txtContrasenaLogin);
        // Compruebe si ninguna vista tiene el foco.
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AutenticacionActivity.this).setCancelable(false);
        builder.setMessage("Ingresando...");
        alert = builder.create();
        alert.show();
        new IngresarTask().execute(txtUsuarioLogin.getText().toString(), txtContrasenaLogin.getText().toString());


    }


}
