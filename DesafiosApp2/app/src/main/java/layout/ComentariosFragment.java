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
import volcovinskygwiazda.desafiosapp2.Comentario;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaComentariosAdapter;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class ComentariosFragment extends Fragment {

    View vista;
    MainActivity actividadAnfitriona;
    private List<Comentario> listaComentarios;
    private ListView listViewComentarios;
    AlertDialog alert;
    private listaComentariosAdapter adapterComentarios;
    ImageView btnVolverDeComentarios;
    TextView tituloCantComentarios;
    TextView displayErrores;
    int cantComentarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_comentarios, container, false);
        actividadAnfitriona = (MainActivity)getActivity();

        listViewComentarios = (ListView)vista.findViewById(R.id.listViewComentarios);

        tituloCantComentarios = (TextView)vista.findViewById(R.id.tituloCantComentarios);

        btnVolverDeComentarios = (ImageView)vista.findViewById(R.id.btnVolverDeComentarios);
        btnVolverDeComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new HomeFragment());
            }
        });

        displayErrores = (TextView)vista.findViewById(R.id.displayErrores);
        displayErrores.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
        builder.setMessage("Cargando comentarios...");
        alert = builder.create();
        alert.show();

        new buscarComentarios().execute(actividadAnfitriona.Usuario.Token);



        return vista;
    }


    // Definimos AsyncTask
    private class buscarComentarios extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                displayErrores.setVisibility(View.VISIBLE);
                displayErrores.setText("Comprueba tu conexión a Internet");
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
                    listaComentarios = new ArrayList<>();
                    Comentario comentarioTemp;
                    cantComentarios = jsonArray.length();
                    tituloCantComentarios.setText(String.valueOf(cantComentarios) + " comentarios");
                    if(cantComentarios == 0)
                    {
                        displayErrores.setVisibility(View.VISIBLE);
                        displayErrores.setText("No hay comentarios aún!");
                    }
                    else
                    {
                        for(int pos = 0; pos < cantComentarios; pos++)
                        {
                            jsonObject = new JSONObject(jsonArray.get(pos).toString());
                            int IDCOMENTARIO = Integer.valueOf(jsonObject.getInt("IDCOMENTARIO"));
                            String COMENTARIO = String.valueOf(jsonObject.getString("COMENTARIO"));
                            int IDUSUARIO = jsonObject.getInt("IDUSUARIO");
                            String USUARIO = jsonObject.getString("USUARIO");
                            int IDPUBLICACION = jsonObject.getInt("IDPUBLICACION");
                            comentarioTemp = new Comentario(IDCOMENTARIO, IDPUBLICACION, IDUSUARIO, COMENTARIO, USUARIO);
                            listaComentarios.add(comentarioTemp);

                        }

                        adapterComentarios = new listaComentariosAdapter(getActivity(), listaComentarios);
                        Log.d("Estado", "Listo para rockearla");
                        listViewComentarios.setAdapter(adapterComentarios);
                        Log.d("Estado", "Adapter seteado");

                        registerForContextMenu(listViewComentarios);

                        /*listViewComentarios.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                desafioClickeado((int)view.getTag());
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
                    .addFormDataPart("idPublicacion", String.valueOf(actividadAnfitriona.comentariosViendo))
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listarComentarios.php")
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
