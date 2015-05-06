package datastructures;


import java.io.Serializable;

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
