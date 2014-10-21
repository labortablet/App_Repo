package scon;

public class RemoteProject implements Comparable<RemoteProject>{
    protected Integer id;
    protected String name;
    protected String description;

    public RemoteProject(Integer id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    };

    public RemoteProject(RemoteProject a){
        this.id = a.id;
        this.name = a.name;
        this.description = a.description;
    };

    public RemoteProject() {

    }


    @Override
    public int compareTo(RemoteProject other) {
        if(this.id == null || other.id==null){
            return 0;
        };
        return this.id.compareTo(other.id);
    };

    public Integer get_id(){
        return this.id;
    };

    public String get_name(){
        return this.name;
    };

    public String get_description(){
        return this.description;
    };
}
