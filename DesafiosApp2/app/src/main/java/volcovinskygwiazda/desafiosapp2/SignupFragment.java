package volcovinskygwiazda.desafiosapp2;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import layout.BienvenidaFragment;

public class SignupFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    // Definimos AsyncTask
    private class BuscarDatos extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String dato) {
            super.onPostExecute(dato);
        }

        @Override
        protected String doInBackground(String... parametros) {
            String urlParameters  = "param1=a&param2=b&param3=c";
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            String request        = "http://example.com/index.php";
            URL    url            = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }


            String url = parametros[0];
            return new String();
        }

    }


}
