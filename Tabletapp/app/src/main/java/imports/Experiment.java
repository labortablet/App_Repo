package imports;
import scon.RemoteExperiment;


public class Experiment extends RemoteExperiment {
    //private SortedSet<Entry> entrys = new TreeSet(); //we need to add an comparator here
private int local_id;
    public Experiment(Integer project_id, String name) {
        super(project_id, null, name, null);
    };

    public Experiment(RemoteExperiment a){
        super(a);
    }
public Experiment(){
   super();

}
    public Experiment(Integer local_id, Integer id, Integer project_id, String name, String description) {
        this.project_id = project_id;
        this.id = id;
        this.name = name;
        this.description = description;
        this.local_id = local_id;
    };

    public void set_id(Integer new_id){
        this.id = new_id;
    };

    public void set_description(String new_description){
        this.description = new_description;
    };

    public void update_by_remote(RemoteExperiment a){
        this.id = a.get_id();
        this.project_id = a.get_project_id();
        this.description = a.get_description();
        this.name = a.get_name();
    };
}
