package scon;

import datastructures.AttachmentBase;
import datastructures.BaseEntry;
import datastructures.User;

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
    public RemoteEntry(int remote_id, AttachmentBase attachment, Long entry_time, int Experiment_id, Long sync_time, Long change_time, String title, User user)
    {
        this.remote_id = remote_id;
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
