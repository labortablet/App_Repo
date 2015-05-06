package datastructures;
import java.io.Serializable;
import java.net.URL;
import java.security.MessageDigest;

public class User implements Serializable{
    private String lastname="";
    private String firstname ="";
    private String user_email ="";
    private byte[] pw_hashb;
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

    //not sure if we need this: nope
    public User(String firstname, String lastname){
        this.lastname = lastname;
        this.firstname = firstname;
        this.id = 0;
    }

    public User(String firstname, String lastname, long id){
        this(firstname,lastname);
        this.id = id;
        this.server = null;
    }

    public User(String user_email, String password, URL server, long id){
        this.user_email = user_email;
        this.setPw(password);
        this.server = server;
        this.firstname = "";
        this.lastname = "";
        this.id = id;
    }

    public User(String user_email, byte[] hashed_pw, URL server, long id, String firstname,String lastname){
        this(firstname,lastname,id);
        this.user_email = user_email;
        this.pw_hashb = hashed_pw;
        this.server = server;
    }

    public String display(String separator,boolean lastname_first){
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

    public long getId(){return this.id;}
}