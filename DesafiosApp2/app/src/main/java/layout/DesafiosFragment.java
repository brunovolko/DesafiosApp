package layout;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.SignupFragment;
import volcovinskygwiazda.desafiosapp2.Usuario;
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;

public class DesafiosFragment extends Fragment {

    private ListView listViewDesafios;
    private listaDesafiosAdapter adapterDesafios;
    private List<desafio> listaDesafios;
    private int cantDesafios;
    MainActivity actividadAnfitriona;
    AlertDialog alert;
    private TextView displayCantDesafios;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_desafios, container, false);

        listViewDesafios = (ListView)rootView.findViewById(R.id.listViewDesafios);
        Log.d("Estado", "Adapter encontrado");
        AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
        builder.setMessage("Cargando desafios");
        alert = builder.create();
        alert.show();
        Log.d("Estado", actividadAnfitriona.Usuario.Token);

        new buscarDesafiosOnline().execute(actividadAnfitriona.Usuario.Token);
        Log.d("Estado", "Desafios cargados");

        displayCantDesafios = (TextView)rootView.findViewById(R.id.displayCantDesafios);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actividadAnfitriona = (MainActivity)getActivity();
    }

    // Definimos AsyncTask
    private class buscarDesafiosOnline extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", Toast.LENGTH_SHORT);
                miToast.show();
            }
            else if(datos.equals("error2"))
            {
                // El token está mal, asi que a borrarloo y que vuelva al inicio
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Tu sesión expiró, vuelve a iniciar sesion.", Toast.LENGTH_SHORT);
                miToast.show();
                Fragment fragment;
                fragment = new BienvenidaFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContenedor, fragment);
                ft.commit();
            }
            else
            {
                //ok
                try
                {
                    //Parseo el JSON
                    JSONArray jsonArray = new JSONArray(datos);
                    JSONObject jsonObject;
                    listaDesafios = new ArrayList<>();
                    desafio desafioTemp;
                    cantDesafios = jsonArray.length();
                    displayCantDesafios.setText(String.valueOf(cantDesafios) + " desafíos disponibles");
                    for(int pos = 0; pos < cantDesafios; pos++)
                    {
                        jsonObject = new JSONObject(jsonArray.get(pos).toString());
                        int IDDESAFIO = Integer.valueOf(jsonObject.getString("IDDESAFIO"));
                        int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                        String DESAFIO = jsonObject.getString("DESAFIO");
                        String USUARIO = jsonObject.getString("USUARIO");
                        desafioTemp = new desafio(IDDESAFIO, IDUSUARIO, DESAFIO, USUARIO);
                        listaDesafios.add(desafioTemp);

                    }

                    adapterDesafios = new listaDesafiosAdapter(getActivity(), listaDesafios);
                    Log.d("Estado", "Listo para rockearla");
                    listViewDesafios.setAdapter(adapterDesafios);
                    Log.d("Estado", "Adapter seteado");

                }
                catch (JSONException e)
                {
                    Toast miToast;
                    miToast = Toast.makeText(actividadAnfitriona, e.getMessage(), Toast.LENGTH_LONG);
                    miToast.show();
                }
            }


            //miToast.show();
        }

        @Override
        protected String doInBackground(String... parametros) {
            Log.d("Estado", "Entra al doInBackground");
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listardesafios.php")
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


}
