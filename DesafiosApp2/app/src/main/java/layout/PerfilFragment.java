package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import volcovinskygwiazda.desafiosapp2.PublicacionesPerfilTemporalAdapter;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.Usuario;
import volcovinskygwiazda.desafiosapp2.listaUsuariosAdapter;
import volcovinskygwiazda.desafiosapp2.publicacion;

import static android.widget.Toast.LENGTH_SHORT;

public class PerfilFragment extends Fragment {

    MainActivity actividadAnfitriona;
    TextView tituloUsuarioPerfil;
    TextView seguidoresPerfil;
    ImageView btnVolverDelPerfil;
    private List<publicacion> listaPublicaciones;
    private int cantPublicaciones;
    private PublicacionesPerfilTemporalAdapter adapterPublicaciones;
    ImageView imagenUsuarioPerfil;
    TextView btnSeguimiento;
    TextView seguidosPerfil;
    TextView displayErrores;
    ListView listviewPublicacionesHechasPerfilTemporal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        actividadAnfitriona = (MainActivity)getActivity();

        btnSeguimiento = (TextView)vista.findViewById(R.id.btnSeguimiento);

        btnVolverDelPerfil = (ImageView)vista.findViewById(R.id.btnVolverDelPerfil);
        btnVolverDelPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new HomeFragment());
            }
        });
        tituloUsuarioPerfil = (TextView)vista.findViewById(R.id.tituloUsuarioPerfil);
        seguidoresPerfil = (TextView)vista.findViewById(R.id.seguidoresPerfil);
        seguidosPerfil = (TextView)vista.findViewById(R.id.seguidosPerfil);
        imagenUsuarioPerfil = (ImageView)vista.findViewById(R.id.imagenPerfilPublico);

        listviewPublicacionesHechasPerfilTemporal = (ListView)vista.findViewById(R.id.listviewPublicacionesHechasPerfilTemporal);
        displayErrores = (TextView)vista.findViewById(R.id.displayErrores);

        Log.d("Estado", "Arrancamo el async");
        new ejecutarBusqueda().execute(actividadAnfitriona.Usuario.Token, String.valueOf(actividadAnfitriona.perfilViendo));
        Log.d("Estado", "Terminamos el async");

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
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new BienvenidaFragment());
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
                            publicacionTemp = new publicacion(IDPUBLICACION, IDUSUARIO, DESAFIO, USUARIO, TIENEIMAGEN);
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




    // Definimos AsyncTask
    private class ejecutarBusqueda extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);

            if(datos.equals("error"))
            {
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
                try
                {
                    //Parseo el JSON
                    JSONObject jsonObject = new JSONObject(datos);
                    Usuario usuarioTemp;


                    int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                    String USUARIO = jsonObject.getString("USUARIO");
                    String TIENEIMAGEN = jsonObject.getString("TIENEIMAGEN");
                    int SEGUIDORES = Integer.valueOf(jsonObject.getString("SEGUIDORES"));
                    int SEGUIDOS = Integer.valueOf(jsonObject.getString("SEGUIDOS"));
                    /*String SIGUIENDO = jsonObject.getString("SIGUIENDO");*/


                    usuarioTemp = new Usuario(IDUSUARIO, USUARIO, Boolean.valueOf(TIENEIMAGEN), SEGUIDORES, SEGUIDOS);
                    Log.d("Estado", String.valueOf(usuarioTemp.IDUsuario));
                    Log.d("Estado", usuarioTemp.Usuario);
                    Log.d("Estado", String.valueOf(usuarioTemp.tieneImagen));
                    Log.d("Estado", String.valueOf(usuarioTemp.Seguidores));
                    Log.d("Estado", String.valueOf(usuarioTemp.Seguidos));
                    tituloUsuarioPerfil.setText(usuarioTemp.Usuario);
                    seguidosPerfil.setText(String.valueOf(usuarioTemp.Seguidos));
                    seguidoresPerfil.setText(String.valueOf(usuarioTemp.Seguidores));

                    imagenUsuarioPerfil.setImageResource(R.drawable.defaultuserperfil);

                    /*if(SIGUIENDO.equals("1"))
                    {
                        btnSeguimiento.setTextColor(Color.BLACK);
                        btnSeguimiento.setBackgroundColor(Color.WHITE);
                    }
                    else
                    {
                        btnSeguimiento.setTextColor(Color.WHITE);
                        btnSeguimiento.setBackgroundColor(Color.parseColor(String.valueOf(R.color.celeste)));
                    }*/




                    if(TIENEIMAGEN.equals("1"))
                    {
                        Log.d("Estado", "Encontro imagen, esta en http://proyectoinfo.hol.es/imagenes/usuarios/perfiles/"+IDUSUARIO+".png");
                        Glide.with(actividadAnfitriona)
                                .load("http://proyectoinfo.hol.es/imagenes/usuarios/perfiles/"+IDUSUARIO+".png")
                                .into(imagenUsuarioPerfil);

                    }
                    else
                    {
                        Log.d("Estado", "No tiene imagen");
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
                    .addFormDataPart("idUsuario", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/cargarPerfil.php")
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
