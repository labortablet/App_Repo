package company;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.test1.tabletapp.app.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import datastructures.AttachmentImage;
import datastructures.Entry;
import datastructures.Experiment;
import imports.ActivityRegistry;
import imports.App_Methodes;

/**
 * Created by Grit on 21.04.2015.
 */
public class Gui_NewImageEntry extends Activity {
    String photoFile;
    Bitmap bitmap;
    ImageView imageView;
    Experiment experiment;
    EditText editText;
    File file;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gui_new_image_entry);
        imageView = (ImageView)findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        ActivityRegistry.register(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        assert b != null;

        experiment = (Experiment) b.getSerializable("experiment");
    }
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;

    public void buttonEventHandler(View v) {  // butten events

        switch (v.getId()) {  // switch ID button

            case R.id.button:
                if (!(editText.getText().toString().trim().isEmpty())) {
                    try {
                        long time = App_Methodes.generateTimestamp();
                     Entry entry  =  Gui_StartActivity.mService.getObjectlevel_db().new_Entry(Gui_StartActivity.mService.getUser(), experiment, editText.getText().toString(), new AttachmentImage(file.getName()), time);
                        Log.e("ID_des IMAGE entries", String.valueOf(entry.getAttachment().getTypeNumber()));
                        this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.button2:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = dateFormat.format(new Date());
                photoFile = "Picture_" + date + ".jpg";
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                file = new File(Environment.getExternalStorageDirectory()+File.separator + photoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

                break;
        }}

    public void onResume() {
        super.onResume();  // Always call the superclass method first
    imageView.setImageBitmap(bitmap);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Check that request code matches ours:
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE)
        {
            //Get our saved file into a bitmap object:
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + photoFile);
             bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
        }
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


