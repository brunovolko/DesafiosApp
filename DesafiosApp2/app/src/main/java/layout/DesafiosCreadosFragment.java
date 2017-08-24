package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;
import volcovinskygwiazda.desafiosapp2.listaDesafiosCreadosAdapter;

import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_SHORT;


public class DesafiosCreadosFragment extends Fragment {

    MainActivity actividadAnfitriona;
    View vista;
    private ListView listviewDesafiosCreadosPerfil;
    private listaDesafiosCreadosAdapter adapterDesafios;
    private List<desafio> listaDesafios;
    TextView displayErrores;
    int cantDesafios;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_desafios_creados, container, false);
        actividadAnfitriona = (MainActivity)getActivity();

        displayErrores = (TextView)vista.findViewById(R.id.displayErrores);

        listviewDesafiosCreadosPerfil = (ListView) vista.findViewById(R.id.listviewDesafiosCreadosPerfil);


        displayErrores.setVisibility(View.VISIBLE);
        displayErrores.setText("Cargando desafíos...");

        new buscarDesafiosOnline().execute(actividadAnfitriona.Usuario.Token, String.valueOf(actividadAnfitriona.perfilViendo));



        return vista;
    }

    private void abrirPerfilDesafio(int idDesafio)
    {
        actividadAnfitriona.perfilDesafioViendo = idDesafio;
        actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilDesafioFragment());
    }




    // Definimos AsyncTask
    private class buscarDesafiosOnline extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);

            Log.d("Estado", datos);


            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", LENGTH_SHORT);
                miToast.show();
                displayErrores.setText("Error");
            }
            else if(datos.equals("error2"))
            {
                // El token está mal, asi que a borrarloo y que vuelva al inicio
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Tu sesión expiró, vuelve a iniciar sesion.", LENGTH_SHORT);
                miToast.show();
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new BienvenidaFragment());
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
                    if(cantDesafios == 0)
                    {
                        displayErrores.setText("El usuario aún no ha creado desafíos!");
                    }
                    else
                    {
                        for(int pos = 0; pos < cantDesafios; pos++)
                        {
                            jsonObject = new JSONObject(jsonArray.get(pos).toString());
                            int IDDESAFIO = Integer.valueOf(jsonObject.getString("IDDESAFIO"));
                            String DESAFIO = jsonObject.getString("DESAFIO");
                            desafioTemp = new desafio(IDDESAFIO, actividadAnfitriona.perfilViendo, DESAFIO);
                            listaDesafios.add(desafioTemp);

                        }

                        adapterDesafios = new listaDesafiosCreadosAdapter(getActivity(), listaDesafios);
                        Log.d("Estado", "Listo para rockearla");
                        listviewDesafiosCreadosPerfil.setAdapter(adapterDesafios);
                        Log.d("Estado", "Adapter seteado");

                        displayErrores.setVisibility(GONE);

                        registerForContextMenu(listviewDesafiosCreadosPerfil);

                        listviewDesafiosCreadosPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                actividadAnfitriona.ref = "perfil";
                                abrirPerfilDesafio((int)view.getTag());
                            }
                        });
                    }



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
                    .addFormDataPart("idUsuario", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listarDesafiosCreados.php")
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
