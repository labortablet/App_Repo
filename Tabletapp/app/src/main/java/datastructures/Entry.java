package datastructures;

import java.io.Serializable;

public class Entry implements Serializable{
    protected long id;
    protected long experiment_id;
    protected String title;
    protected AttachmentBase attachment;
    protected User user;

    //altered names so they fit with the MySQL Server
    //I did not change the getter and setters to not break anything
    protected Long date = null; //sync_time
    protected long date_user; //entry_time
    protected long current_time; //change_time

    public long  getChange_time() {
        return this.current_time;
    }

    public Long  getSync_time() {
        return this.date;
    }

    public long  getEntry_time() {
        return this.date_user;
    }

    public long getExperiment_id() {
        return this.experiment_id;
    }

    public String getTitle() {
        return this.title;
    }

    public AttachmentBase getAttachment() {
        return this.attachment;
    }

    public User getUser() {
        return this.user;
    }

    public Entry(long id, User user, long Experiment_id, String title, AttachmentBase attachment, Long entry_time, Long sync_time, Long change_time) {
        this(id,user,Experiment_id,title,attachment,entry_time);
        this.date = sync_time;
        this.current_time = change_time;
    }
    public Entry(long id, User user, long Experiment_id, String title, AttachmentBase attachment, Long entry_time) {
        this.title = title;
        this.attachment = attachment;
        this.date_user = entry_time;
        this.user = user;
        this.id = id;
        this.experiment_id = Experiment_id;
        this.current_time = entry_time;
    }


    public boolean isSync() {
        return (this.date == null);
    }






}