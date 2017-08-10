package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import volcovinskygwiazda.desafiosapp2.Usuario;
import volcovinskygwiazda.desafiosapp2.listaUsuariosAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class PerfilFragment extends Fragment {

    MainActivity actividadAnfitriona;
    TextView tituloUsuarioPerfil;
    TextView seguidoresPerfil;
    TextView seguidosPerfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        actividadAnfitriona = (MainActivity)getActivity();

        tituloUsuarioPerfil = (TextView)vista.findViewById(R.id.tituloUsuarioPerfil);
        seguidoresPerfil = (TextView)vista.findViewById(R.id.seguidoresPerfil);
        seguidosPerfil = (TextView)vista.findViewById(R.id.seguidosPerfil);

        new ejecutarBusqueda().execute(actividadAnfitriona.Usuario.Token, String.valueOf(actividadAnfitriona.perfilViendo));



        return vista;
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
                    /*//Parseo el JSON
                    JSONObject jsonObject = new JSONObject(datos);
                    Usuario usuarioTemp;


                    int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                    String USUARIO = jsonObject.getString("USUARIO");
                    String TIENEIMAGEN = jsonObject.getString("TIENEIMAGEN");
                    int SEGUIDORES = jsonObject.getInt("SEGUIDORES");
                    int SEGUIDOS = jsonObject.getInt("SEGUIDOS");

                    usuarioTemp = new Usuario(IDUSUARIO, USUARIO, Boolean.valueOf(TIENEIMAGEN), SEGUIDORES, SEGUIDOS);

                    /*tituloUsuarioPerfil.setText(usuarioTemp.Usuario);
                    seguidosPerfil.setText(usuarioTemp.Seguidos);
                    seguidoresPerfil.setText(usuarioTemp.Seguidores);*/


                }
                catch (JSONException e)
                {
                    Toast miToast;
                    miToast = Toast.makeText(actividadAnfitriona, e.getMessage(), Toast.LENGTH_LONG);
                    miToast.show();
                }
            }
            Log.d("Estado", "Post");


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
