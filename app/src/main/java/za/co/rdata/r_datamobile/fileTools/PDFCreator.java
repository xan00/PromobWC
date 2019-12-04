package za.co.rdata.r_datamobile.fileTools;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by James de Scande on 13/10/2017 at 10:14.
 */

public class PDFCreator {

    public void stringtopdf(String pdfname, ArrayList sourcedata, String[] pdfheaders, int[] pdfcolumnsizes) {

        File exportDir = new File(Environment.getExternalStorageDirectory()+"/filesync/Documents/", "");

        if (!exportDir.exists()) {
            boolean bool = exportDir.mkdir();
        }

        try {
            final File file = new File(exportDir, pdfname + ".pdf");
            //file.createNewFile();
            if (!file.exists()) {
                file.createNewFile();
            } else {
                FileActions fileActions = new FileActions();
                fileActions.deleteFile(exportDir.toString(),pdfname+".pdf");
                file.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(file);
            Paint paint = new Paint();
            paint.setTextSize(4);

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new
                    PdfDocument.PageInfo.Builder(842, 595, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            int y = 0;
            int columnincrementvalue = 0;
            int k = 30;
            int maxdatavalue;

            for (int l = 0; l < pdfheaders.length-1; l++) {

                int pdfheaderlength = pdfheaders[l].length();
                canvas.drawText(pdfheaders[l], columnincrementvalue, 20, paint);
                columnincrementvalue = columnincrementvalue + pdfcolumnsizes[l];

            }

            for (int i = 0; i < sourcedata.size() - 1; i++) {

                String joined = TextUtils.join(", ", sourcedata);
                ArrayList<String> list = new ArrayList<>(Arrays.asList(joined.split(" , ")));
                for (int q = 0; i < list.size() - 1; i++) {
                    canvas.drawText(list.get(q), columnincrementvalue, k, paint);
                }
                k=k+10;
            }

            document.finishPage(page);
            document.writeTo(fOut);
            document.close();
            //curPDF.close();

        } catch (IOException e) {
            Log.i("error", e.getLocalizedMessage());
        }
    }

    public void checklonglength(int intfromdata, int holdingcolumn) {
        if (intfromdata > holdingcolumn) {
            holdingcolumn = intfromdata;
        }
    }

}
