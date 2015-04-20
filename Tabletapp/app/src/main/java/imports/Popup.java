package imports;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.test1.tabletapp.app.R;

/**
 * Created by Grit on 03.06.2014.
 * Dynamic popup with setable text
 */
public class Popup extends DialogFragment {
    /**
     * String ID of the Output string in the Strings.xml
     * String ID{@value} .
     */
int the_String = 0;

    /**
     * Setting the String of the popup
     * @param input_string   Id of the String that should be set.
     */
public void set_String(int input_string){
    the_String = input_string;
}
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(the_String).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            return builder.create();

                    }






    }

