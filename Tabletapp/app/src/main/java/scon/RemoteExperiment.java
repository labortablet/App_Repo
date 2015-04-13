package scon;

public class RemoteExperiment {
    private long project_id;
    private long id;
    private String name;
    private String description;
    private long date_creation;

    public RemoteExperiment(long id, long project_id, String name, String description, long date_creation) {
        this.project_id = project_id;
        this.id = id;
        this.name = name;
        this.description = description;
        this.date_creation = date_creation;
    }

    public long getProject_id() {
        return project_id;
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

    public long getDate_creation() {
        return date_creation;
    }



}