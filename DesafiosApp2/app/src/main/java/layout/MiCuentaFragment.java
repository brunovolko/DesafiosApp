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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.Usuario;

import static android.widget.Toast.LENGTH_SHORT;

public class MiCuentaFragment extends Fragment {

    View vista;
    MainActivity actividadAnfitriona;
    TextView tituloUsuarioPerfil, seguidoresPerfil, seguidosPerfil, btnVerDesafiosCompletados, btnVerDesafiosCreados;
    ImageView imagenUsuarioPerfil, btnOpciones;
    FrameLayout framePublicacionesPerfil;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_mi_cuenta, container, false);

        actividadAnfitriona = (MainActivity)getActivity();

        btnOpciones = (ImageView)vista.findViewById(R.id.btnOpciones);
        tituloUsuarioPerfil = (TextView)vista.findViewById(R.id.tituloUsuarioPerfil);
        seguidoresPerfil = (TextView)vista.findViewById(R.id.seguidoresPerfil);
        seguidosPerfil = (TextView)vista.findViewById(R.id.seguidosPerfil);
        imagenUsuarioPerfil = (ImageView)vista.findViewById(R.id.imagenPerfilPublico);
        framePublicacionesPerfil = (FrameLayout) vista.findViewById(R.id.framePublicacionesPerfil);

        actividadAnfitriona.perfilViendo = actividadAnfitriona.Usuario.getIDUsuario();

        btnVerDesafiosCompletados = (TextView)vista.findViewById(R.id.btnVerDesafiosCompletados);
        btnVerDesafiosCompletados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDesafiosCompletados();
            }
        });

        btnVerDesafiosCreados = (TextView)vista.findViewById(R.id.btnVerDesafiosCreados);
        btnVerDesafiosCreados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDesafiosCreados();
            }
        });

        btnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new OpcionesFragment());
            }
        });

        new ejecutarBusqueda().execute(actividadAnfitriona.Usuario.Token);

        cargarDesafiosCompletados();


        return vista;
    }

    void cargarDesafiosCompletados()
    {

        btnVerDesafiosCompletados.setShadowLayer(2, 1, 1, Color.parseColor("#000000"));
        btnVerDesafiosCreados.setShadowLayer(0, 0, 0, Color.parseColor("#000000"));
        actividadAnfitriona.cambiarFragment(R.id.framePublicacionesPerfil, new DesafiosCompletadosFragment());

    }

    void cargarDesafiosCreados()
    {
        btnVerDesafiosCreados.setShadowLayer(2, 1, 1, Color.parseColor("#000000"));
        btnVerDesafiosCompletados.setShadowLayer(0, 0, 0, Color.parseColor("#000000"));
        actividadAnfitriona.cambiarFragment(R.id.framePublicacionesPerfil, new DesafiosCreadosFragment());
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
                try {
                    Log.d("MiCuentaa", datos);
                    //Parseo el JSON
                    JSONObject jsonObject = new JSONObject(datos);
                    Usuario usuarioTemp;


                    int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                    String USUARIO = jsonObject.getString("USUARIO");
                    String TIENEIMAGEN = jsonObject.getString("TIENEIMAGEN");
                    int SEGUIDORES = Integer.valueOf(jsonObject.getString("SEGUIDORES"));
                    int SEGUIDOS = Integer.valueOf(jsonObject.getString("SEGUIDOS"));

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
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/cargarMiPerfil.php")
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
