package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import volcovinskygwiazda.desafiosapp2.CameraView;
import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;

import static android.app.Activity.RESULT_OK;

public class CumplirDesafioFragment extends Fragment {


    View vista;
    MainActivity actividadAnfitriona;
    ImageButton btnTomarFoto;
    Camera mCamera;
    FrameLayout camera_view;
    File directory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_cumplir_desafio, container, false);

        actividadAnfitriona = (MainActivity)getActivity();
        btnTomarFoto = (ImageButton)vista.findViewById(R.id.btnTomarFoto);
        btnTomarFoto.setLongClickable(false);
        btnTomarFoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTomarFoto();
            }
        });

        ejecutarCamara();




        return vista;
    }

    void ejecutarCamara()
    {
        final String TAG = "CameraActivity";


        mCamera = null;
        CameraView mCameraView = null;


        try{
            mCamera = Camera.open();
        } catch (Exception e){
            Log.d("Estado", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null) {
            mCameraView = new CameraView(actividadAnfitriona, mCamera);
            camera_view = (FrameLayout)vista.findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//agrega la vista CameraView()
        }

        //boton para cerrar la aplicación.
        ImageButton imgClose = (ImageButton)vista.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cierra la actividad.
                actividadAnfitriona.cambiarFragment(R.id.fragmentContenedor, new PrincipalFragment());
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new DesafiosFragment());
            }
        });
    }

    void onClickTomarFoto()
    {
        directory = new File(Environment.getExternalStorageDirectory() + "/DefyhallPictures/");

        if (!directory.exists()) {
            directory.mkdir();
        }

        Camera.Parameters parameters = mCamera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        //Camera.Size size1 = sizes.get(0);
        for(int i=0;i<sizes.size();i++)
        {

            if(sizes.get(i).width > size.width)
                size = sizes.get(i);
        }



        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPictureSize(1280, 720);
        parameters.setPreviewSize(1280, 720);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setJpegQuality(100);
        parameters.setRotation(90);
        mCamera.setParameters(parameters);
        mCamera.takePicture(null,null,photoCallback);


    }



    Camera.PictureCallback photoCallback=new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            try {
                actividadAnfitriona.image = new File(directory, "desafioCumplido.jpg");
                FileOutputStream out = new FileOutputStream(actividadAnfitriona.image);
                out.write(data);
                out.flush();
                out.close();

                RelativeLayout vistaCamara = (RelativeLayout)vista.findViewById(R.id.vistaCamara);
                RelativeLayout vistaFoto = (RelativeLayout)vista.findViewById(R.id.vistaFoto);
                vistaCamara.setVisibility(View.INVISIBLE);
                vistaFoto.setVisibility(View.VISIBLE);
                ImageView imagenFoto = (ImageView)vista.findViewById(R.id.imagenFoto);
                BitmapDrawable d = new BitmapDrawable(getResources(), actividadAnfitriona.image.getAbsolutePath()); // path is ur resultant //image
                imagenFoto.setImageDrawable(d);

                /*Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imagenFoto.setImageBitmap(bitmap);*/

                imagenFoto.setRotation(90);
                //.delete()
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            Toast.makeText(actividadAnfitriona, "ok", Toast.LENGTH_SHORT).show();

        }
    };



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Comprovamos que la foto se a realizado
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory()+
                            "/Tutorialeshtml5/"+"foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            img.setImageBitmap(bMap);
            Toast.makeText(actividadAnfitriona, "vamoo", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(actividadAnfitriona, "ups", Toast.LENGTH_SHORT).show();
        }
    }*/


}
