package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import volcovinskygwiazda.desafiosapp2.CameraView;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;

import static android.app.Activity.RESULT_OK;

public class CumplirDesafioFragment extends Fragment {


    View vista;
    MainActivity actividadAnfitriona;
    TextView textoDesafio;
    ImageView imagenTomada;
    ImageView btnVolverADesafios;
    ImageView btnEnviarPublicacion;
    AlertDialog alert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_cumplir_desafio, container, false);

        actividadAnfitriona = (MainActivity)getActivity();

        textoDesafio = (TextView)vista.findViewById(R.id.textoDesafio);
        textoDesafio.setText(actividadAnfitriona.textoDesafioCumpliendo);
        imagenTomada = (ImageView)vista.findViewById(R.id.imagenTomada);
        Bitmap myBitmap = BitmapFactory.decodeFile(actividadAnfitriona.image.getAbsolutePath());
        imagenTomada.setImageBitmap(myBitmap);
        btnVolverADesafios = (ImageView)vista.findViewById(R.id.btnVolverADesafios);
        btnVolverADesafios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new DesafiosFragment());
            }
        });
        btnEnviarPublicacion = (ImageView)vista.findViewById(R.id.btnEnviarPublicacion);
        btnEnviarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPublicacion();
            }
        });





        return vista;
    }

    void enviarPublicacion()
    {
        actividadAnfitriona.cerrarTeclado();
        AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
        builder.setMessage("Publicando");
        alert = builder.create();
        alert.show();
        new publicarDesafio().execute(actividadAnfitriona.Usuario.Token, String.valueOf(actividadAnfitriona.desafioCumpliendo));
    }

    // Definimos AsyncTask
    private class publicarDesafio extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", Toast.LENGTH_SHORT).show();
            }
            else if(datos.equals("error2"))
            {
                // El token está mal, asi que a borrarloo y que vuelva al inicio
                Toast.makeText(actividadAnfitriona, "Tu sesión expiró, vuelve a iniciar sesion.", Toast.LENGTH_SHORT).show();
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new BienvenidaFragment());
            }
            else
            {
                //ok
                Toast.makeText(actividadAnfitriona, "Tu desafio fué creado con éxito!", Toast.LENGTH_SHORT).show();
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new HomeFragment());
            }


        }

        @Override
        protected String doInBackground(String... parametros) {

            OkHttpClient client = new OkHttpClient();

            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .addFormDataPart("desafio", parametros[1])
                    //.addFormDataPart("imagen", actividadAnfitriona.image)
                    .addFormDataPart("imagen", "profile.png", RequestBody.create(MEDIA_TYPE_JPG, actividadAnfitriona.image))
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/cumplirDesafio.php")
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
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
