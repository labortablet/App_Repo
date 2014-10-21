package imports;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Grit on 08.08.2014.
 */
public class Lablet_Functions {

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = myFormat.format(new Date()); // Getting the actual date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
