package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import layout.DesafiosFragment;

/**
 * Created by Bruno on 8/6/2017.
 */

public class listaDesafiosAdapter extends BaseAdapter {

    private Context mContext;
    private List<desafio> desafiosList;
    ImageView imagenItemDesafio;
    View v;
    Boolean imagenCargada;

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
        v = View.inflate(mContext, R.layout.layout_desafio, null);
        TextView displayDesafio = (TextView)v.findViewById(R.id.desafioItemDesafio);
        TextView displayUsuario = (TextView)v.findViewById(R.id.usuarioItemDesafio);
        imagenItemDesafio = (ImageView)v.findViewById(R.id.imagenItemDesafio);
        displayDesafio.setText(desafiosList.get(position).getDesafio());
        displayUsuario.setText(desafiosList.get(position).getUsuario());

        v.setTag(desafiosList.get(position).getId());

        imagenItemDesafio.setImageResource(R.drawable.defaultusericon);



        if(desafiosList.get(position).getTieneImagen() == 1)
        {
            Glide.with(mContext)
                    .load("http://proyectoinfo.hol.es/imagenes/usuarios/"+desafiosList.get(position).getIdUsuario()+".png")
                    .into(imagenItemDesafio);

            /*//Tiene imagen
            Log.d("Estado", "Tiene imagen adapter en el desafio" + desafiosList.get(position).getId());

            imagenCargada = false;

            new DownloadImageTask().execute(String.valueOf(desafiosList.get(position).getIdUsuario()));*/

            //new DownloadImageTask(imagenItemDesafio).execute("http://proyectoinfo.hol.es/imagenes/usuarios/"+desafiosList.get(position).getIdUsuario()+".png");



        }


        return v;

    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {


                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
                Log.d("Estado", String.valueOf(mIcon.getHeight()));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }








}
