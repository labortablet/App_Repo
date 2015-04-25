package datastructures;

import java.io.Serializable;

public class Entry implements Serializable{
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
    //Konstruktor for new entries whitch got no id yet
    //FIXME
    //Es gibt ekine Entries die noch keine ID haben weil sie zuerst in die DB geschrieben werden und dann
    //erzeugt werden
    //werde es jetzt erstmal drin lassen, aber später muss das hier weg
    // das ist geplant, da das interface aber noch nicht fertig ist, nutze ich die alten funktionen, bis das interface fertig ist, da du ja deine eigenen Datenbank klasse geschrieben hast, gehe ich davon aus, das du auch die entsprechenden sql queries geschrieben hast, ansonsten muss ich meine queries noch anpassen.
    public Entry( User user, long Experiment_id, String title, AttachmentBase attachment, Long entry_time, Long change_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        this.Experiment_id =Experiment_id;
        this.sync_time = sync_time;
        this.change_time = change_time;
    }
    //Sorry, aber das hier ist totaler bullshit, Expeiment_id had hier irgendeinen belebigen wert, des wird nämlich garnicht gesetzt.
    //ich gehe mal davon aus, dass das hier auc nur drin ist weil wir
    //gerade noch alten Code haben der das braucht aber ansosnten muss das Weg!
    public Entry(long id, User user, String title, AttachmentBase attachment, Long entry_time, Long sync_time, Long change_time) {
        this.title = title;
        this.attachment = attachment;
        this.entry_time = entry_time;
        this.user = user;
        this.id = id;
        //totaler blödsinn
        //this.Experiment_id =Experiment_id;
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