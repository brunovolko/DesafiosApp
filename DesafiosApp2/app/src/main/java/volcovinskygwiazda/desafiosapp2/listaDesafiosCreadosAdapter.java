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

public class listaDesafiosCreadosAdapter extends BaseAdapter {
    private Context mContext;
    private List<desafio> desafiosList;
    View v;

    //Constructor


    public listaDesafiosCreadosAdapter(Context mContext, List<desafio> desafiosList) {
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
        v = View.inflate(mContext, R.layout.layout_desafio_creado, null);
        TextView displayDesafio = (TextView)v.findViewById(R.id.desafioItemDesafio);
        displayDesafio.setText(desafiosList.get(position).getDesafio());

        v.setTag(desafiosList.get(position).getId());


        return v;

    }
}
