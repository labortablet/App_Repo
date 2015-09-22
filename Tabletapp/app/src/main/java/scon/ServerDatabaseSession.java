package scon;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.utils.Base64;

//FIXME this is only needed as we are also debugging on pc
//so I just added a copy of the android base64 file to our source for now
//should be removed once we ship this.

import android.util.Log;

import org.json_pc.JSONArray;
import org.json_pc.JSONException;
import org.json_pc.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import datastructures.AttachmentBase;
import datastructures.User;
import exceptions.*;


public class ServerDatabaseSession {
    private String session_id;
    private Boolean session_id_set;
    private User user;
    private URL database_url;
    private byte[] salt;


    public ServerDatabaseSession(URL database_url, User user) {
        this.database_url = database_url;
        this.user = user;
        this.session_id_set = Boolean.FALSE;
        this.salt = null;
    }

    private byte[] uni2bin(String uni) {
        return Base64.decode(uni.trim(), Base64.DEFAULT);
    }

    private String bin2uni(byte[] bin) {
        return Base64.encodeToString(bin, Base64.DEFAULT).trim();
    }

    private JSONObject send_json(JSONObject message) throws SBSBaseException {
        return this.send_json(message, 10000, 15000);
    }

    private JSONObject send_json(JSONObject message, Integer ReadTimeout, Integer ConnectTimeout) throws SBSBaseException {
        //transform JSONObject to a byte string
        byte[] message_bytes = null;
        try {
            message_bytes = message.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not available, this should really not happen");
        }

        String response_string = null;
        try {
            HttpsURLConnection conn = (HttpsURLConnection) this.database_url.openConnection();
            conn.setReadTimeout(ReadTimeout);
            conn.setConnectTimeout(ConnectTimeout);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message_bytes.length);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message_bytes);
            os.flush();
            os.close();
            InputStream is = conn.getInputStream();
            response_string = new Scanner(is, "UTF-8").useDelimiter("\\A").next().trim();
            is.close();
            conn.disconnect();
        } catch (SSLHandshakeException e) {
            throw new NoValidSSLCert();
        } catch (IOException e) {
            System.out.println(e);
            throw new NoServerConnectionException();
        }
        //maybe we should return a JSONObject with success:failed here and add the string as a parameter
        if (response_string.startsWith("<html>")) {
            System.out.println(response_string);
            throw new ServerSideException();
        }
        try {
            return new JSONObject(response_string);
        } catch (JSONException e) {
            System.out.println(response_string);
            System.out.println(e);
            throw new ErroneousResponse();
        }
    }

    private byte[] calculate_response(byte[] challenge)
    {
        MessageDigest sha256 = null;
        byte[] pw_hash = this.user.getPw_hash();
        byte[] inner_hash = new byte[pw_hash.length + this.salt.length];
        System.arraycopy(this.salt, 0, inner_hash, 0, this.salt.length);
        System.arraycopy(pw_hash, 0, inner_hash, this.salt.length, pw_hash.length);

        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        }catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-265 not available, this should really not happen");
        }
        sha256.reset();
        byte[] pw_hash_salted = sha256.digest(inner_hash);
        byte[] outer_hash = new byte[challenge.length + pw_hash_salted.length];
        System.arraycopy(challenge, 0, outer_hash, 0, challenge.length);
        System.arraycopy(pw_hash_salted, 0, outer_hash, challenge.length, pw_hash_salted.length);
        sha256.reset();
        return sha256.digest(outer_hash);

    }

    private void check_for_session() throws NoSession {
        if (!this.session_id_set) {
            //we need a session id before we try to get projects
            throw new NoSession();
        }

    }

    private void check_for_success(JSONObject result) throws SBSBaseException {
        //check if we succeeded
        //TODO: Kostet 8 ms rechenzeit warum keine ahnung
        try {
            if (!result.getString("status").toLowerCase().equals("success")) {
                System.out.println(result);
                throw new NoSuccess();
            }

        } catch (JSONException e) {
            System.out.println("status string not found while checking for success of the request");
            System.out.println(result);
            throw new ErroneousResponse();
        }
    }

    private JSONObject put_wrapper(JSONObject obj, String name, String value) {
        try {
            obj.put(name, value);
        } catch (JSONException e) {
            throw new RuntimeException("This should never happen, put wrapper");
        }
        return obj;
    };

    private JSONObject put_wrapper(JSONObject obj, String name, Number value) {
        try {
            obj.put(name, value);
        } catch (JSONException e) {
            throw new RuntimeException("This should never happen, put wrapper");
        }
        return obj;
    };

    private JSONObject send_action_after_auth_and_get_result(String action) throws SBSBaseException {
        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", action);
        this.put_wrapper(request, "session_id", this.session_id);
        JSONObject result = this.send_json(request);
        this.check_for_success(result);
        return result;
    }

    private byte[] get_challenge() throws SBSBaseException {
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_challenge");
        this.put_wrapper(request, "username", this.user.getUser_email());
        JSONObject result = null;
        result = this.send_json(request);
        try {
            this.session_id = result.getString("session_id");
            this.session_id_set = Boolean.TRUE;
        }catch (JSONException e){
            System.out.println("Challenge: no id!");
            System.out.println(result);
            throw new JSONError();
        }
        try{
            this.salt = this.uni2bin(result.getString("salt").trim());
        }catch (JSONException e){
            System.out.println("Challenge: could not decode the salt");
            throw new JSONError();
        }
        try{
            return this.uni2bin(result.getString("challenge"));
        }catch (JSONException e){
            System.out.println("Challenge: could not decode the challenge");
            throw new JSONError();
        }
    }

    private void auth_session(byte[] response) throws SBSBaseException{

        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "auth_session");
        this.put_wrapper(request, "session_id", this.session_id);
        this.put_wrapper(request, "response", bin2uni(response));
        JSONObject result = this.send_json(request);
        this.check_for_success(result);
        }

    public void start_session() throws SBSBaseException{
        //byte[] challenge = this.get_challenge();
        //byte[] response = this.calculate_response(challenge);
        this.auth_session(this.calculate_response(this.get_challenge()));
    };

    public LinkedList<RemoteProject> get_projects() throws SBSBaseException {

        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_projects");
        this.put_wrapper(request, "session_id", this.session_id);
        JSONObject result = this.send_json(request);
        this.check_for_success(result);
        JSONArray project_json_array = null;
        // getestet kein geschwindigkeits verlust bis hier
        try {
            project_json_array = result.getJSONArray("projects");
        } catch (JSONException e) {
            System.out.println("no projects json array");
            System.out.println(project_json_array);
            throw new JSONError();
        }

        LinkedList<RemoteProject> remoteProject_list = new LinkedList<RemoteProject>();
        int len = project_json_array.length();
        for (int i = 0; i < len; i++) {
            JSONArray project_json;
            Long id;
            String name;
            String description;
           // Long date = null;
            try {
                project_json = project_json_array.getJSONArray(i);
                id = project_json.getLong(0);
                name = project_json.getString(1);
                description = project_json.getString(2);
                //date = project_json.getLong(3);
                remoteProject_list.add(new RemoteProject(id, name, description, 0));
            } catch (JSONException e) {
                System.out.println("Some project did not decode properly");
                System.out.println(project_json_array);
                throw new JSONError();
            }

        }
        return remoteProject_list;
    }


    public LinkedList<RemoteExperiment> get_experiments() throws SBSBaseException {
        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_experiments");
        this.put_wrapper(request, "session_id", this.session_id);
        JSONObject result = this.send_json(request);
        this.check_for_success(result);

        JSONArray experiment_json_array = null;
        try {
            experiment_json_array = result.getJSONArray("experiments");
        } catch (JSONException e) {
            System.out.println("No array of Experiments found");
            System.out.println(result);
            System.out.println(e);
            throw new JSONError();
        }
        LinkedList<RemoteExperiment> remoteExperiment_list = new LinkedList<RemoteExperiment>();
        for (int i = 0; i < experiment_json_array.length(); i++) {
            JSONArray experiment_json = null;
            Long project_id = null;
            Long id = null;
            String name = null;
            String description = null;
            Long date = null;
            try {
                experiment_json = experiment_json_array.getJSONArray(i);
                project_id = experiment_json.getLong(0);
                id = experiment_json.getLong(1);
                name = experiment_json.getString(2);
                description = experiment_json.getString(3);
                //date = experiment_json.getLong(4);
                remoteExperiment_list.add(new RemoteExperiment(id, project_id, name, description, 0));
            } catch (JSONException e) {
                System.out.println("getting remote Experiments caused exception!");
                System.out.println(e);
                //some project did not decode correctly
                throw new JSONError();
            }

        }
        return remoteExperiment_list;
    }

    public LinkedList<Entry_Remote_Identifier> get_last_entry_references(long experiment_id, long entry_count, LinkedList<Entry_Remote_Identifier> excluded) throws SBSBaseException {
        //excluded is ignored right now but in the future, you will be able
        //to list entries you do not like to be shown so you only will
        //see entries which you do not know.
        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_last_entry_ids");
        this.put_wrapper(request, "session_id", this.session_id);
        this.put_wrapper(request, "entry_count", entry_count);
        this.put_wrapper(request, "experiment_id", experiment_id);

        JSONObject result = this.send_json(request);
        this.check_for_success(result);

        JSONArray entry_id_timestamps = null;
        try {
            entry_id_timestamps = result.getJSONArray("entry_id_timestamps");
        } catch (JSONException e) {
            System.out.println("getting last entry references!");
            System.out.println(e);
            throw new JSONError();
        }
        LinkedList<Entry_Remote_Identifier> entry_references = new LinkedList<Entry_Remote_Identifier>();

        for (int i = 0; i < entry_id_timestamps.length(); i++) {
            JSONArray id_timestamp = entry_id_timestamps.getJSONArray(i);
            Integer id = id_timestamp.getInt(0);
            Long timestamp = id_timestamp.getLong(1);
            entry_references.add(new Entry_Remote_Identifier(id, timestamp));
        }
        return entry_references;
    }

    public boolean check_auth() throws SBSBaseException{
        this.check_for_session();
        JSONObject result = send_action_after_auth_and_get_result("check_auth");
        this.check_for_success(result);
        return result.getBoolean("auth");
    };

    public Entry_Remote_Identifier send_entry(long experiment_id, long entry_time, String title, AttachmentBase attachment) throws SBSBaseException{
        this.check_for_session();
        //only text right now
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "send_entry");
        this.put_wrapper(request, "session_id", this.session_id);
        this.put_wrapper(request, "title", title);
        this.put_wrapper(request, "date_user", entry_time);
        this.put_wrapper(request, "attachment", attachment.serialize());
        this.put_wrapper(request, "attachment_type", attachment.getTypeNumber());
        this.put_wrapper(request, "experiment_id", experiment_id);
        JSONObject result = this.send_json(request);
        this.check_for_success(result);
        Long entry_current_time;
        Integer entry_id;
        try {
            entry_current_time = result.getLong("entry_current_time");
            entry_id = result.getInt("entry_id");
        } catch (JSONException e) {
            System.out.println("Exception while analyzing data after sending an entry");
            System.out.println(result);
            throw new JSONError();
        }
        return new Entry_Remote_Identifier(entry_id, entry_current_time);
    }

    public RemoteEntry get_entry(Entry_Remote_Identifier a) throws SBSBaseException{
        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_entry");
        this.put_wrapper(request, "session_id", this.session_id);
        this.put_wrapper(request, "entry_id", a.getId());
        this.put_wrapper(request, "entry_change_time", a.getLast_change());
        JSONObject result = this.send_json(request);
        this.check_for_success(result);
        Long entry_time = result.getLong("entry_date_user");
        Long sync_time = result.getLong("entry_date");
        Long change_time = result.getLong("entry_current_time");
        long experiment_id = result.getLong("experiment_id");
        int attachment_type = result.getInt("entry_attachment_type");
        String attachment_serialized = result.getString("entry_attachment");
        String title = result.getString("entry_title");
        String user_firstname = result.getString("user_firstname");
        long user_id = result.getLong("user_id");
        String user_lastname = result.getString("user_lastname");
        return new RemoteEntry(a.getId(), experiment_id, title, attachment_serialized, attachment_type, sync_time, entry_time, change_time, user_id, user_firstname, user_lastname);
    }



}