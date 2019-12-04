package za.co.rdata.r_datamobile.fileTools;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by James de Scande on 17/11/2017 at 15:12.
 */

public class EmailCreator extends AppCompatActivity {

    public String MakeDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss aa", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void SendEmailforStock(String strBinCode, int intReorder, int intMax, int intCurrentqty) {

        String strCurrencyOrderMessage = null;

        if (intCurrentqty < intReorder) {
            strCurrencyOrderMessage = "Stock is currently under the minimum required holding and must be reordered.";
        } else if (intReorder < intCurrentqty & intCurrentqty < intMax) {
            strCurrencyOrderMessage = "Stock is currently at the appropriate level and no action is required.";
        } else if (intCurrentqty > intMax) {
            strCurrencyOrderMessage = "Stock has been over supplied, recommended action is to recount and report to Supply Chain if the stock if the number is confirmed to be over.";
        }

            Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{});
            i.putExtra(Intent.EXTRA_SUBJECT, "Stock Report For Bin: " + strBinCode);
            i.putExtra(Intent.EXTRA_TEXT, "This is a stock report for Bin " + strBinCode + " as at "+MakeDate()+"." + "\n\n"
                    + "The current stock level is at " + intCurrentqty + "." + "\n"
                    + strCurrencyOrderMessage + "\n\n"
                    + "The Bin Stock Criteria Are:" + "\n"
                    + "Current Reorder Level: "+ intReorder + "\n"
                    + "Current Maximum Level: "+ intMax
                    + "\n\n"
                    + "Regards/Groete.");

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void SendEmailforWarehouse(String strBinCode, int intReorder, int intMax, int intCurrentqty) {

        String strCurrencyOrderMessage = null;

        if (intCurrentqty < intReorder) {
            strCurrencyOrderMessage = "Stock is currently under the minimum required holding and must be reordered.";
        } else if (intReorder < intCurrentqty & intCurrentqty < intMax) {
            strCurrencyOrderMessage = "Stock is currently at the appropriate level and no action is required.";
        } else if (intCurrentqty > intMax) {
            strCurrencyOrderMessage = "Stock has been over supplied, recommended action is to recount and report to Supply Chain if the stock if the number is confirmed to be over.";
        }

        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{});
        i.putExtra(Intent.EXTRA_SUBJECT, "Stock Report For Bin: " + strBinCode);
        i.putExtra(Intent.EXTRA_TEXT, "This is a stock report for Bin " + strBinCode + " as at "+MakeDate()+"." + "\n\n"
                + "The current stock level is at " + intCurrentqty + "." + "\n"
                + strCurrencyOrderMessage + "\n\n"
                + "The Bin Stock Criteria Are:" + "\n"
                + "Current Reorder Level: "+ intReorder + "\n"
                + "Current Maximum Level: "+ intMax
                + "\n\n"
                + "Regards/Groete.");

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
