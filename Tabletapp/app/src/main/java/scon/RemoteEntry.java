package scon;

public class RemoteEntry {
    private long id;
    private long Experiment_id;
    private String title;
    private String attachment_ser;
    private int attachment_type;
    private long sync_time;
    private long entry_time;
    private long change_time;
    private RemoteUser user;

    public long getId() {
        return id;
    }

    public long getExperiment_id() {
        return Experiment_id;
    }

    public String getTitle() {
        return title;
    }

    public String getAttachment_ser() {
        return attachment_ser;
    }

    public int getAttachment_type() {
        return attachment_type;
    }

    public long getSync_time() {
        return sync_time;
    }

    public long getEntry_time() {
        return entry_time;
    }

    public long getChange_time() {
        return change_time;
    }

    public RemoteUser getUser() {
        return user;
    }

    public RemoteEntry(long id, long experiment_id, String title, String attachment_ser, int attachment_type, long sync_time, long entry_time, long change_time, long user_id, String firstname, String lastname) {
        this.id = id;
        Experiment_id = experiment_id;
        this.title = title;
        this.attachment_ser = attachment_ser;
        this.attachment_type = attachment_type;
        this.sync_time = sync_time;
        this.entry_time = entry_time;
        this.change_time = change_time;
        this.user = new RemoteUser(user_id, firstname, lastname);
    }


}
