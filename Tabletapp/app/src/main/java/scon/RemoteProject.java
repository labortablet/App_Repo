package scon;


public class RemoteProject {
    private long id;
    private String name;
    private String description;
    private long date_creation;

    public RemoteProject(long id, String name, String description, long date_creation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date_creation = date_creation;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getDate_creation() {
        return date_creation;
    }


}
