package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import volcovinskygwiazda.desafiosapp2.PublicacionesPerfilDesafioAdapter;
import volcovinskygwiazda.desafiosapp2.PublicacionesPerfilTemporalAdapter;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.Usuario;
import volcovinskygwiazda.desafiosapp2.publicacion;

import static android.widget.Toast.LENGTH_SHORT;

public class PerfilDesafioFragment extends Fragment {

    MainActivity actividadAnfitriona;
    View vista;
    private List<publicacion> listaPublicaciones;
    private int cantPublicaciones;
    private PublicacionesPerfilDesafioAdapter adapterPublicaciones;
    ListView listViewPublicacionesPerfilDesafio;
    TextView displayErrores, tituloCantPerfilDesafio;
    ImageView btnVolverDelPerfilDeLaLista;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_perfil_desafio, container, false);
        actividadAnfitriona = (MainActivity)getActivity();
        displayErrores = (TextView)vista.findViewById(R.id.displayErrores);
        tituloCantPerfilDesafio = (TextView)vista.findViewById(R.id.tituloCantPerfilDesafio);
        btnVolverDelPerfilDeLaLista = (ImageView)vista.findViewById(R.id.btnVolverDelPerfilDeLaLista);
        btnVolverDelPerfilDeLaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        listViewPublicacionesPerfilDesafio = (ListView)vista.findViewById(R.id.listViewPublicacionesPerfilDesafio);

        displayErrores.setVisibility(View.VISIBLE);
        displayErrores.setText("Cargando publicaciones");

        new ejecutarBusqueda().execute(actividadAnfitriona.Usuario.Token, String.valueOf(actividadAnfitriona.perfilDesafioViendo));



        return vista;
    }

    void regresar()
    {
        switch (actividadAnfitriona.ref)
        {
            case "perfil":
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilFragment());
                break;
        }
    }


    // Definimos AsyncTask
    private class ejecutarBusqueda extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            Log.d("Estado", datos);

            if(datos.equals("error"))
            {
                displayErrores.setText("Comprueba tu conexión a Internet.");
                Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", LENGTH_SHORT).show();
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
                try {

                    //Parseo el JSON
                    JSONArray jsonArray = new JSONArray(datos);
                    JSONObject jsonObject;
                    listaPublicaciones = new ArrayList<>();
                    publicacion publicacionTemp;
                    cantPublicaciones = jsonArray.length();
                    if(cantPublicaciones == 0)
                    {
                        displayErrores.setText("Aún nadie completó el desafío!");
                    }
                    else
                    {
                        tituloCantPerfilDesafio.setText(cantPublicaciones + " publicaciones");
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
                            publicacionTemp = new publicacion(IDPUBLICACION, IDUSUARIO, DESAFIO, USUARIO, TIENEIMAGEN, CANTIDADCOMENTARIOS);
                            listaPublicaciones.add(publicacionTemp);
                            Log.d("Estado", jsonArray.get(pos).toString());

                        }

                        adapterPublicaciones = new PublicacionesPerfilDesafioAdapter(getActivity(), listaPublicaciones);
                        Log.d("Estado", "Listo para rockearla");


                        listViewPublicacionesPerfilDesafio.setAdapter(adapterPublicaciones);
                        Log.d("Estado", "Adapter seteado");

                        listViewPublicacionesPerfilDesafio.setDivider(null);




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
            Log.d("Estado", datos);


        }


        @Override
        protected String doInBackground(String... parametros) {
            Log.d("Estado", "Entra al doInBackground");
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .addFormDataPart("idDesafio", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listarPerfilDesafio.php")
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
