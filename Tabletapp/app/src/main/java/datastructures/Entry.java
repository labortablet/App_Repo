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
    protected Long date; //sync_time
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
        this.title = title;
        this.attachment = attachment;
        this.date_user = entry_time;
        this.user = user;
        this.id = id;
        this.experiment_id = Experiment_id;
        this.date = sync_time;
        this.current_time = change_time;
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
        this.date_user = entry_time;
        this.user = user;
        this.id = 0;
        this.experiment_id = Experiment_id;
        this.date = null;
        this.current_time = change_time;
    }
    //Sorry, aber das hier ist totaler bullshit, Expeiment_id had hier irgendeinen belebigen wert, des wird nämlich garnicht gesetzt.
    //ich gehe mal davon aus, dass das hier auc nur drin ist weil wir
    //gerade noch alten Code haben der das braucht aber ansosnten muss das Weg!
    public Entry(long id, User user, String title, AttachmentBase attachment, Long entry_time, Long sync_time, Long change_time) {
        this.title = title;
        this.attachment = attachment;
        this.date_user = entry_time;
        this.user = user;
        this.id = id;
        //totaler blödsinn
        //this.Experiment_id =Experiment_id;
        this.date = sync_time;
        this.current_time = change_time;
    }

    public Entry(long id, User user, long Experiment_id, String title, AttachmentBase attachment, Long entry_time) {
        this.title = title;
        this.attachment = attachment;
        this.date_user = entry_time;
        this.user = user;
        this.id = id;
        this.experiment_id =Experiment_id;
        this.date = null;
        this.current_time = entry_time;
    }


    public boolean isSync() {
        return (this.date == null);
    }






}