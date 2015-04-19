package datastructures;


import java.io.Serializable;

public class Experiment implements Serializable{
    protected long project_id;
    protected long id;
    protected String name;
    protected String description;
    protected Long date_creation;

    public Experiment(long id, long project_id, String name) {
        this.id = id;
        this.project_id = project_id;
        this.name = name;
        this.description = null;
        this.date_creation = null;
    };

    public Experiment(long id, long project_id, String name, String description, Long date_creation) {
        this.project_id = project_id;
        this.id = id;
        this.name = name;
        this.description = description;
        this.date_creation = date_creation;
    };

    public void set_description(String new_description){
        this.description = new_description;
    };

    public long get_id() {
        return this.id;
    };

    public long get_project_id() {
        return this.project_id;
    };

    public String get_name() {
        return this.name;
    };

    public String get_description() {
        return this.description;
    };
}
