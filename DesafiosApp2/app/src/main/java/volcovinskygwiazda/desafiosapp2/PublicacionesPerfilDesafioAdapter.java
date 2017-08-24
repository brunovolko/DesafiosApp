package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Bruno on 8/6/2017.
 */

public class PublicacionesPerfilDesafioAdapter extends BaseAdapter {

    private Context mContext;
    private List<publicacion> publicacionesList;
    ImageView imagenPerfilPublicacionUser;
    View v;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Errores", "aaaargfetrga");
        v = View.inflate(mContext, R.layout.layout_publicacion_home, null);
        //Seteo de valores etc

        Log.d("Errores", "aaaa");

        imagenPerfilPublicacionUser = (ImageView)v.findViewById(R.id.imagenPerfilPublicacionHome);

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

        imagenPerfilPublicacionUser.getLayoutParams().height = 105; // equivalente a los 35px
        imagenPerfilPublicacionUser.getLayoutParams().width = 105;




        return v;

    }












}
