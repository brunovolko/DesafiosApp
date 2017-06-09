package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Bruno on 8/6/2017.
 */

public class listaDesafiosAdapter extends BaseAdapter {

    private Context mContext;
    private List<desafio> desafiosList;

    //Constructor


    public listaDesafiosAdapter(Context mContext, List<desafio> desafiosList) {
        this.mContext = mContext;
        this.desafiosList = desafiosList;
    }

    @Override
    public int getCount() {
        return desafiosList.size();
    }

    @Override
    public Object getItem(int position) {
        return desafiosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.layout_desafio, null);
        TextView displayDesafio = (TextView)v.findViewById(R.id.desafioItemDesafio);
        TextView displayUsuario = (TextView)v.findViewById(R.id.usuarioItemDesafio);
        displayDesafio.setText(desafiosList.get(position).getDesafio());
        displayUsuario.setText(desafiosList.get(position).getUsuario());

        v.setTag(desafiosList.get(position).getId());


        return v;
    }
}
