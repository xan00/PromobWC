package za.co.rdata.r_datamobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rapid.decoder.BitmapDecoder;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;

import static za.co.rdata.r_datamobile.fileTools.FileActions.createFolder;

/**
 * Created by James de Scande on 13/09/2017 at 10:33.
 */

public class CameraActivity extends Activity {

    private static final String TAG = "Camera2";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    int backcode = -1;
    String mCurrentPhotoPath;
    String imageFileName;
    String idvalue;
    CameraCharacteristics characteristics;
    StreamConfigurationMap map;
    private TextureView textureView;
    private String cameraId;
    private Size imageDimension;
    Handler hanImagecheck = new Handler();
    Runnable runImagecheck;



    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera(width, height);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private ImageReader imageReader;
    private File file = null;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    @SuppressLint("NewAPI")
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    private HandlerThread mBackgroundThread;
    private boolean picreq = false;
    private String strPictureText = "";
    private String sql;
    private String strDetail1Name;
    private String strDetail2name;
    private String picturetype;
    private String strDetail1Value;

    private String strCurrentRoom;
    private String strSelectedRoom;
    private int handlerruntime;

    ArrayAdapter<File> adapter; //= new picdetails_ListAdapter();
    static ArrayList<File> arrGallery;

    private ArrayList<Toast> toasts;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent iPhoto = getIntent();
        Bundle bSaved = iPhoto.getExtras();
        idvalue = bSaved.getString("PHOTO ID");
        strCurrentRoom = bSaved.getString(intentcodes.asset_activity.current_room);
        strSelectedRoom =  bSaved.getString("ROOM SCAN");
        strDetail1Value =  bSaved.getString(intentcodes.asset_activity.asset_barcode);
        sql = bSaved.getString("PIC TEXT SQL STRING");
        try {
            strPictureText = bSaved.getString("PICTURE TEXT");
        } catch (NullPointerException ignore) {
        }

        strDetail1Name = bSaved.getString("DETAIL1 TITLE");

        try {
            strDetail2name = bSaved.getString("DETAIL2 TITLE");
        } catch (Exception ignore) {
        }

        try {
            picturetype = bSaved.getString("PICTURE TYPE");
        } catch (Exception ignore) {
        }

        setContentView(R.layout.activity_image_capture);
        textureView = findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        FloatingActionButton takePictureButton = findViewById(R.id.fltNewPictureFromActivity);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        try {
            arrGallery = null;
        } catch (NullPointerException ignore) {
        }

        File dir = createFolder(this.getBaseContext(), "/filesync/Images/");

        try {
            //noinspection ConstantConditions
            arrGallery = new ArrayList<>(Arrays.asList(dir.listFiles(pathname -> {
                Log.d("Picture Name", pathname.getName());
                return pathname.getName().toUpperCase().startsWith(picturetype+"_"+idvalue+"_")
                        & pathname.getName().toLowerCase().endsWith(".jpg");

            })));


            for (File d: arrGallery
            ) {
                if (d.getTotalSpace()<10) d.delete();
            }
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

        adapter = new picdetails_ListAdapter();

        try {
            if (arrGallery.size()>0) {

                ListView lstGallery = findViewById(R.id.listGallery);
                lstGallery.bringToFront();
                lstGallery.setAdapter(adapter);

            }   else {
                Toast.makeText(getBaseContext(), "There Are No Images For This Gallery", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        runImagecheck = () -> {
            adapter.notifyDataSetChanged();

        };
        //hanImagecheck.post(runImagecheck);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openCamera(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            characteristics = manager.getCameraCharacteristics(cameraId);
            map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            configureTransform(width, height);
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = this;
        int width = 0;
        int height = 0;

        Point displaySize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
        int rotatedPreviewWidth = width;
        int rotatedPreviewHeight = height;
        int maxPreviewWidth = displaySize.x;
        int maxPreviewHeight = displaySize.y;
        int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Size largest = Collections.max(
                Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                new CompareSizesByArea());
        int mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        boolean swappedDimensions = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                    swappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                    swappedDimensions = true;
                }
                break;
            default:
                Log.e(TAG, "Display rotation is invalid: " + displayRotation);
        }

        if (swappedDimensions) {
            rotatedPreviewWidth = height;
            rotatedPreviewHeight = width;
            maxPreviewWidth = displaySize.y;
            maxPreviewHeight = displaySize.x;
        }

        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
            maxPreviewWidth = MAX_PREVIEW_WIDTH;
        }

        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
            maxPreviewHeight = MAX_PREVIEW_HEIGHT;
        }

        Size mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                maxPreviewHeight, largest);
        if (null == textureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        //bitmaptemp = rotateImage(BitmapDecoder.from(photoFile.getAbsolutePath()).decode(),90);
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // Reverse device orientation for front-facing cameras
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

        return jpegOrientation;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void takePicture() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }

            Point size = new Point();
            int width = 1280;
            int height = 720;

            try {
                this.getWindowManager().getDefaultDisplay().getRealSize(size);
                width = size.x;
                height = size.y;
            } catch (NoSuchMethodError e) {
                Log.i("error", "it can't work");
            }

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH);
            //captureBuilder.set(CaptureRequest.FLASH_MODE,1);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            //captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(characteristics, rotation));
            //final File file = new File(Environment.getExternalStorageDirectory()+"filesync/Images/"+idvalue+".jpg");
            String timeStamp = new SimpleDateFormat("ddMMyy_HHmmss", Locale.getDefault()).format(new Date());

            try {
                file = createImageFile(idvalue,timeStamp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                        sendToast("Saved:" + file, CameraActivity.this);
                    } catch (NullPointerException ignore) {
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                    String strTempbarcode = null;

                    if (strDetail2name.equals("BARCODE: ")) {
                        if (!strPictureText.startsWith("BARCODE: ")) {
                            strPictureText = strDetail2name + idvalue + '\n' + strPictureText;
                        }
                    }


                    //ExifUtil exifUtil = new ExifUtil();
                    Bitmap bmp = BitmapDecoder.from(file.getAbsolutePath()).decode();
                    try {
                        //Bitmap tempbit = BitmapDecoder.from(file.getAbsolutePath()).decode();
                        bmp = drawMultilineTextToBitmap(getBaseContext(), ExifUtil.rotateBitmap(file.getAbsolutePath(), BitmapDecoder.from(file.getAbsolutePath()).decode()), strPictureText + "\nDate: " + timeStamp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        arrGallery.add(file);

                        //refreshpiclist refreshpiclist = new refreshpiclist();
                        //refreshpiclist.execute();

                    }
                }
            };

            //ListView lstGallery = findViewById(R.id.listGallery);
           reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    //Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();


                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException | IllegalStateException e) {
            e.printStackTrace();
        }

        hanImagecheck.postDelayed(runImagecheck, 500);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //setContentView(R.layout.add_entry);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeToast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeToast();
    }

    public void removeToast() {
        runOnUiThread(() -> {
            if (null != toasts) {
                for (Toast toast : toasts) {
                    toast.cancel();
                }
                toasts = null;
            }
        });
    }

    public void sendToast(final String message, Context toastContext) {
        if (null == toasts) {
            toasts = new ArrayList<>();
        }
        runOnUiThread(() -> {
            try {
                Toast toast = Toast.makeText(toastContext, message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 300);
                toast.show();
                toasts.add(toast);
            } catch (Exception e) {/* do nothing, just means that the activity doesn't exist anymore*/}
        });
    }

    public Bitmap drawMultilineTextToBitmap(Context gContext, Bitmap bitmap, String gText) {

        // prepare canvas
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);

        // new antialiased Paint
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize((int) (10 * scale));
        // text shadow
        paint.setShadowLayer(2f, 2f, 2f, Color.WHITE);

        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // get height of multiline text
        int textHeight = textLayout.getHeight();

        // get position of text's top left corner
        float x = 20; //(bitmap.getWidth() - textWidth);
        float y = (bitmap.getHeight() - textHeight) - 20;

        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    private File createImageFile(String barcode, String timeStamp) throws IOException {
        // Create an image file name


        File dir = createFolder(this.getBaseContext(), "/filesync/Images/");
        int folderlength = 0;
        try {
            folderlength = dir.list().length + 1;
        } catch (NullPointerException e) {
            e.printStackTrace();
            folderlength = 0;
        }
        imageFileName = picturetype + "_" + barcode + "_" + folderlength + "_" + timeStamp;
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                dir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intentsmr
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("PHOTOPATH: ", mCurrentPhotoPath);
        return image;
    }

    @Override
    public void onBackPressed() {
        Intent gotogallery = new Intent(CameraActivity.this, GalleryActivity.class);
        gotogallery.putExtra("PHOTO ID", idvalue);
        gotogallery.putExtra("PIC TEXT SQL STRING", sql);
        gotogallery.putExtra("DETAIL1 TITLE", strDetail1Name);
        gotogallery.putExtra("PICTURE TYPE", picturetype);
        try {
            gotogallery.putExtra("DETAIL2 TITLE", strDetail2name);
        } catch (Exception ignore) {
        }
        startActivity(gotogallery);
        finish();
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public View itemView;

    private class picdetails_ListAdapter extends ArrayAdapter<File> {

        picdetails_ListAdapter() {
            super(CameraActivity.this, R.layout.content_pictures, arrGallery);
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            itemView = convertView;

            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.content_pictures, parent, false);

            final int pos = position;

            File mSaveBit = arrGallery.get(position);
            String filePath = mSaveBit.getAbsolutePath();
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

            //itemView.setClickable(true);
            //itemView.setOnClickListener(piclistener);

            return itemView;
        }
    }

}