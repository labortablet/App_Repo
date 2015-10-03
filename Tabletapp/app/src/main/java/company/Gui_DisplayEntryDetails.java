package company;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.tabletapp.app.R;

import java.io.File;


import datastructures.Entry;


import static imports.App_Methodes.return2DArray;

/**
 * @author Grit on 02.08.2014.
 *         Shows the Specified LocalEntry Attributes
 */
public class Gui_DisplayEntryDetails extends Activity {
    /**
     * This is the LocalEntry Which is Selected for closer Details
     * Selected LocalEntry {@value} .
     *
     * @since 1.0
     */

//private Integer entry_Selected = Gui_DisplayEntryList.getEntry_Selected();

    /**
     * This is the ProjectExperimentEntries Object which contains all Project, Experiments and Entries.
     * Selected Object {@value} .
     *
     * @since 1.0
     */

    /**
     * TextView for Title
     * Title {@value} .
     *
     * @since 1.0
     */
    TextView textView;
    /**
     * TextView for Attachment
     * Attachment {@value} .
     *
     * @since 1.0
     */
    TextView textView2;
    /**
     * TextView for entry_time
     * Entry_time {@value} .
     *
     * @since 1.0
     */
    TextView textView3;
    /**
     * TextView for user
     * User name{@value} .
     *
     * @since 1.0
     */
    TextView textView4;
    /**
     * TableLayout only if Detail view of a Table
     * TableView {@value} .
     *
     * @since 1.0
     */
    TableLayout table;
    /**
     * This is the experiment, where the LocalEntry  belongs to
     * Selected Experiment {@value} .
     *
     * @since 1.0
     */
    ImageView imageView;

    /**
     * This is the Project, where the Experiment  belongs to
     * Selected Project {@value} .
     *
     * @since 1.0
     */


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;

        Entry entry = (Entry) b.getSerializable("entry");

        switch (entry.getAttachment().getTypeNumber()) {
            case 1: // For Keyboard LocalEntry
                if (entry.getSync_time() != 0)
                    setContentView(R.layout.entry_keyboarddetails_synctrue); // Setting Layout
                else
                    setContentView(R.layout.entry_keyboarddetails_syncfalse);

                textView = (TextView) findViewById(R.id.textView2);
                textView2 = (TextView) findViewById(R.id.textView4);
                textView3 = (TextView) findViewById(R.id.textView6);
                textView4 = (TextView) findViewById(R.id.textView8);

                textView.setText(entry.getTitle());

                textView2.setText(entry.getAttachment().getContent());
                textView3.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(entry.getEntry_time() * 1000)));
                //     textView3.setText(projectExperimentEntries.get(project_Selected).getExperimentEntry().get(experiment_Selected).getEntriesList().get(entry_Selected).getEntry_time().toString());
                //TODO: FIX HERE THE DISPLAY FUNKTION WITH VARIABLE VIEW
                textView4.setText(entry.getUser().display("  ", false));
                break;
            case 2: //For Table LocalEntry
                if (entry.getSync_time() != 0)
                    setContentView(R.layout.entry_tabledetails_synctrue);  // Setting Layout
                else
                    setContentView(R.layout.entry_tabledetails_syncfalse);

                textView = (TextView) findViewById(R.id.textView2);
                textView2 = (TextView) findViewById(R.id.textView4);
                textView3 = (TextView) findViewById(R.id.textView6);
                table = (TableLayout) findViewById(R.id.tableLayout2);

                textView.setText(entry.getTitle());
                textView2.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(entry.getEntry_time() * 1000)));
                textView3.setText(entry.getUser().display("  ", false));
                try {


                    String[][] strings = return2DArray(entry.getAttachment().getContent());
                    for (String[] s : strings) { // Starting Table output

                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        for (String string : s) {
                            EditText tv = new EditText(this);
                            tv.setInputType(0);

                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));

                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(string);
                            row.addView(tv);
                            row.setGravity(Gravity.CENTER);
                        }

                        table.addView(row);

                    } // End Table output
                } catch (IndexOutOfBoundsException Exc) {
                    Exc.printStackTrace();
                }
                break;

            case 3:

                if (entry.getSync_time() != 0)
                    setContentView(R.layout.entry_imagedetails_synctrue); // Setting Layout
                else
                    setContentView(R.layout.entry_imagedetails_syncfalse);

                Log.e("Entry titel",entry.getTitle());
               textView = (TextView) findViewById(R.id.textView19);

                 textView3 = (TextView) findViewById(R.id.textView21);
                textView4 = (TextView) findViewById(R.id.textView23);

                textView.setText(entry.getTitle());

               textView3.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(entry.getEntry_time() * 1000)));
                textView4.setText(entry.getUser().display("  ", false));
                imageView = (ImageView) findViewById(R.id.imageView3);
                Bitmap bitmap = decodeSampledBitmapFromFile(Environment.getExternalStorageDirectory()+File.separator + entry.getAttachment().getContent(), 1000, 700);
                imageView.setImageBitmap(bitmap);

                break;

        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }  // Standart Android Methoden für apps

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_refresh:
                // refresh
                //  Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), Gui_Settings.class));
                return true;
            case R.id.sync:
                long tenMinutes = 600000;
                long millis = System.currentTimeMillis() - Gui_StartActivity.mService.getTimeTillSync();
                long secs = (tenMinutes - millis) / 1000;
                long mins = secs / 60;
                Toast.makeText(getApplicationContext(), "Next Sync in" + mins + " Minutes", Toast.LENGTH_SHORT).show();
                return true;
            // help action
            default:
                return super.onOptionsItemSelected(item);
        }
    }  // Standart Android Methoden für apps



    private int countLetter(String str, String letter) {
        int count = 0;
        for (int pos = -1; (pos = str.indexOf(letter, pos + 1)) != -1; count++) ;
        return count;
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


}
