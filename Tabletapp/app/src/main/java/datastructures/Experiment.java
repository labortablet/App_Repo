package datastructures;


import java.io.Serializable;

import scon.RemoteEntry;
import scon.RemoteExperiment;

public class Experiment implements Serializable{
    protected long project_id;
    protected long id;
    protected String name;
    protected String description;
    protected Long date_creation;

    public Experiment(long id, long project_id, String name) {
        this(id, 0, name, null, null);
    }
    public Experiment(long id,  String name, String description ) {
        this(id, 0, name, description, null);
    }
    public Experiment(long id, long project_id, String name, String description, Long date_creation) {
        this.id = id;
        this.project_id = project_id;
        this.name = name;
        this.description = description;
        this.date_creation = date_creation;
    }

    public void update(RemoteExperiment remoteexperiment, long project_id){
        this.name = remoteexperiment.getName();
        this.description = remoteexperiment.getDescription();
        this.date_creation = remoteexperiment.getDate_creation();
        this.project_id = project_id;
    }

    public long get_id() {
        return this.id;
    }

    public long get_project_id() {
        return this.project_id;
    }

    public String get_name() {
        return this.name;
    }

    public String get_description() {
        return this.description;
    }
}
