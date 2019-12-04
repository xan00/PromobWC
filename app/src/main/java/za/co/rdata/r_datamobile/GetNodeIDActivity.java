package za.co.rdata.r_datamobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class GetNodeIDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_node_id);

        Button bEnter = findViewById(R.id.Get_Node_Benter);
        bEnter.setOnClickListener(Enter_Clicked);
    }

    private View.OnClickListener Enter_Clicked = v -> {
        EditText ETnode_id = findViewById(R.id.Get_Node_TVnodeID);
        String Snode_id = String.valueOf(ETnode_id.getText());
        if (TextUtils.isEmpty(Snode_id)) {
            ETnode_id.setError(getString(R.string.error_invalid_node_id));
            ETnode_id.requestFocus();
        } else {
            sendResult(Snode_id);
        }
    };

    private void sendResult(String Snode_id) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("node_id", Snode_id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
