package imports;

/*
*
* This Class is for building the LocalEntry object we need in the Activity
*
* */


import java.sql.Timestamp;

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

    /**
     * Konstruktor For the converting the Remote to Local entry
     *@param rem_id   The Remote ID
     *@param title Title of the LocalEntry
     *@param attachment Content of the LocalEntry
     *@param sync_time Sync time of the LocalEntry
     *@param entry_time Time when the entry was created
     *@param user The name of the LocalEntry creator

     *@param sync The value which says if the LocalEntry already on the Server
     */

    public LocalEntry(Integer rem_id, String title, String attachment, Timestamp sync_time, Timestamp entry_time, User user, boolean sync) {
        this.remote_id = rem_id;
        this.title = title;
        this.attachment = new AttachmentText(attachment);
        this.attachment_type = 1;
        this.sync_time = sync_time;
        this.entry_time = entry_time;
        this.user = user;
        this.sync = sync;
    }

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

    public LocalEntry( String title, String attachment,Timestamp entry_time, User user, boolean sync) {
        this.title = title;
        this.attachment = new AttachmentText(attachment);
        this.attachment_type = 1;
        this.entry_time = entry_time;
        this.user = user;
        this.sync = sync;
    }
    /**
     * Returns the Sync Value
     * @return    status of synced
     */
    public boolean isSync() {
        return sync;
    }
    /**
     * Changes the status of the sync
     * @param sync  status of sync
     */



    /**
     * Konstruktor For the Table_entry
     * @param rem_id   The Remote ID
     * @param title Title of the LocalEntry
     *@param sync_time Sync time of the LocalEntry
     *@param entry_time Time when the entry was created
     *@param user The name of the LocalEntry creator
     *@param array The array holds the Content of the Table_entry
     *@param sync The value which says if the LocalEntry already on the Server
     *
     */

    public LocalEntry(Integer rem_id,  String title, Timestamp sync_time, Timestamp entry_time, User user, String[][] array, boolean sync) {
        this.remote_id = rem_id;
        this.title = title;
        this.attachment_type = 2;
        this.sync_time = sync_time;
        this.entry_time = entry_time;
        this.user = user;
        this.attachment = new AttachmentTable(array);
        this.sync = sync;


    }
    /**
     * Konstruktor For the Table_entry

     * @param title Title of the LocalEntry
     *@param entry_time Time when the entry was created
     *@param user The name of the LocalEntry creator
     *@param array The array holds the Content of the Table_entry
     *@param sync The value which says if the LocalEntry already on the Server
     *
     */

    public LocalEntry( String title,Timestamp entry_time, User user, String[][] array, boolean sync) {


        this.title = title;
        this.attachment_type = 2;

        this.entry_time = entry_time;
        this.user = user;

        this.attachment = new AttachmentTable(array);
        this.sync = sync;


    }

    @Override
    public int compareTo(LocalEntry other_entry) {
        return this.entry_time.compareTo(other_entry.getEntry_time());
    }





}