package datastructures;

public class Entry{
    protected long id;
    protected long Experiment_id;
    protected String title;
    protected AttachmentBase attachment;
    protected Long sync_time;
    protected long entry_time;
    protected long change_time;
    protected User user;


    public long  getChange_time() {
        return this.change_time;
    }

    public Long  getSync_time() {
        return this.sync_time;
    }

    public long  getEntry_time() {
        return this.entry_time;
    }

    public long getExperiment_id() {
        return this.Experiment_id;
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
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.change_time = change_time;
    }
    public Entry(long id, User user, String title, AttachmentBase attachment, Long entry_time, Long sync_time, Long change_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.change_time = change_time;
    }

    public Entry(long id, User user, long Experiment_id, String title, AttachmentBase attachment, Long entry_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        this.Experiment_id =Experiment_id;
        this.sync_time = null;
        this.change_time = entry_time;
    }


    public boolean isSync() {
        return (this.sync_time == null);
    }






}