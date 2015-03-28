package datastructures;

public class Entry{
    protected Integer id;
    protected Integer Experiment_id;
    protected String title;
    protected AttachmentBase attachment;
    protected Long sync_time;
    protected Long entry_time;
    protected Long change_time;
    protected User user;


    public Long  getChange_time() {
        return this.change_time;
    }

    public Long  getSync_time() {
        return this.sync_time;
    }

    public Long  getEntry_time() {
        return this.entry_time;
    }

    public Integer getExperiment_id() {
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

    public Entry(int id, User user, int Experiment_id, String title, AttachmentBase attachment, Long entry_time, Long sync_time, Long change_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.change_time = change_time;
    }

    public Entry(int id, User user, int Experiment_id, String title, AttachmentBase attachment, Long entry_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        this.Experiment_id =Experiment_id;
        this.sync_time = null;
        this.change_time = null;
    }


    public boolean isSync() {
        return (this.sync_time == null);
    }






}