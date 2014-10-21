package company;

/**
 * Created by Grit on 03.06.2014.
 */

 public class Connect  {
  


    public static int login_server(String link,String email,String password){
int i = 0;
/*
        //the login etc data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("link",link));
        nameValuePairs.add(new BasicNameValuePair("email",email));
        nameValuePairs.add(new BasicNameValuePair("password",password));

        //http post

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(""); //http/ php datei
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        //parse json data
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Log.i("log_tag","id: "+json_data.getInt("id")+
                                ", name: "+json_data.getString("name")+
                                ", sex: "+json_data.getInt("sex")+
                                ", birthyear: "+json_data.getInt("birthyear")
                );
            }

    }catch(JSONException e){
        Log.e("log_tag", "Error parsing data "+e.toString());
    }

*/
    return i;

    }

    public String connect (){
return null;
    }




}


/**
 Connection dbCon;
 try {
 Class.forName("com.mysql.jdbc.Driver");
 dbCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/databasename?user=root&amp;password=rootpass&amp;autoReconnect=true");

 } catch (ClassNotFoundException e) {
 //
 e.printStackTrace();
 } catch (SQLException e) {
 //
 e.printStackTrace();
 }

 String query = "TRUNCATE TABLE tablename;";

 try {
 Statement stmt = dbCon.createStatement();
 stmt.execute(query);
 } catch (SQLException e) {
 //
 e.printStackTrace();
 }
 */