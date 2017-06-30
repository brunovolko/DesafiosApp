package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import volcovinskygwiazda.desafiosapp2.PublicacionesHomeAdapter;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;
import volcovinskygwiazda.desafiosapp2.publicacion;

import static android.widget.Toast.LENGTH_SHORT;

public class HomeFragment extends Fragment {

    View vista;
    ImageView btnAbirBuscar;
    private List<publicacion> listaPublicaciones;
    MainActivity actividadAnfitriona;
    LinearLayout displaySigueUsuarios;
    private int cantPublicaciones;
    private PublicacionesHomeAdapter adapterPublicaciones;
    ListView listViewPublicacionesHome;
    AlertDialog alert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actividadAnfitriona = (MainActivity)getActivity();

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_home, container, false);

        displaySigueUsuarios = (LinearLayout)vista.findViewById(R.id.displaySigueUsuarios);
        displaySigueUsuarios.setVisibility(View.GONE);

        btnAbirBuscar = (ImageView)vista.findViewById(R.id.btnAbrirBuscar);
        btnAbirBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new BuscadorFragment());
            }
        });

        listViewPublicacionesHome = (ListView)vista.findViewById(R.id.listViewPublicacionesHome);

        AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
        builder.setMessage("Cargando publicaciones...");
        alert = builder.create();
        alert.show();

        new buscarPublicaciones().execute(actividadAnfitriona.Usuario.Token);


        return vista;
    }

    // Definimos AsyncTask
    private class buscarPublicaciones extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", LENGTH_SHORT);
                miToast.show();
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
                    listaPublicaciones = new ArrayList<>();
                    publicacion publicacionTemp;
                    cantPublicaciones = jsonArray.length();
                    if(cantPublicaciones == 0)
                    {
                        displaySigueUsuarios.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        for(int pos = 0; pos < cantPublicaciones; pos++)
                        {
                            jsonObject = new JSONObject(jsonArray.get(pos).toString());
                            int IDPUBLICACION = Integer.valueOf(jsonObject.getString("IDPUBLICACION"));
                            int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                            String DESAFIO = jsonObject.getString("DESAFIO");
                            String USUARIO = jsonObject.getString("USUARIO");
                            int TIENEIMAGEN = jsonObject.getInt("TIENEIMAGEN");
                            publicacionTemp = new publicacion(IDPUBLICACION, IDUSUARIO, DESAFIO, USUARIO, TIENEIMAGEN);
                            listaPublicaciones.add(publicacionTemp);
                            Log.d("Estado", jsonArray.get(pos).toString());

                        }

                        adapterPublicaciones = new PublicacionesHomeAdapter(getActivity(), listaPublicaciones);
                        Log.d("Estado", "Listo para rockearla");
                        listViewPublicacionesHome.setAdapter(adapterPublicaciones);
                        Log.d("Estado", "Adapter seteado");

                        listViewPublicacionesHome.setDivider(null);

                        //registerForContextMenu(listViewPublicacionesHome);

                        /*listViewPublicacionesHome.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //desafioClickeado((int)view.getTag());
                            }
                        });*/
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
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listarPublicacionesHome.php")
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
