package datastructures;
import java.net.URL;
import java.security.MessageDigest;

public class User {
    private String lastname;
    private String firstname;
    private String user_email;
    private byte[] pw_hashb;
    private static Boolean lastname_first = false;
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

    public User(String email)
{
    this.user_email = email;
}

    public User(String firstname, String lastname){
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public User(String user_email, String password, URL server, long id){
        this.user_email = user_email;
        this.setPw(password);
        this.server = server;
        this.id = id;
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




}