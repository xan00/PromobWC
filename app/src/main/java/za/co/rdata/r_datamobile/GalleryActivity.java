package za.co.rdata.r_datamobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import rapid.decoder.BitmapDecoder;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.locationTools.GetLocation;

/**
 * Created by James de Scande on 08/12/2017 at 10:33.
 */

public class GalleryActivity extends AppCompatActivity {

    public View itemView;
    int backcode = -1;
    static ArrayList<File> arrGallery;
    String mCurrentPhotoPath;
    String imageFileName;
    String idvalue;
    public Bitmap bmp;

    private File photoFile;
    protected ImageView textureView;
    private boolean picreq = false;
    private String lat;
    private String lng;

    View.OnClickListener acceptpictureclick = v -> {
        backcode = 0;
        onBackPressed();
    };

    private String strSQL = "";
    private String strDetail1Name;
    private String strDetail2name;
    private String imagetype;

    private static sqliteDBHelper sqliteDb;

    View.OnClickListener takepictureclick = v -> {

        Cursor camerausage = sqliteDb.getReadableDatabase().rawQuery("SELECT parm_value FROM pro_sys_parms WHERE parm='camera_usage'",null);
        camerausage.moveToFirst();
        String camerausagevalue;

        try {
        camerausagevalue = camerausage.getString(0);
        } catch (android.database.CursorIndexOutOfBoundsException e) {
            camerausagevalue = "0";
        }

        if (Build.VERSION.SDK_INT < 21 || camerausagevalue.equals("1")) {
            camerausage.close();
            //finish();
            TakePicture();
        } else {
           camerausage.close();
           TakePictureCamera2();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent iPhoto = getIntent();
        Bundle bSaved = iPhoto.getExtras();

        try {
            imagetype = bSaved.getString("PICTURE TYPE");
        } catch (Exception ignore) {}

        try {
            assert bSaved != null;
            picreq = bSaved.getBoolean("PIC REQUIRED");
        } catch (Exception ignore) {}

        strSQL = bSaved.getString("PIC TEXT SQL STRING");
        strDetail1Name = bSaved.getString("DETAIL1 TITLE");

        sqliteDb = sqliteDBHelper.getInstance(this.getApplicationContext());
        Cursor picturedata = sqliteDb.getReadableDatabase().rawQuery(strSQL,null);
        picturedata.moveToFirst();
        try {
            idvalue = String.valueOf(picturedata.getString(3));
        } catch (IllegalStateException e) {
            idvalue = bSaved.getString("PHOTO ID");
        }

        picturedata.close();

        try {
            strDetail2name = bSaved.getString("DETAIL2 TITLE");
        } catch (Exception ignore) {}

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1000);
            GoToGallery(idvalue);
        } else {
            GoToGallery(idvalue);
        }
    }

    private void checkcoordsforzero(double latin, double lngin) {

        GetLocation location = new GetLocation(this);

        if (latin==0) {
            lat=String.valueOf(location.getLatitude());
        }
        if (lngin==0) {
            lng=String.valueOf(location.getLongitude());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        //if (!MainActivity.NODE_ID.startsWith(String.valueOf(6))) {
            if (requestCode == 1011) {

                Cursor picturedata = sqliteDb.getReadableDatabase().rawQuery(strSQL, null);
                picturedata.moveToFirst();

                setLat(String.valueOf(picturedata.getDouble(1)));
                setLng(String.valueOf(picturedata.getDouble(2)));

                checkcoordsforzero(picturedata.getDouble(1),picturedata.getDouble(2));

                String user = MainActivity.USER;
                String detail1 = String.valueOf(picturedata.getString(0));

                /*
                String detail2 = null;

                try {
                    detail2 = String.valueOf(picturedata.getString(3));
                } catch (Exception ignore) {
                }
*/
                picturedata.close();
                String strPicturetext = "Lat: " + lat + "\nLong: " + lng + "\nUser: " + user + "\n" + strDetail1Name + idvalue;

                if (strDetail2name != null) {
                    strPicturetext = strPicturetext + "\n" + strDetail2name + detail1;
                }

                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
                //String strTempbarcode = idvalue;
                //String strPictureText = strDetail1Name+": det 1";

                //ExifUtil exifUtil = new ExifUtil();
                Cursor camerausage =sqliteDb.getReadableDatabase().rawQuery("SELECT parm_value FROM pro_sys_parms WHERE parm='camera_usage'",null);
                camerausage.moveToFirst();
                String camerausagevalue = camerausage.getString(0);

                try {
                if (!MainActivity.NODE_ID.startsWith(String.valueOf(6)) || camerausagevalue.equals("1")) {
                    bmp = drawMultilineTextToBitmap(getBaseContext(), ExifUtil.rotateBitmap(mCurrentPhotoPath, BitmapDecoder.from(mCurrentPhotoPath).decode()), strPicturetext + "\nDate: " + timeStamp, 50);
                } else {
                    bmp = drawMultilineTextToBitmap(getBaseContext(), BitmapDecoder.from(mCurrentPhotoPath).decode() ,strPicturetext+"\nDate: "+timeStamp, 50);
                }
                } catch (NullPointerException ignore) {}
                camerausage.close();

                FileOutputStream out;// = null;
                try {
                    out = new FileOutputStream(photoFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                } catch (FileNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
       // }

        try {
            GoToGallery(idvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void TakePictureCamera2() {
        finish();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                @SuppressWarnings("JavaReflectionMemberAccess") Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent captureintent = new Intent(GalleryActivity.this,CameraActivity.class);
        captureintent.putExtra("PHOTO ID",idvalue);
        captureintent.putExtra("PIC TEXT SQL STRING",strSQL);

        Cursor picturedata = sqliteDb.getReadableDatabase().rawQuery(strSQL,null);
        picturedata.moveToFirst();
        lat = String.valueOf(picturedata.getDouble(1));
        lng = String.valueOf(picturedata.getDouble(2));

        checkcoordsforzero(Double.parseDouble(getLat()),Double.parseDouble(getLng()));

        String user = MainActivity.USER;
        String detail1 = String.valueOf(picturedata.getString(0));
        String detail2 = null;

        try {
            detail2 = String.valueOf(picturedata.getString(3));
        } catch (Exception ignore) {}

        String picturetext = "Lat: "+lat+"\nLong: "+lng+"\nUser: "+user+"\n"+strDetail1Name+detail1;

        if (strDetail2name!=null & detail2!=null) {
            picturetext = picturetext+"\n"+strDetail2name+detail2;
        }

        picturedata.close();

        captureintent.putExtra("DETAIL1 TITLE",strDetail1Name);

        try {
            captureintent.putExtra("DETAIL2 TITLE",strDetail2name);
        } catch (Exception ignore) {}

        captureintent.putExtra("PICTURE TEXT",picturetext);
        startActivity(captureintent);
    }

    public void TakePicture() {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                @SuppressWarnings("JavaReflectionMemberAccess") Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            photoFile = null;
            try {
                photoFile = createImageFile(idvalue);
            } catch (IOException ex) {

                Log.i("MISSING FILE: ", "IOException");
            }

            if (photoFile != null) {
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                //File dir = new File(Environment.getExternalStorageDirectory().toString() + "/filesync/Images/");

                startActivityForResult(cameraIntent, 1011);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile(String barcode) throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/filesync/Images/");
        File dir2 = new File(Environment.getDataDirectory().toString() + "/filesync/Images/");
        int folderlength = 0;
        try {
            folderlength = dir.list().length + 1;
        } catch (NullPointerException e) {
            e.printStackTrace();
            folderlength = 0;
        }
        imageFileName =  imagetype + "_" + barcode + "_" + folderlength + "_" + timeStamp;
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                dir      // directory
        );
        mCurrentPhotoPath = image.getAbsolutePath();

                // Save a file: path for use with ACTION_VIEW intents

        Log.i("PHOTOPATH: ", mCurrentPhotoPath);
        return image;
    }

    public void GoToGallery(final String barcode) {

        backcode = 0;
        setContentView(R.layout.activity_picture_check);

        try {

            ConstraintLayout fltAccept = findViewById(R.id.fltAccept);
            fltAccept.setClickable(false);

            textureView = findViewById(R.id.imgAssetPic);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            arrGallery = null;
        } catch (NullPointerException ignore) {
        }

        //if (arrGallery == null) {
            //File dir = new File(Environment.getExternalStorageDirectory().toString() + "/filesync/Images/");

        File dir = null;
        try {
            dir = new File(Environment.getExternalStorageDirectory().toString() + "/filesync/Images/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        catch(Exception e){
            Log.w("creating file error", e.toString());
        }

        try {
            //noinspection ConstantConditions
            arrGallery = new ArrayList<>(Arrays.asList(dir.listFiles(pathname -> {
                Log.d("Picture Name", pathname.getName());
                return pathname.getName().toUpperCase().startsWith(imagetype+"_"+barcode+"_")
                        & pathname.getName().toLowerCase().endsWith(".jpg");

            })));
        }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        //}

        try {
            if (!arrGallery.get(0).getName().endsWith(".jpg")) {
                arrGallery.remove(0);
            }
        } catch (NullPointerException | IndexOutOfBoundsException ignore) {
        }

        try {
            if (arrGallery.get(0).getName().equals("Version")) {
                arrGallery.remove(0);
            }
        } catch (IndexOutOfBoundsException | NullPointerException ignore) {
        }

        try {
            if (!mCurrentPhotoPath.equals(arrGallery.get(arrGallery.size() - 1).getAbsolutePath())) {
                dir = new File(mCurrentPhotoPath);

                if (dir.getName().startsWith(barcode)) {
                    arrGallery.add(dir);
                }
                mCurrentPhotoPath = null;
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignore) {
        }

        try {
        if (arrGallery.size()>0) {
            try {
                mainpicdisplay mainpicdisplay = new mainpicdisplay();
                mainpicdisplay.execute();
            } catch (RuntimeException e) {
                Toast.makeText(getBaseContext(), "There Are No Images For This Gallery", Toast.LENGTH_SHORT).show();
            }
        }   else {
            Toast.makeText(getBaseContext(), "There Are No Images For This Gallery", Toast.LENGTH_SHORT).show();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (arrGallery.size()>0) {
        ArrayAdapter<File> adapter = new picdetails_ListAdapter();
        ListView lstGallery = findViewById(R.id.listGallery);
        piclistpopulate piclistpopulate = new piclistpopulate(adapter,lstGallery);
        piclistpopulate.execute();
            }   else {
                Toast.makeText(getBaseContext(), "There Are No Images For This Gallery", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton acceptpicture = findViewById(R.id.fltAcceptPicture);
        FloatingActionButton newpicture = findViewById(R.id.fltNewPicture);

        acceptpicture.setOnClickListener(acceptpictureclick);
        newpicture.setOnClickListener(takepictureclick);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("file_uri", photoFile);
        outState.putString("PHOTO ID",idvalue);
        outState.putBoolean("PIC REQUIRED",picreq);
        outState.putString("PIC TEXT SQL STRING",strSQL);
        outState.putString("DETAIL1 TITLE",strDetail1Name);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        photoFile = savedInstanceState.getParcelable("file_uri");
        idvalue = savedInstanceState.getString("PHOTO ID");
        try {
            picreq = savedInstanceState.getBoolean("PIC REQUIRED");
        } catch (Exception ignore) {}

        strSQL = savedInstanceState.getString("PIC TEXT SQL STRING");
        strDetail1Name = savedInstanceState.getString("DETAIL1 TITLE");
    }

    /*
    private void setCurrentImage(Bitmap bitmap, String src) {

        ExifUtil exifUtil = new ExifUtil();
        textureView.setImageBitmap(ExifUtil.rotateBitmap(src, bitmap));
        //textureView.setImageBitmap(bitmap);
        //BitmapDecoder.from(exifUtil.rotateBitmap(src, bitmap)).into(textureView);
    }
*/

    @Override
    public void onBackPressed() {
        if (picreq) {
            if (arrGallery.size()<1) {
                Toast.makeText(getBaseContext(), "Picture is required for this meter", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
                finish();
            }
        } else {
            super.onBackPressed();
            finish();
        }
    }

    public Bitmap drawMultilineTextToBitmap(Context gContext, Bitmap bitmap, String gText, int size) {

        // prepare canvas
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);

        // new antialiased Paint
        TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize((int) (size * scale));
        // text shadow
        paint.setShadowLayer(1f, 1f, 1f, Color.WHITE);

        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // get height of multiline text
        int textHeight = textLayout.getHeight();

        // get position of text's top left corner
        float x = 20; //(bitmap.getWidth() - textWidth);
        float y = (bitmap.getHeight() - textHeight)-20;

        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    protected class piclistpopulate extends AsyncTask<Void,String, Boolean> {

        ArrayAdapter<File> adapter;
        ListView lstGallery;

        piclistpopulate(ArrayAdapter<File> adap, ListView lst) {
            this.adapter = adap;
            this.lstGallery = lst;
            //adapter = new picdetails_ListAdapter();
            //ListView lstGallery = findViewById(R.id.listGallery);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                lstGallery.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class mainpicdisplay extends AsyncTask<Void, String, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) throws RuntimeException {
            //try {
                BitmapDecoder.from(arrGallery.get(arrGallery.size() - 1).getAbsolutePath()).into(textureView);
                TextView imagename = findViewById(R.id.txtPicName);
                imagename.setText(arrGallery.get(arrGallery.size() - 1).getName());

            //} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                //Toast.makeText(getBaseContext(), "There Are No Images For This Gallery", Toast.LENGTH_SHORT).show();
            //}
            return null;
        }
    }

    private class picdetails_ListAdapter extends ArrayAdapter<File> {

        picdetails_ListAdapter() {
            super(GalleryActivity.this, R.layout.content_pictures, arrGallery);
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            itemView = convertView;

            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.content_pictures, parent, false);

            final int pos = position;

            File mSaveBit = arrGallery.get(position);
            String filePath = mSaveBit.getPath();
            final ImageView contentpic = itemView.findViewById(R.id.content_imgview);

            /*
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                contentpic.setRotation(90);
            }
*/

            try {
                BitmapDecoder.from(filePath).scale(75, 75).into(contentpic);
            } catch (Exception e) {
                e.printStackTrace();
            }

            View.OnClickListener piclistener = view -> {
                //ImageView image = findViewById(R.id.imgAssetPic);
                TextView imagename = findViewById(R.id.txtPicName);
                imagename.setText(arrGallery.get(pos).getName());
                mCurrentPhotoPath = arrGallery.get(pos).getAbsolutePath();
                //Bitmap mImageBitmap = BitmapDecoder.from(mCurrentPhotoPath).decode();
                //rotateImage(mImageBitmap,mCurrentPhotoPath);
                BitmapDecoder.from(mCurrentPhotoPath).into(textureView);
                /*
                try {
                    setCurrentImage(BitmapDecoder.from(mCurrentPhotoPath).decode(), mCurrentPhotoPath);
                } catch (EOFException e) {
                    FileActions fileActions = new FileActions();
                    fileActions.deleteFile(mCurrentPhotoPath);
                }
                */
                //textureView.setImageBitmap(mImageBitmap);
                //imageOrientationValidator(mCurrentPhotoPath);
            };

            itemView.setClickable(true);
            itemView.setOnClickListener(piclistener);

            return itemView;
        }
    }

}
