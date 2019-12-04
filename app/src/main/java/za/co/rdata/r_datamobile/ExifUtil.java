package za.co.rdata.r_datamobile;
/*
  Created by James de Scande on 13/12/2017 at 09:25.
 */

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import za.co.rdata.r_datamobile.fileTools.FileActions;

class ExifUtil {

    static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src, bitmap);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            FileActions fileActions = new FileActions();
            fileActions.deleteFile(src);
        }

        return bitmap;
    }

    private static int getExifOrientation(String src, Bitmap bitmap) {
        int orientation = 1;

        try {
            Class<?> exifClass = Class.forName("android.media.ExifInterface");
            Constructor<?> exifConstructor;
            Object exifInstance = null;
            try {
            exifConstructor = exifClass.getConstructor(String.class);
            exifInstance = exifConstructor.newInstance(src);
            } catch (Exception e) {
                FileActions fileActions = new FileActions();
                fileActions.deleteFile(src);
            }
            Method getAttributeInt = exifClass.getMethod("getAttributeInt", String.class, int.class);
            Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
            String tagOrientation = (String) tagOrientationField.get(null);
            orientation = (Integer) getAttributeInt.invoke(exifInstance, tagOrientation, 1);

        } catch (ClassNotFoundException | SecurityException | IllegalArgumentException | NoSuchMethodException  | InvocationTargetException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            FileActions fileActions = new FileActions();
            fileActions.deleteFile(src);
        }

        return orientation;
    }
}
