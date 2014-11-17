package imports;
import java.security.MessageDigest;

public class User {
    private String lastname;
    private String firstname;
    private String user_email;
    private String pw_hash;
    private byte[] pw_hashb;
    private static Boolean lastname_first = false;

    //this methode can be used if one has the unhashed password
    public void setPw(String password){
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
            result = new String(resultb, "UTF-8");
        }catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not available, this should really not happen");
        }
        this.pw_hash = result;
        this.pw_hashb = resultb;
    }

public User(String email)
{
    this.user_email = email;
}
    public User(String firstname, String lastname, String user_email, String pw_hash){
        this.lastname = lastname;
        this.firstname = firstname;
        this.user_email = user_email;
        this.pw_hash = pw_hash;
        try {
            this.pw_hashb = pw_hash.getBytes("UTF_8");
        }catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not available, this should really not happen");
        }


    }
    public User(String user_email, String pw_hash){
        this.pw_hash = pw_hash;
        this.user_email = user_email;
        try {
            this.pw_hashb = pw_hash.getBytes("UTF_8");
        }catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not available, this should really not happen");
        }
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

    public void setLogin(String user_email, String pw_hash){
        this.user_email = user_email;
        this.pw_hash = pw_hash;
    }

    public String getUser_email(){
        return this.user_email;
    }

    public String getPw_hash(){
        return this.pw_hash;
    }

    public byte[] getPw_hashb(){
        return this.pw_hashb;
    }

    //Added this so we are not bound to use the email as an id
    public String getUser_id(){
        return this.user_email;
    }




}