package scon;



public class RemoteUser {
    private String lastname;
    private String firstname;
    private long id;

    public RemoteUser(long id, String firstname, String lastname){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    };

    public String getFirstname() {
        return firstname;
    }

    public long getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }
}
