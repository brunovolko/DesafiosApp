package layout;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ImageWriter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Exchanger;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.SignupFragment;
import volcovinskygwiazda.desafiosapp2.Usuario;
import volcovinskygwiazda.desafiosapp2.desafio;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_SHORT;



public class DesafiosFragment extends Fragment {

    private ListView listViewDesafios;
    private listaDesafiosAdapter adapterDesafios;
    private List<desafio> listaDesafios;
    private int cantDesafios;
    TextView btnNuevoDesafio;
    MainActivity actividadAnfitriona;
    AlertDialog alert;
    private TextView displayCantDesafios;
    View rootView;
    Uri imagenGaleriaUri;

    static final int REQUEST_IMAGE_CAPTURE = 1;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_desafios, container, false);

        btnNuevoDesafio = (TextView)rootView.findViewById(R.id.btnNuevoDesafio);
        btnNuevoDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevoDesafio();
            }
        });

        listViewDesafios = (ListView)rootView.findViewById(R.id.listViewDesafios);


        AlertDialog.Builder builder = new AlertDialog.Builder(actividadAnfitriona).setCancelable(false);
        builder.setMessage("Cargando desafios...");
        alert = builder.create();
        alert.show();

        new buscarDesafiosOnline().execute(actividadAnfitriona.Usuario.Token);

        displayCantDesafios = (TextView)rootView.findViewById(R.id.displayCantDesafios);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actividadAnfitriona = (MainActivity)getActivity();
    }

    private void nuevoDesafio()
    {
        actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new NuevoDesafioFragment());

    }



    // Definimos AsyncTask
    private class buscarDesafiosOnline extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String datos) {
            super.onPostExecute(datos);
            alert.hide();

            if(datos.equals("error"))
            {
                Toast miToast;
                miToast = Toast.makeText(actividadAnfitriona, "Comprueba tu conexión a Internet", LENGTH_SHORT);
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
                //ok
                try
                {
                    //Parseo el JSON
                    JSONArray jsonArray = new JSONArray(datos);
                    JSONObject jsonObject;
                    listaDesafios = new ArrayList<>();
                    desafio desafioTemp;
                    cantDesafios = jsonArray.length();
                    displayCantDesafios.setText(String.valueOf(cantDesafios) + " desafíos disponibles");
                    if(cantDesafios == 0)
                    {
                        TextView displayNoDesafios1 = (TextView)rootView.findViewById(R.id.displayNoDesafios1);
                        TextView displayNoDesafios2 = (TextView)rootView.findViewById(R.id.displayNoDesafios2);
                        displayNoDesafios1.setVisibility(View.VISIBLE);
                        displayNoDesafios2.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        for(int pos = 0; pos < cantDesafios; pos++)
                        {
                            jsonObject = new JSONObject(jsonArray.get(pos).toString());
                            int IDDESAFIO = Integer.valueOf(jsonObject.getString("IDDESAFIO"));
                            int IDUSUARIO = Integer.valueOf(jsonObject.getString("IDUSUARIO"));
                            String DESAFIO = jsonObject.getString("DESAFIO");
                            String USUARIO = jsonObject.getString("USUARIO");
                            int TIENEIMAGEN = jsonObject.getInt("TIENEIMAGEN");
                            desafioTemp = new desafio(IDDESAFIO, IDUSUARIO, DESAFIO, USUARIO, TIENEIMAGEN);
                            listaDesafios.add(desafioTemp);

                        }

                        adapterDesafios = new listaDesafiosAdapter(getActivity(), listaDesafios);
                        Log.d("Estado", "Listo para rockearla");
                        listViewDesafios.setAdapter(adapterDesafios);
                        Log.d("Estado", "Adapter seteado");

                        registerForContextMenu(listViewDesafios);

                        listViewDesafios.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                desafioClickeado((int)view.getTag());
                            }
                        });
                    }



                }
                catch (JSONException e)
                {
                    Toast miToast;
                    miToast = Toast.makeText(actividadAnfitriona, e.getMessage(), Toast.LENGTH_LONG);
                    miToast.show();
                }
            }


            //miToast.show();
        }


        @Override
        protected String doInBackground(String... parametros) {
            Log.d("Estado", "Entra al doInBackground");
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("token", parametros[0])
                    .build();


            Request request = new Request.Builder()
                    .url("http://proyectoinfo.hol.es/listardesafios.php")
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = new MenuInflater(actividadAnfitriona.getApplicationContext());
        inflater.inflate(R.menu.ctxmenudesafio, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo Info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.btnTomarFoto:
                Log.d("Estado", "foto" + actividadAnfitriona.desafioCumpliendo);
                if(tomarFoto() == true)
                {
                    Log.d("Estado", "Camara abierta");
                }
                else
                {
                    Toast.makeText(actividadAnfitriona, "Hubo un error al abrir la camara.", LENGTH_SHORT).show();
                    Log.d("Estado", "Camara no se pudo abrir");
                }
                break;
            case R.id.btnGaleria:
                Log.d("Estado", "galria" + actividadAnfitriona.desafioCumpliendo);
                if(abrirGaleria() == true)
                {
                    Log.d("Estado", "Galeria abierta");

                }
                else
                {
                    Toast.makeText(actividadAnfitriona, "Hubo un error al abrir la galeria.", LENGTH_SHORT).show();
                    Log.d("Estado", "Galeria abierta");
                }
                break;
        }

        return super.onContextItemSelected(item);
    }

    File fileImagen;

    boolean abrirGaleria()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

        /*fileImagen = new File(actividadAnfitriona.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/DefyhallPictures/");
        if (!fileImagen.exists()) {
            fileImagen.mkdir();
        }

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String date = df.format(Calendar.getInstance().getTime());

        fileImagen = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DefyhallPictures/", date + ".jpg");
        try {
            fileImagen.createNewFile();
        }
        catch (IOException e)
        {
            Log.d("Estado", e.getMessage());
            return false;
        }

        Uri outputFileUri = Uri.fromFile(fileImagen);


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 1);*/


        return true;
    }

    String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    boolean tomarFoto()
    {
        fileImagen = new File(actividadAnfitriona.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/DefyhallPictures/");
        if (!fileImagen.exists()) {
            fileImagen.mkdir();
        }

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String date = df.format(Calendar.getInstance().getTime());

        fileImagen = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DefyhallPictures/", date + ".jpg");
        try {
            fileImagen.createNewFile();
        }
        catch (IOException e)
        {
            Log.d("Estado", e.getMessage());
            return false;
        }

        Uri outputFileUri = Uri.fromFile(fileImagen);


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, 0);

        return true;

    }

    File comprimirImagen(File tempFile)
    {
        try
        {
            String filePath = tempFile.getPath();
            Bitmap bmp = BitmapFactory.decodeFile(filePath);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DefyhallPictures/temp.jpg");
            //OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile));
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, os);
            //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            os.close();

            return tempFile;
        }
        catch (IOException e)
        {
            Log.d("Estado", e.getMessage());
            Toast.makeText(actividadAnfitriona, "Ocurrio un error al procesar la imagen. Intentelo nuevamente.", LENGTH_SHORT).show();
            return null;
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            if(fileImagen.exists())
            {
                Log.d("Estado", "Imagen Guardada");

                File imagenComprimida = comprimirImagen(fileImagen);


                actividadAnfitriona.image = imagenComprimida;

                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new CumplirDesafioFragment());
            }
            else
            {
                Log.d("Estado", "La imagen no se guardo");
            }

        }
        else if(requestCode == 1 && resultCode == RESULT_OK)
        {
            Log.d("Estado", "Imagen seleccionada");
            imagenGaleriaUri = data.getData();






            try
            {

                File imagenOriginal = new File(getFilePath(getContext(), imagenGaleriaUri));
                File imagenComprimida = comprimirImagen(imagenOriginal);
                actividadAnfitriona.image = imagenComprimida;
            }
            catch (URISyntaxException e)
            {
                Log.d("Estado", e.getMessage());
            }

            actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new CumplirDesafioFragment());

        }
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    void desafioClickeado(int tag)
    {
        actividadAnfitriona.desafioCumpliendo = tag;

        boolean encontrado = false;
        int indice = 0;
        while(!encontrado && indice < listaDesafios.size())
        {
            if(listaDesafios.get(indice).getId() == tag)
            {
                encontrado = true;
            }
            else
            {
                indice++;
            }

        }
        if(!encontrado)
        {
            Toast.makeText(actividadAnfitriona, "Ocurrio un error grave, por favor reinicie la aplicacion.", LENGTH_SHORT).show();
            return;
        }

        actividadAnfitriona.textoDesafioCumpliendo = listaDesafios.get(indice).getDesafio();
        actividadAnfitriona.openContextMenu(rootView.findViewById(R.id.listViewDesafios));




    }


}
