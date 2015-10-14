package datastructures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

import scon.Base64;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentImage extends AttachmentText {
    public AttachmentImage(String titel) {
        super(titel);
    }
   // public String serialize(){
      //  Base64 b64 = new Base64();
      //  Bitmap bitmap = decodeSampledBitmapFromFile(Environment.getExternalStorageDirectory()+ File.separator + this.getContent(),1000,1000);
    //    return b64;
   // };
    @Override
    public int getTypeNumber(){return 3;}

    private static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
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
