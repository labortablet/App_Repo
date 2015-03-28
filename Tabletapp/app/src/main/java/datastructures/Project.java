package datastructures;

import java.util.Dictionary;
import java.util.Hashtable;



public class Project{ //implements Comparable
    //public Dictionary experiments;
    private long id;
    private String name;
    private String description;
    private Long date_creation;

    public Project(long id, String name) {
        this.id = id;
        this.name = name;
        this.description = null;
        this.date_creation = null;
    }


    public Project(long id, String name, String description, Long date_creation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date_creation = date_creation;
    }

    public void set_name(String new_name) {
        this.name = new_name;
    }

    public void set_description(String new_description) {
        this.description = new_description;
    }

    public void set_date_creation(long date_creation) {
        this.date_creation = date_creation;
    }


    /*
    public int compareTo(Project other) {
        if(this.remote_id == null || other.remote_id==null){
            return 0;
        };
        return this.remote_id.compareTo(other.remote_id);
    };
*/
    public long get_id(){
        return this.id;
    }

    public String get_name(){
        return this.name;
    };

    public String get_description(){
        return this.description;
    };

    public Long get_date_creation(){return  this.date_creation;};
}