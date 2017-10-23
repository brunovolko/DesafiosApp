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
import volcovinskygwiazda.desafiosapp2.PublicacionesPerfilTemporalAdapter;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.publicacion;

import static android.widget.Toast.LENGTH_SHORT;

public class DesafiosCompletadosFragment extends Fragment {

    MainActivity actividadAnfitriona;
    View vista;
    private List<publicacion> listaPublicaciones;
    private int cantPublicaciones;
    TextView displayErrores;
    private PublicacionesPerfilTemporalAdapter adapterPublicaciones;
    ListView listviewPublicacionesHechasPerfilTemporal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_desafios_completados, container, false);
        actividadAnfitriona = (MainActivity)getActivity();

        displayErrores = (TextView)vista.findViewById(R.id.displayErrores);

        listviewPublicacionesHechasPerfilTemporal = (ListView)vista.findViewById(R.id.listviewPublicacionesHechasPerfilTemporal);

        displayErrores.setVisibility(View.VISIBLE);
        displayErrores.setText("Cargando publicaciones...");
        new buscarPublicacionesHechas().execute(actividadAnfitriona.Usuario.Token, String.valueOf(actividadAnfitriona.perfilViendo));

        return vista;
    }

    // Definimos AsyncTask
    private class buscarPublicacionesHechas extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);

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
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new BienvenidaFragment(), false);
                actividadAnfitriona.vaciarStackFragments();
            }
            else
            {
                Log.d("Estado", datos);
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
                        displayErrores.setText("El usuario no ha completado ningún desafío aún!");
                    }
                    else
                    {
                        displayErrores.setVisibility(View.GONE);
                        for(int pos = 0; pos < cantPublicaciones; pos++)
                        {
                            jsonObject = new JSONObject(jsonArray.get(pos).toString());
                            int IDPUBLICACION = Integer.valueOf(jsonObject.getString("IDPUBLICACION"));
                            int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                            String DESAFIO = jsonObject.getString("DESAFIO");
                            String USUARIO = jsonObject.getString("USUARIO");
                            int TIENEIMAGEN = jsonObject.getInt("TIENEIMAGEN");
                            int CANTIDADCOMENTARIOS = jsonObject.getInt("CANTIDADCOMENTARIOS");
                            int CALIFICACIONESPOSITIVAS = jsonObject.getInt("CANTIDADPOSITIVOS");
                            int CALIFICACIONESNEGATIVAS = jsonObject.getInt("CANTIDADNEGATIVOS");
                            int MICALIFICACION = jsonObject.getInt("MICALIFICACION");
                            publicacionTemp = new publicacion(IDPUBLICACION, IDUSUARIO, DESAFIO, USUARIO, TIENEIMAGEN, CANTIDADCOMENTARIOS, CALIFICACIONESPOSITIVAS, CALIFICACIONESNEGATIVAS, MICALIFICACION);

                            listaPublicaciones.add(publicacionTemp);
                            Log.d("Estado", jsonArray.get(pos).toString());

                        }

                        adapterPublicaciones = new PublicacionesPerfilTemporalAdapter(getActivity(), listaPublicaciones);
                        Log.d("Estado", "Listo para rockearla");


                        listviewPublicacionesHechasPerfilTemporal.setAdapter(adapterPublicaciones);
                        Log.d("Estado", "Adapter seteado");

                        listviewPublicacionesHechasPerfilTemporal.setDivider(null);




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
                    .addFormDataPart("idUsuario", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listarPublicacionesHechasPerfil.php")
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
