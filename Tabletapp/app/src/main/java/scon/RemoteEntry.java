package scon;



import java.sql.Timestamp;

import imports.BaseEntry;
import imports.User;

/**
 * Created by Grit on 07.09.2014.
 */
public class RemoteEntry extends BaseEntry {


    public RemoteEntry(RemoteEntry a) {
       this.attachment = a.attachment;
       this.attachment_type =a.attachment_type;
       this.entry_time =a.entry_time;
       this.Experiment_id =a.Experiment_id;
       this.remote_id = a.remote_id;
       this.sync_time = a.sync_time;
       this.title = a.title;
       this.user = a.user;
       this.change_time=a.change_time;
    }
    public RemoteEntry(Object attachment,int attachment_type,Timestamp entry_time,int Experiment_id,Timestamp sync_time,Timestamp change_time, String title, User user)
    {


        this.attachment = attachment;
        this.attachment_type =attachment_type;
        this.entry_time =entry_time;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.title = title;
        this.user = user;
        this.change_time = change_time;





    }
    public RemoteEntry(){



    }
}
