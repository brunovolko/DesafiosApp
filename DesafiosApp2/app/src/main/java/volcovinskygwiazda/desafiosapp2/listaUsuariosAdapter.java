package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
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

public class listaUsuariosAdapter extends BaseAdapter {

    private Context mContext;
    private List<Usuario> resultadosList;
    ImageView imagenUsuario;
    View v;
    Boolean imagenCargada;

    //Constructor


    public listaUsuariosAdapter(Context mContext, List<Usuario> resultadosList) {
        this.mContext = mContext;
        this.resultadosList = resultadosList;
    }

    @Override
    public int getCount() {
        return resultadosList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultadosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = View.inflate(mContext, R.layout.layout_busqueda_usuario, null);
        TextView usuarioUsuario = (TextView)v.findViewById(R.id.usuarioUsuario);
        imagenUsuario = (ImageView)v.findViewById(R.id.imagenUsuario);
        usuarioUsuario.setText(resultadosList.get(position).getUsuario());

        v.setTag(resultadosList.get(position).getIDUsuario());

        imagenUsuario.setImageResource(R.drawable.defaultusericon);



        if(resultadosList.get(position).getTieneImagen() == true)
        {
            Log.d("Estado2", "Trayendo " + "http://proyectoinfo.hol.es/imagenes/usuarios/"+resultadosList.get(position).getIDUsuario()+".png");
            Glide.with(mContext)
                    .load("http://proyectoinfo.hol.es/imagenes/usuarios/"+resultadosList.get(position).getIDUsuario()+".png")
                    .into(imagenUsuario);

        }


        return v;

    }












}
