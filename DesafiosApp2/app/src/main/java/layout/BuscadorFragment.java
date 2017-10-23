package layout;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.Usuario;
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;
import volcovinskygwiazda.desafiosapp2.listaUsuariosAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class BuscadorFragment extends Fragment {


    View vista;
    MainActivity actividadAnfitriona;
    ImageView btnRegresarAlHome;
    ListView listViewResultadosBusqueda;
    listaUsuariosAdapter adapterResultados;
    private List<Usuario> listaResultados;
    EditText txtBuscador;
    private int cantResultados;
    TextView displayEstado;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_buscador, container, false);
        actividadAnfitriona = (MainActivity)getActivity();
        btnRegresarAlHome = (ImageView)vista.findViewById(R.id.btnRegresarAlHome);
        btnRegresarAlHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cerrarTeclado();
                actividadAnfitriona.volverFrag();
            }
        });
        txtBuscador = (EditText)vista.findViewById(R.id.txtBuscador);
        if(txtBuscador.requestFocus()) {
            actividadAnfitriona.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        displayEstado = (TextView)vista.findViewById(R.id.displayEstado);
        listViewResultadosBusqueda = (ListView)vista.findViewById(R.id.listViewResultadosBusqueda);

        txtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return vista;
    }

    ejecutarBusqueda task;

    void buscar()
    {
        if(task != null)
        {
            task.cancel(true);
        }
        if(!txtBuscador.getText().toString().trim().isEmpty())
        {
            displayEstado.setText("Buscando...");
            displayEstado.setVisibility(View.VISIBLE);
            task = (ejecutarBusqueda) new ejecutarBusqueda().execute(actividadAnfitriona.Usuario.Token, txtBuscador.getText().toString());
        }
    }

    // Definimos AsyncTask
    private class ejecutarBusqueda extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);

            if(datos.equals("error"))
            {
                displayEstado.setText("Comprueba tu conexión a Internet");
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
                //ok
                try
                {
                    //Parseo el JSON
                    JSONArray jsonArray = new JSONArray(datos);
                    JSONObject jsonObject;
                    listaResultados = new ArrayList<>();
                    Usuario usuarioTemp;
                    cantResultados = jsonArray.length();
                    if(cantResultados == 0)
                    {
                        displayEstado.setText("Ningun resultado encontrado");
                    }
                    else
                    {
                        displayEstado.setVisibility(View.GONE);
                        for(int pos = 0; pos < cantResultados; pos++)
                        {
                            jsonObject = new JSONObject(jsonArray.get(pos).toString());
                            int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                            String USUARIO = jsonObject.getString("USUARIO");
                            String TIENEIMAGEN = jsonObject.getString("TIENEIMAGEN");
                            Boolean temp;
                            if(TIENEIMAGEN.equals("1"))
                            {
                                temp = true;
                            }
                            else
                            {
                                temp = false;
                            }
                            usuarioTemp = new Usuario(IDUSUARIO, USUARIO, temp);
                            listaResultados.add(usuarioTemp);

                        }

                        adapterResultados = new listaUsuariosAdapter(getActivity(), listaResultados);
                        Log.d("Estado", "Listo para rockearla");
                        listViewResultadosBusqueda.setAdapter(adapterResultados);
                        Log.d("Estado", "Adapter seteado");

                        registerForContextMenu(listViewResultadosBusqueda);

                        listViewResultadosBusqueda.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                actividadAnfitriona.perfilViendo = Integer.valueOf(view.getTag().toString());
                                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilFragment(), true);
                                actividadAnfitriona.ref = "Buscador";
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
            Log.d("Estado", "Post");


        }


        @Override
        protected String doInBackground(String... parametros) {
            Log.d("Estado", "Entra al doInBackground");
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .addFormDataPart("busqueda", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/resultadosBusqueda.php")
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
