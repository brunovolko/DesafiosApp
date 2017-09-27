package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 42302359 on 18/8/2017.
 */

public class listaComentariosAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comentario> comentariosList;
    View v;

    //Constructor


    public listaComentariosAdapter(Context mContext, List<Comentario> comentariosList) {
        this.mContext = mContext;
        this.comentariosList = comentariosList;
    }

    @Override
    public int getCount() {
        return comentariosList.size();
    }

    @Override
    public Object getItem(int position) {
        return comentariosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = View.inflate(mContext, R.layout.layout_comentario, null);
        TextView displayUsuario = (TextView)v.findViewById(R.id.displayUsuario);
        displayUsuario.setText(comentariosList.get(position).getUsuario());

        TextView displayComentario = (TextView)v.findViewById(R.id.displayComentario);
        displayComentario.setText(comentariosList.get(position).getComentario());

        v.setTag(comentariosList.get(position).getIdComentario());


        return v;

    }
}
