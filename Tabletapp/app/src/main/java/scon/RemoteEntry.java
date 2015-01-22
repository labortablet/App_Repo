package scon;



import java.sql.Timestamp;
import java.util.Calendar;

import imports.AttachmentBase;
import imports.AttachmentText;
import imports.BaseEntry;
import imports.User;

/**
 * Created by Grit on 07.09.2014.
 */
public class RemoteEntry extends BaseEntry {


    public RemoteEntry(RemoteEntry a) {
       this.attachment = a.attachment;
       this.entry_time =a.entry_time;
       this.Experiment_id =a.Experiment_id;
       this.remote_id = a.remote_id;
       this.sync_time = a.sync_time;
       this.title = a.title;
       this.user = a.user;
       this.change_time=a.change_time;
    }
    public RemoteEntry(AttachmentBase attachment, Long entry_time, int Experiment_id, Long sync_time, Long change_time, String title, User user)
    {
        this.attachment = attachment;
        this.entry_time =entry_time;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.title = title;
        this.user = user;
        this.change_time = change_time;
    }
    public RemoteEntry(){};
}
