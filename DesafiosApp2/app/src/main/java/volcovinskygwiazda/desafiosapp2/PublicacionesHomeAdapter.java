package volcovinskygwiazda.desafiosapp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import layout.BienvenidaFragment;
import layout.ComentariosFragment;
import layout.HomeFragment;
import layout.PerfilDesafioFragment;
import layout.PerfilFragment;
import layout.PrincipalFragment;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;

/**
 * Created by Bruno on 8/6/2017.
 */

public class PublicacionesHomeAdapter extends BaseAdapter {

    private Context mContext;
    private List<publicacion> publicacionesList;
    ImageView imagenPerfilPublicacionHome;
    ImageView btnAbrirComentarios;
    Resources res;
    View v;
    int publicacionActual;
    MainActivity actividadAnfitriona;

    int calificacionIntentando;

    ImageView btnCalificarPositivo, btnCalificarNegativo;

    //Boolean imagenCargada;

    //Constructor


    public PublicacionesHomeAdapter(Context mContext, List<publicacion> desafiosList) {
        this.mContext = mContext;
        this.publicacionesList = desafiosList;
    }

    @Override
    public int getCount() {
        return publicacionesList.size();
    }

    @Override
    public Object getItem(int position) {
        return publicacionesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = View.inflate(mContext, R.layout.layout_publicacion_home, null);
        //Seteo de valores etc

        publicacionActual = publicacionesList.get(position).getId();

        actividadAnfitriona = (MainActivity)mContext;

        btnAbrirComentarios = (ImageView) v.findViewById(R.id.btnAbrirComentarios);

        imagenPerfilPublicacionHome = (ImageView)v.findViewById(R.id.imagenPerfilPublicacionHome);

        TextView displayVerComentarios = (TextView)v.findViewById(R.id.displayVerComentarios);

        btnCalificarPositivo = (ImageView)v.findViewById(R.id.btnCalificarPositivo);
        btnCalificarNegativo = (ImageView)v.findViewById(R.id.btnCalificarNegativo);

        res = actividadAnfitriona.getResources();

        switch (publicacionesList.get(position).getMiCalificacion())
        {
            case -1:
                btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage));
                btnCalificarNegativo.setImageDrawable(res.getDrawable(R.drawable.negativoimage));
                break;
            case 0:
                btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage));
                btnCalificarNegativo.setImageDrawable(res.getDrawable(R.drawable.negativoimage_activo));
                break;
            case 1:
                btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage_activo));
                btnCalificarNegativo.setImageDrawable(res.getDrawable(R.drawable.negativoimage));
                break;
        }

        btnCalificarPositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCalificarPositivo.setEnabled(false);
                btnCalificarNegativo.setEnabled(false);
                calificacionIntentando = 1;
                new calificarTask().execute("http://proyectoinfo.hol.es/calificacionPositiva.php", String.valueOf(publicacionesList.get(position).getId()));

                if(publicacionesList.get(position).getMiCalificacion() == 1)
                {
                    Log.d("Calificacion", "Sacando positivo");
                    btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage));
                    publicacionesList.get(position).actualizarCalificacion(-1);
                }
                else if(publicacionesList.get(position).getMiCalificacion() == 0)
                {
                    Log.d("Calificacion", "Sacando negativo y poniendo positivo");
                    btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage_activo));
                    btnCalificarNegativo.setImageDrawable(res.getDrawable(R.drawable.negativoimage));
                    publicacionesList.get(position).actualizarCalificacion(1);
                }
                else
                {
                    Log.d("Calificacion", "poniendo positivo");
                    btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage_activo));
                    publicacionesList.get(position).actualizarCalificacion(1);
                }


            }
        });

        btnCalificarNegativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCalificarPositivo.setEnabled(false);
                btnCalificarNegativo.setEnabled(false);
                calificacionIntentando = 0;
                new calificarTask().execute("http://proyectoinfo.hol.es/calificacionNegativa.php", String.valueOf(publicacionesList.get(position).getId()));

                if(publicacionesList.get(position).getMiCalificacion() == 1)
                {
                    Log.d("Calificacion", "Sacando positivo y poniendo negativo");
                    btnCalificarPositivo.setImageResource(R.drawable.positivoimage);
                    btnCalificarNegativo.setImageResource(R.drawable.negativoimage_activo);
                    publicacionesList.get(position).actualizarCalificacion(0);
                }
                else if(publicacionesList.get(position).getMiCalificacion() == 0)
                {
                    Log.d("Calificacion", "Sacando negativo ");
                    btnCalificarNegativo.setImageResource(R.drawable.negativoimage);
                    publicacionesList.get(position).actualizarCalificacion(-1);
                }
                else
                {
                    Log.d("Calificacion", "poniendo negativo");
                    btnCalificarNegativo.setImageResource(R.drawable.negativoimage_activo);
                    publicacionesList.get(position).actualizarCalificacion(0);
                }

            }
        });



        if(publicacionesList.get(position).getCantidadComentarios() == 0)
        {
            displayVerComentarios.setVisibility(View.GONE);
        }
        else
        {
            displayVerComentarios.setText("Ver " + publicacionesList.get(position).getCantidadComentarios() + " comentarios");
            displayVerComentarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirComentarios(publicacionesList.get(position).getId());
                }
            });
        }

        btnAbrirComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirComentarios(publicacionesList.get(position).getId());
            }
        });

        TextView displayNombreUsuario = (TextView)v.findViewById(R.id.displayNombreUsuario);
        displayNombreUsuario.setText(publicacionesList.get(position).getUsuario());

        displayNombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ir al perfil de este usuario
                //La imagen debe hacer lo mismo
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilFragment());
                actividadAnfitriona.perfilViendo = publicacionesList.get(position).getIdUsuario();
                actividadAnfitriona.ref = "Home";

            }
        });


        TextView displayDesafioPublicacionHome = (TextView)v.findViewById(R.id.displayDesafioPublicacionHome);
        displayDesafioPublicacionHome.setText(publicacionesList.get(position).getDesafio());

        /*displayDesafioPublicacionHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        actividadAnfitriona.ref = "perfil";
                        abrirPerfilDesafio(publicacionesList.get(position).getIdDesafio);
            }
        });*/

        Drawable drawableImagen = ContextCompat.getDrawable(v.getContext(), R.drawable.imagenpublicaciontest1);
        Bitmap bitmap = ((BitmapDrawable)drawableImagen).getBitmap();

        int defaultHeight = bitmap.getHeight(); // px creo
        int defaultWidth = bitmap.getWidth(); // px creo

        float nuevoWidth = 0;
        float nuevoHeight = 0;


        Resources resources = v.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        float anchoCelular = metrics.widthPixels;
        float altoDeseado = anchoCelular * (float)1.1;

        float multiplicador;

        if(defaultHeight > defaultWidth)
        {
            Log.d("Estado", "Es mas alto que ancho");
            // Alto fijo, se ajusta el ancho
            // defaultAlto * X = altoDeseado

            multiplicador = altoDeseado / defaultHeight;

        } else if(defaultWidth > defaultHeight)
        {
            Log.d("Estado", "Es mas ancho que largo");
            //Ancho fijo, se ajusta el alto

            // defaultAncho * X = anchoCelular
            multiplicador = anchoCelular / defaultWidth;

            nuevoWidth = defaultWidth * multiplicador;
            nuevoHeight = altoDeseado;

        } else
        {

        }


        ImageView imagenPublicacionHome = (ImageView)v.findViewById(R.id.imagenPublicacionHome);


        //imagenPublicacionHome.setImageResource(R.drawable.imagenpublicaciontest1);
        Glide.with(mContext)
                .load("http://proyectoinfo.hol.es/imagenes/publicaciones/"+publicacionesList.get(position).getId()+".jpg")
                .placeholder(R.drawable.cargando_imagen_publicacion)
                .into(imagenPublicacionHome);


        imagenPublicacionHome.getLayoutParams().height = (int)nuevoHeight;
        imagenPublicacionHome.getLayoutParams().width = (int)nuevoWidth;

        Log.d("Estado", "Height: " + defaultHeight + "  Width: " + defaultWidth);



        v.setTag(publicacionesList.get(position).getId());


        imagenPerfilPublicacionHome.setImageResource(R.drawable.defaultusericon);




        if(publicacionesList.get(position).getTieneImagen() == 1)
        {
            Glide.with(mContext)
                    .load("http://proyectoinfo.hol.es/imagenes/usuarios/"+publicacionesList.get(position).getIdUsuario()+".png")
                    .placeholder(R.drawable.defaultusericon)
                    .into(imagenPerfilPublicacionHome);

        }

        imagenPerfilPublicacionHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ir al perfil de este usuario
                //La imagen debe hacer lo mismo
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilFragment());
                actividadAnfitriona.perfilViendo = publicacionesList.get(position).getIdUsuario();

            }
        });

        imagenPerfilPublicacionHome.getLayoutParams().height = 105; // equivalente a los 35px
        imagenPerfilPublicacionHome.getLayoutParams().width = 105;




        return v;

    }

    void abrirComentarios(int idPublicacion)
    {
        actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new ComentariosFragment());
        actividadAnfitriona.comentariosViendo = idPublicacion;
    }

    private void abrirPerfilDesafio(int idDesafio)
    {
        actividadAnfitriona.perfilDesafioViendo = idDesafio;
        actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilDesafioFragment());
    }

    // Definimos AsyncTask
    private class calificarTask extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);

            btnCalificarPositivo.setEnabled(true);
            btnCalificarNegativo.setEnabled(true);

            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", Toast.LENGTH_LONG);
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

                Log.d("Adapter", "Notify");
                //actividadAnfitriona.adapterPublicaciones.notifyDataSetChanged();
                /*//Perfectoo
                if(calificacionIntentando == 1)
                {
                    btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage_activo));
                    btnCalificarNegativo.setImageDrawable(res.getDrawable(R.drawable.negativoimage));
                }
                else
                {
                    Log.d("Calificacion", "Negativando");
                    btnCalificarPositivo.setImageDrawable(res.getDrawable(R.drawable.positivoimage));
                    btnCalificarNegativo.setImageDrawable(res.getDrawable(R.drawable.negativoimage_activo));
                }*/
            }

            calificacionIntentando = -1;


            //miToast.show();
        }

        @Override
        protected String doInBackground(String... parametros) {
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", actividadAnfitriona.Usuario.Token)
                    .addFormDataPart("idPublicacion", parametros[1])
                    .build();


            Request request = new Request.Builder()
                    .url(parametros[0])
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
