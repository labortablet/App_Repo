package imports;

import java.util.Dictionary;
import java.util.Hashtable;
import scon.RemoteProject;

public class Project extends RemoteProject {
    public Dictionary experiments;

    public Project(RemoteProject a) {
        super(a);
        this.experiments = new Hashtable();
    }

    public Project(String name) {
        super(null, name, null);
        this.experiments = new Hashtable();
    }

    public Project() {
        super();
    }

    public void set_name(String new_name) {
        this.name = new_name;
    }

    public void set_description(String new_description) {
        this.description = new_description;
    }

    public void set_id(Integer new_id) {
        this.id = new_id;
    }

    public void update_by_Remote(RemoteProject a) {
        this.id = a.get_id();
        this.name = a.get_name();
        this.description = a.get_description();
    }
}