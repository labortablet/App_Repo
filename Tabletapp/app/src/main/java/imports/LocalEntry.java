package imports;

/*
*
* This Class is for building the LocalEntry object we need in the Activity
*
* */


import java.sql.Timestamp;
import java.util.Calendar;

import scon.RemoteEntry;

public class LocalEntry extends RemoteEntry implements Comparable<LocalEntry> {
    /**
     *This is the Local ID an LocalEntry can gets From the tab.
     * @value
     * @since 1.0
     */
    Integer local_id;



    boolean sync;

    /**This is a 2D array which holds the content of the Table.
     *
     * @value
     * @since 1.0
     */

    public LocalEntry(RemoteEntry a,boolean sync) {
        super(a);
    this.sync = sync;
    }
    public LocalEntry(RemoteEntry a) {
        super(a);

    }
    /**
     * Konstruktor For the Keyboard_entry
     *@param title Title of the LocalEntry
     *@param attachment Content of the LocalEntry

     *@param entry_time Time when the entry was created
     *@param user The name of the LocalEntry creator

     *@param sync The value which says if the LocalEntry already on the Server
     */

    public LocalEntry(String title, String attachment,Long  entry_time, User user, boolean sync) {
        this.title = title;
        this.attachment = new AttachmentText(attachment);
        this.entry_time = entry_time;
        this.user = user;
        this.sync = sync;
    }

    public LocalEntry(int local_id, User user, int Experiment_id, String title, AttachmentBase attachment, boolean sync, Long entry_time, Long sync_time, Long change_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.local_id = local_id;
        this.sync = sync;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.change_time = change_time;
    }
    public LocalEntry(String title, AttachmentBase attachment,Long  entry_time, User user, boolean sync,int Experiment_id) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.local_id = null;
        this.sync = sync;
        this.Experiment_id =Experiment_id;
        this.sync_time = null;
        this.change_time = null;
    }

    /**
     * Konstruktor For the Keyboard_entry
     *@param title Title of the LocalEntry
     *@param attachment Content of the LocalEntry

     *@param entry_time Time when the entry was created
     *@param user The name of the LocalEntry creator

     *@param sync The value which says if the LocalEntry already on the Server
     */

    /**
     * Returns the Sync Value
     * @return    status of synced
     */
    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
/*
    public LocalEntry(Integer rem_id,  String title, Long  sync_time,Long  entry_time, User user, String[][] array, boolean sync) {
        this.remote_id = rem_id;
        this.title = title;
        this.attachment_type = 2;
        this.sync_time = sync_time;
        this.entry_time = entry_time;
        this.user = user;
        this.attachment = new AttachmentTable(array);
        this.sync = sync;


    }*/


/*
    public LocalEntry( String title,Long  entry_time, User user, String[][] array, boolean sync) {


        this.title = title;
        this.attachment_type = 2;

        this.entry_time = entry_time;
        this.user = user;

        this.attachment = new AttachmentTable(array);
        this.sync = sync;


    }
    public LocalEntry(String title, String[][] array,Long  entry_time, User user, boolean sync,Integer local_id,int Experiment_id,Long  sync_time,Long change_time) {
        this.title = title;
        this.attachment = new AttachmentTable(array);
        this.attachment_type = 2;
        this.entry_time = entry_time;
        this.user = user;
        this.local_id = local_id;
        this.sync = sync;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.change_time = change_time;
    }*/

    @Override
    public int compareTo(LocalEntry other_entry) {
        return this.entry_time.compareTo(other_entry.getEntry_time());
    }





}