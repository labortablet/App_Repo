package company;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Grit on 12.06.2014.
 * Registry for the Activity and their Intents, needed to close them all on one click.
 *
 */



public class ActivityRegistry {
    /**
     * A List for the activ Activities.
     * Active list {@value}.
     */
    private static List<Activity> _activities;

/**
 * Adds a new Activity to the List.
 * @param activity It's the new Activity/Intent.
 *
 */

    public static void register(Activity activity) {
        if(_activities == null) {
            _activities = new ArrayList<Activity>();
        }
        _activities.add(activity);
    }

    /*
    * Finishing all Activities/Intents in the Activity list.
    *
    * */
    public static void finishAll() {
        for (Activity activity : _activities) {
            activity.finish();
        }
    }
    public static String[] convert_array(String[][] temp,int j){
        String[] values = new String[j];
        for(int i=0; i < j ;i++ ) {   // Zweidimensionales array in eindimensionales array umwandeln.
            if (temp[i][0] == null)
                break;
            else
                values[i] = temp[i][0];
        }
        return values;
    }
    public static String[] convert_array2(String[][] temp,int j,int z){
        String[] values = new String[j];

    for (int i = 1; i <= j; i++) {
        if (temp[z][i] == null){
            break;
        }
        else
            values[i] = temp[z][i];
}
        return values;
    }
 /*
   public static int array_rows(String[][] temp,String word)
    {
        int t = 0;

           get_wordposition1(temp,word);

        return t;
    }

    */
public static int get_wordposition1(String[][] temp, String word){
    int t =0;
     for (int i=0 ;temp.length >= i ;i++){
if(temp[i][0] == word ) {
    t = i;
    break;
}
     }
    return t;
}

public static int Lookup_String(String[][] temp,String lookup ){
    int i;
    int ii = 0;
    for(i = 0; i<temp.length; i++) {
            if(temp[i][0].equals(lookup)) {
              ii = i;
                break;
            }
        }
        return ii;
    }





}
