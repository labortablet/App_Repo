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

}
