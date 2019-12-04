package za.co.rdata.r_datamobile.stringTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.DBHelperAssets;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_mr_notes;
import za.co.rdata.r_datamobile.R;

public class SelectNoteActivity extends AppCompatActivity {

    private List<model_pro_mr_notes> notes = new ArrayList<>();
    private String note_code = "";
    private String note_description = "";
    static public String tablename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_note);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.activity_select_note_listView);

        if (tablename== meta.pro_mr_route_notes.TableName) {
            notes = DBHelper.pro_mr_notes.getNotes();
        } else if (tablename==meta.pro_ar_notes.TableName) {
            notes = DBHelperAssets.pro_ar_asset_rows.getNotes();
        }

        Collections.sort(notes, new Comparator<model_pro_mr_notes>() {
            @Override
            public int compare(model_pro_mr_notes lhs, model_pro_mr_notes rhs) {
                return lhs.getNote_description().compareToIgnoreCase(rhs.getNote_description());
            }
        });

        ArrayAdapter<model_pro_mr_notes> adapter = new note_ListAdapter();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView_Note_Code = view.findViewById(R.id.note_list_item_note_code);
                TextView textView_Note_Description = view.findViewById(R.id.note_list_item_note_description);
                note_code = (String) textView_Note_Code.getText();
                note_description = (String) textView_Note_Description.getText();
                sendResult();
            }
        });
    }

    private class note_ListAdapter extends ArrayAdapter<model_pro_mr_notes> {

        public note_ListAdapter() {
            super(SelectNoteActivity.this, R.layout.select_note_list_item, notes);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_note_list_item, parent, false);

            model_pro_mr_notes note = notes.get(position);

            try {
                TextView tv_note_description = itemView.findViewById(R.id.note_list_item_note_description);
                TextView tv_note_code = itemView.findViewById(R.id.note_list_item_note_code);

                tv_note_description.setText(note.getNote_description());
                tv_note_code.setText(note.getNote_code());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }

    private void sendResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("note_code", note_code);
        resultIntent.putExtra("note_description", note_description);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
