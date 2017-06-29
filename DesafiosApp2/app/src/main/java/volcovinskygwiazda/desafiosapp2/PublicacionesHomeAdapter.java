package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
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

public class PublicacionesHomeAdapter extends BaseAdapter {

    private Context mContext;
    private List<publicacion> publicacionesList;
    ImageView imagenItemDesafio;
    View v;
    Boolean imagenCargada;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        v = View.inflate(mContext, R.layout.layout_desafio, null);
        //Seteo de valores etc
        TextView displayNombreUsuario = (TextView)v.findViewById(R.id.displayNombreUsuario);
        displayNombreUsuario.setText(publicacionesList.get(position).getUsuario());


        v.setTag(publicacionesList.get(position).getId());

        /*imagenItemDesafio.setImageResource(R.drawable.defaultusericon);



        if(publicacionesList.get(position).getTieneImagen() == 1)
        {
            Glide.with(mContext)
                    .load("http://proyectoinfo.hol.es/imagenes/usuarios/"+publicacionesList.get(position).getIdUsuario()+".png")
                    .into(imagenItemDesafio);

        }*/


        return v;

    }












}
