package datastructures;
import java.io.Serializable;
import java.net.URL;
import java.security.MessageDigest;

public class User implements Serializable{
    private String lastname;
    private String firstname;
    private String user_email;
    private byte[] pw_hashb;
    private static Boolean lastname_first = false;
    //TODO
    //wat?
    //das ist eine App weite Einstellung
    //warum wird das den an jedes Objekt drangepapt?
    private URL server;
    private long id;

    //this method can be used if one has the unhashed password

    public static byte[] hashedPW(String password){
        byte[] resultb = null;
        String result = null;
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        }catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-265 not available, this should really not happen");
        }
        sha256.reset();
        try {
            resultb = sha256.digest(password.getBytes("UTF-8"));
        }catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not available, this should really not happen");
        }
        return resultb;
    }

    public void setPw(String password){
        this.pw_hashb = hashedPW(password);
    }

    //TODO
    //wat? Warum brauchen wir das den???
    public User(String email)
{
    this.user_email = email;
}

    //not sure if we need this
    public User(String firstname, String lastname){
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public User(String user_email, String password, URL server, long id){
        this.user_email = user_email;
        this.setPw(password);
        this.server = server;
        this.firstname = null;
        this.lastname = null;
        this.id = id;
    }

    //TODO: remove once the interface is done
    public User(String user_email, String password, URL server){
        this.user_email = user_email;
        this.setPw(password);
        this.server = server;
        this.firstname = null;
        this.lastname = null;
        this.id = 0;
    }

    public String display(String separator){
        if(this.lastname != null && this.firstname != null){
            if(lastname_first){
                return this.lastname + separator + this.firstname;
            }else{
                return this.firstname + separator + this.lastname;
            }
        }else{
            return this.user_email;
        }
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setName(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public void setLogin(String user_email, String password){
        //TODO
        //we need to add some connection to the database so we also update
        //we state of the db
        this.user_email = user_email;
        this.setPw(password);
    }

    public String getUser_email(){
        return this.user_email;
    }

    public byte[] getPw_hash(){
        return this.pw_hashb;
    }

    //Added this so we are not bound to use the email as an id
    public String getUser_id(){
        return this.user_email;
    }

    public long getId(){return this.id;}

    //auch hier: es gibt keinen Grund die ID jemals zu setzen!
    //wenn wir es noch brauchen weil wir alten Code haben, okay, ansonsten weg damit!
    //1. warum hat er dann eine ID?
    //2. ich habe keine lust jedesmal wenn ein neuer entry erzeugt wird die datenbank ran zu ziehen um zu schauen welche ID zu der Email gehört
    //3. da es keinen konstruktor mit id gibt, muss ich sie im nachhinein setzen was sowieso besser ist

    //Du bist mir ein Spaßvogel.
    //In https://bitbucket.org/Gritu/main_repo/src/d6df48c48f5d/Tabletapp/app/src/main/java/datastructures/User.java
    //hast du die id aus dem Konstruktur raus gepatched.
    //die id sollte im Konstuktur natürlich gesetzt werden und danach ist sie konstant.
    //TODO: remove once the interface is done
    public void setId(long id) {
        this.id = id;
    }
}