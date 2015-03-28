package datastructures;


public class Experiment{
    protected Integer project_id;
    protected Integer id;
    protected String name;
    protected String description;
    protected Long date_creation;

    public Experiment(int id, Integer project_id, String name) {
        this.id = id;
        this.project_id = project_id;
        this.name = name;
    };

    public Experiment(Integer id, Integer project_id, String name, String description, Long date_creation) {
        this.project_id = project_id;
        this.id = id;
        this.name = name;
        this.description = description;

    };

    public void set_description(String new_description){
        this.description = new_description;
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
