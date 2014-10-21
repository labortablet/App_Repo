package scon;

public class RemoteExperiment implements Comparable<RemoteExperiment> {
    protected Integer project_id;
    protected Integer id;
    protected String name;
    protected String description;

    public RemoteExperiment(Integer project_id, Integer id, String name, String description) {
        this.project_id = project_id;
        this.id = id;
        this.name = name;
        this.description = description;
    };

    public RemoteExperiment(RemoteExperiment a) {
        this.project_id = a.project_id;
        this.id = a.id;
        this.name = a.name;
        this.description = a.description;
    };
    public RemoteExperiment(){


    }



    @Override
    public int compareTo(RemoteExperiment other) {
        return this.id.compareTo(other.id);
    };

    public Integer get_id() {
        return this.id;
    };

    public Integer get_project_id() {
        return this.project_id;
    };

    public String get_name() {
        return this.name;
    };

    public String get_description() {
        return this.description;
    };
}
