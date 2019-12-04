package za.co.rdata.r_datamobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MaintainWebServerSettingsActivity extends AppCompatActivity {

    public String serverURL;
    Spinner server_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_web_server_settings);
        server_ip = findViewById(R.id.spnWebServerIP);
        server_ip.setSelection(0);

        EditText ET_serverURL = findViewById(R.id.A_MaintainWebServerSettings_ET_ServerURL);
        ET_serverURL.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        serverURL = sharedPref.getString("serverURL", "http://196.41.123.51:31415/sync/");
        ((EditText) findViewById(R.id.A_MaintainWebServerSettings_ET_ServerURL)).setText(serverURL);
        (findViewById(R.id.A_MaintainWebServerSettings_B_Save)).setOnClickListener(Save_Clicked);
        (findViewById(R.id.A_MaintainWebServerSettings_B_Cancel)).setOnClickListener(Cancel_Clicked);

        String[] ipaddresses = getResources().getStringArray(R.array.ip_addresses);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, R.layout.ip_text_layout, ipaddresses
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.ip_text_layout);
        server_ip.setAdapter(spinnerArrayAdapter);

    }

    private View.OnClickListener Save_Clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            serverURL = "http://"+(server_ip.getSelectedItem().toString())+":31415/sync/";
            /*
            if (TextUtils.isEmpty(serverURL)) {
                ET_serverURL.setError(getString(R.string.error_invalid_node_id));
                ET_serverURL.requestFocus();
            } else {
              */
            SaveSettings();
            //}
        }
    };

    private View.OnClickListener Cancel_Clicked = v -> finish();

    private void SaveSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("serverURL", serverURL);
        editor.apply();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("serverURL", serverURL);
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
