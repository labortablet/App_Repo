package imports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.test1.tabletapp.app.R;

import java.util.LinkedList;

import company.Gui_NewTableEntry;
import datastructures.AttachmentTable;
import datastructures.AttachmentText;
import datastructures.Entry;
import datastructures.Experiment;

/**
 * Created by Grit on 20.04.2015.
 */
public class RowAndColumnPopup extends Activity {
EditText text;
EditText text2;
Experiment experiment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompts);
        text = (EditText) findViewById(R.id.editText5);
        text2 = (EditText)findViewById(R.id.editText6);
        ActivityRegistry.register(this);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;
        experiment = (Experiment) b.getSerializable("experiment");
    }

    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button5:
                if(!(text.getText().toString().trim().isEmpty()) && !(text2.getText().toString().trim().isEmpty())){
                int row    = Integer.parseInt(text.getText().toString());
                int column = Integer.parseInt(text2.getText().toString());

                Intent intent;
                intent = new Intent(this, Gui_NewTableEntry.class);

                    intent.putExtra("experiment",experiment);
                    intent.putExtra("row",row);
                    intent.putExtra("column",column);
                    startActivity(intent);
                    this.finish();
                }
                break;
        }}


}

