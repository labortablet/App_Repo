package imports;

public class User {
    private String lastname;
    private String firstname;
    private String user_email;
    private String pw_hash;
    private static Boolean lastname_first = false;


    public User(String firstname, String lastname, String user_email, String pw_hash){
        this.lastname = lastname;
        this.firstname = firstname;
        this.pw_hash = pw_hash;
        this.user_email = user_email;
    }
    public User(String user_email, String pw_hash){
        this.pw_hash = pw_hash;
        this.user_email = user_email;
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
    public String getUser_id(){

               //Added this so we are not bound to use the email as an id

                       return this.user_email;

           }




}