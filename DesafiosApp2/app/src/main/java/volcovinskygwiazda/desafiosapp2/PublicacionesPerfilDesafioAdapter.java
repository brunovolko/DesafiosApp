package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import layout.BienvenidaFragment;
import layout.ComentariosFragment;
import layout.PerfilFragment;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Bruno on 8/6/2017.
 */

public class PublicacionesPerfilDesafioAdapter extends BaseAdapter {

    private Context mContext;
    private List<publicacion> publicacionesList;
    ImageView imagenPerfilPublicacionUser;
    ImageView btnCalificarPositivo, btnCalificarNegativo;
    Resources res;
    int calificacionIntentando;
    View v;
    MainActivity actividadAnfitriona;
    //Boolean imagenCargada;

    //Constructor


    public PublicacionesPerfilDesafioAdapter(Context mContext, List<publicacion> desafiosList) {
        Log.d("Errores", "0");
        this.mContext = mContext;
        Log.d("Errores", "1");
        this.publicacionesList = desafiosList;
        Log.d("Errores", "2");
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
        Log.d("Errores", "aaaargfetrga");
        v = View.inflate(mContext, R.layout.layout_publicacion_home, null);
        //Seteo de valores etc

        actividadAnfitriona = (MainActivity)mContext;

        Log.d("Errores", "aaaa");

        imagenPerfilPublicacionUser = (ImageView)v.findViewById(R.id.imagenPerfilPublicacionHome);

        TextView displayVerComentarios = (TextView)v.findViewById(R.id.displayVerComentarios);

        ImageView btnAbrirComentarios = (ImageView)v.findViewById(R.id.btnAbrirComentarios);

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

        Log.d("Errores", "1");
        TextView displayNombreUsuario = (TextView)v.findViewById(R.id.displayNombreUsuario);
        Log.d("Errores", "2");
        displayNombreUsuario.setText(publicacionesList.get(position).getUsuario());
        Log.d("Errores", "3");

        TextView displayDesafioPublicacionHome = (TextView)v.findViewById(R.id.displayDesafioPublicacionHome);
        Log.d("Errores", "4");
        displayDesafioPublicacionHome.setText(publicacionesList.get(position).getDesafio());
        Log.d("Errores", "5");

        Drawable drawableImagen = ContextCompat.getDrawable(v.getContext(), R.drawable.imagenpublicaciontest1);
        Log.d("Errores", "6");
        Bitmap bitmap = ((BitmapDrawable)drawableImagen).getBitmap();
        Log.d("Errores", "7");

        int defaultHeight = bitmap.getHeight(); // px creo
        int defaultWidth = bitmap.getWidth(); // px creo

        float nuevoWidth = 0;
        float nuevoHeight = 0;

        Log.d("Errores", "8");


        Resources resources = v.getContext().getResources();
        Log.d("Errores", "9");
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Log.d("Errores", "10");

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

        Log.d("Errores", "11");
        ImageView imagenPublicacionHome = (ImageView)v.findViewById(R.id.imagenPublicacionHome);
        Log.d("Errores", "12");


        //imagenPublicacionHome.setImageResource(R.drawable.imagenpublicaciontest1);
        Glide.with(mContext)
                .load("http://proyectoinfo.hol.es/imagenes/publicaciones/"+publicacionesList.get(position).getId()+".jpg")
                .placeholder(R.drawable.cargando_imagen_publicacion)
                .into(imagenPublicacionHome);

        Log.d("Errores", "13");


        imagenPublicacionHome.getLayoutParams().height = (int)nuevoHeight;
        imagenPublicacionHome.getLayoutParams().width = (int)nuevoWidth;

        Log.d("Estado", "Height: " + defaultHeight + "  Width: " + defaultWidth);

        Log.d("Errores", "4");

        v.setTag(publicacionesList.get(position).getId());


        imagenPerfilPublicacionUser.setImageResource(R.drawable.defaultusericon);


        Log.d("Errores", "5");

        if(publicacionesList.get(position).getTieneImagen() == 1)
        {
            Glide.with(mContext)
                    .load("http://proyectoinfo.hol.es/imagenes/usuarios/"+publicacionesList.get(position).getIdUsuario()+".png")
                    .placeholder(R.drawable.defaultusericon)
                    .into(imagenPerfilPublicacionUser);


        }

        imagenPerfilPublicacionUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilFragment(), true);
                actividadAnfitriona.perfilViendo = publicacionesList.get(position).getIdUsuario();
                actividadAnfitriona.ref = "PerfilDesafio";
            }
        });

        displayNombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new PerfilFragment(), true);
                actividadAnfitriona.perfilViendo = publicacionesList.get(position).getIdUsuario();
                actividadAnfitriona.ref = "PerfilDesafio";
            }
        });

        imagenPerfilPublicacionUser.getLayoutParams().height = 105; // equivalente a los 35px
        imagenPerfilPublicacionUser.getLayoutParams().width = 105;





        res = actividadAnfitriona.getResources();

        ///////////// Calificar ///////////////

        btnCalificarPositivo = (ImageView)v.findViewById(R.id.btnCalificarPositivo);
        btnCalificarNegativo = (ImageView)v.findViewById(R.id.btnCalificarNegativo);




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



        /////////// Fin Calificar ////////////




        ///////////////Calificaciones barrita ///////////////////

        TextView cantPositivos, cantNegativos;
        cantPositivos = (TextView)v.findViewById(R.id.cantPositivos);
        cantNegativos = (TextView)v.findViewById(R.id.cantNegativos);

        View rectanguloPositivos, rectanguloNegativos;
        rectanguloNegativos = (View) v.findViewById(R.id.rectanguloNegativos);
        rectanguloPositivos = (View) v.findViewById(R.id.rectanguloPositivos);



        int totalCalificaciones = publicacionesList.get(position).getCalificacionesNegativas() + publicacionesList.get(position).getCalificacionesPositivas();
        Log.d("Calificaciones", "Total: " + totalCalificaciones);

        if(totalCalificaciones > 0)
        {
            float porcentajePositivas = (publicacionesList.get(position).getCalificacionesPositivas() * 100) / totalCalificaciones;
            float porcentajeNegativas = (publicacionesList.get(position).getCalificacionesNegativas() * 100) / totalCalificaciones;
            Log.d("Calificaciones", "Pos: " + porcentajePositivas + "    Neg: " + porcentajeNegativas);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rectanguloNegativos.getLayoutParams();
            //params.weight = 1.0f;
            params.weight = porcentajeNegativas/100;
            Log.d("Calificaciones", "Nuevo weight neg: " + porcentajeNegativas/100);
            rectanguloNegativos.setLayoutParams(params);

            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)
                    rectanguloPositivos.getLayoutParams();
            params2.weight = porcentajePositivas/100;
            Log.d("Calificaciones", "Nuevo weight pos: " + porcentajePositivas/100);
            rectanguloPositivos.setLayoutParams(params2);

            if(publicacionesList.get(position).getCalificacionesNegativas() == 0)
            {
                cantNegativos.setVisibility(View.INVISIBLE);
                cantPositivos.setText(String.valueOf(publicacionesList.get(position).getCalificacionesPositivas()));
            }
            else if(publicacionesList.get(position).getCalificacionesPositivas() == 0)
            {
                cantPositivos.setVisibility(View.INVISIBLE);
                cantNegativos.setText(String.valueOf(publicacionesList.get(position).getCalificacionesNegativas()));
            }
            else
            {
                cantNegativos.setText(String.valueOf(publicacionesList.get(position).getCalificacionesNegativas()));
                cantPositivos.setText(String.valueOf(publicacionesList.get(position).getCalificacionesPositivas()));
            }


        }
        else
        {
            //Borramos los views
            rectanguloNegativos.setVisibility(View.INVISIBLE);
            rectanguloPositivos.setVisibility(View.INVISIBLE);
            cantNegativos.setVisibility(View.INVISIBLE);
            cantPositivos.setVisibility(View.INVISIBLE);

        }

        ////////////// Fin CAlificaciones Barrita ///////////////





        return v;

    }

    void abrirComentarios(int idPublicacion)
    {
        actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new ComentariosFragment(), true);
        actividadAnfitriona.comentariosViendo = idPublicacion;
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
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new BienvenidaFragment(), false);
                actividadAnfitriona.vaciarStackFragments();
            }
            else
            {

                Log.d("Adapter", "Notify");
                actividadAnfitriona.adapterPublicaciones.notifyDataSetChanged();
                //Perfectoo
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
                }
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
