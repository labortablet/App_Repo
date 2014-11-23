package scon;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.utils.Base64;

//FIXME this is only needed as we are also debugging on pc
//so I just added a copy of the android base64 file to our source for now
//should be removed once we ship this.

import org.json_pc.JSONArray;
import org.json_pc.JSONException;
import org.json_pc.JSONObject;

import imports.BaseEntry;
import scon.Base64;

import imports.User;
import scon.Entry_id_timestamp;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

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
            throw new NoServerConnectionException();
        }

        //maybe we should return a JSONObject with success:failed here and add the string as a parameter
        if (response_string.startsWith("<html>")) {
            throw new ServerSideException();
        }


        try {
            return new JSONObject(response_string);
        } catch (JSONException e) {
            throw new ErroneousResponse();
        }
    }

    private byte[] calculate_response(byte[] challenge)
    {
        MessageDigest sha256 = null;
        byte[] pw_hash = this.user.getPw_hash();
        byte[] result1 = new byte[pw_hash.length + this.salt.length];
        System.arraycopy(this.salt, 0, result1, 0, this.salt.length);
        System.arraycopy(pw_hash, 0, result1, this.salt.length, pw_hash.length);

        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        }catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-265 not available, this should really not happen");
        }
        sha256.reset();
        byte[] inner_hash = sha256.digest(result1);
        byte[] result2 = new byte[challenge.length + inner_hash.length];
        System.arraycopy(challenge, 0, result2, 0, challenge.length);
        System.arraycopy(result1, 0, result2, challenge.length, result1.length);
        sha256.reset();
        return sha256.digest(result2);
    }

    private void check_for_session() throws NoSession {
        if (!this.session_id_set) {
            //we need a session id before we try to get projects
            throw new NoSession();
        }
    }

    private void check_for_success(JSONObject result) throws SBSBaseException {
        //check if we succeeded
        try {
            if (!result.getString("status").toLowerCase().equals("success")) {
                throw new NoSuccess();
            }

        } catch (JSONException e) {
            throw new ErroneousResponse();
        }
    }

    private JSONObject put_wrapper(JSONObject obj, String name, String value) {
        try {
            obj.put(name, value);
        } catch (JSONException e) {
            //should be impossible as we add a valid parameter to the json
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
        System.out.println("Creating JSON Object for challenge");
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_challenge");
        this.put_wrapper(request, "username", this.user.getUser_id());
        System.out.println("Object is ready and send");
        JSONObject result = null;
        result = this.send_json(request);
        System.out.println("got result");
        System.out.println(result);
        try {
            this.session_id = result.getString("session_id");
            this.session_id_set = Boolean.TRUE;
        }catch (JSONException e){
            throw new SBSBaseException();
        }
        System.out.println("Got session_id");
        try{
            this.salt = this.uni2bin(result.getString("salt").trim());
        }catch (JSONException e){
            throw new SBSBaseException();
        }
        System.out.println("Got salt");
        try{
            return this.uni2bin(result.getString("challenge"));
        }catch (JSONException e){
            throw new SBSBaseException();
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
        byte[] challenge = this.get_challenge();
        byte[] response = this.calculate_response(challenge);
        this.auth_session(response);
    };

    public LinkedList<RemoteProject> get_projects() throws SBSBaseException {
        this.check_for_session();
        JSONObject request = new JSONObject();
        this.put_wrapper(request, "action", "get_projects");
        this.put_wrapper(request, "session_id", this.session_id);
        JSONObject result = this.send_json(request);
        this.check_for_success(result);

        JSONArray project_json_array = null;
        try {
            project_json_array = result.getJSONArray("projects");
        } catch (JSONException e) {
            throw new SBSBaseException();
        }

        LinkedList<RemoteProject> remoteProject_list = new LinkedList<RemoteProject>();
        for (int i = 0; i < project_json_array.length(); i++) {
            JSONArray project_json = null;
            Integer id = null;
            String name = null;
            String description = null;
            try {
                project_json = project_json_array.getJSONArray(i);
                id = project_json.getInt(0);
                name = project_json.getString(1);
                description = project_json.getString(2);
                remoteProject_list.add(new RemoteProject(id, name, description));
            } catch (JSONException e) {
                //some project did not decode correctly
                throw new SBSBaseException();
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
            throw new SBSBaseException();
        }
        LinkedList<RemoteExperiment> remoteExperiment_list = new LinkedList<RemoteExperiment>();
        for (int i = 0; i < experiment_json_array.length(); i++) {
            JSONArray experiment_json = null;
            Integer project_id = null;
            Integer id = null;
            String name = null;
            String description = null;
            try {
                experiment_json = experiment_json_array.getJSONArray(i);
                project_id = experiment_json.getInt(0);
                id = experiment_json.getInt(1);
                name = experiment_json.getString(2);
                description = experiment_json.getString(3);
                remoteExperiment_list.add(new RemoteExperiment(project_id, id, name, description));
            } catch (JSONException e) {
                //some project did not decode correctly
                throw new SBSBaseException();
            }

        }
        return remoteExperiment_list;
    }

    public Integer sendEntry(BaseEntry Entry) throws SBSBaseException{
        return 0;
    }

    public Entry_id_timestamp[] get_last_entry_references(Integer session_id, Integer experiment_id, Integer entry_count) throws SBSBaseException {
        this.check_for_session();
        JSONObject request = new JSONObject();
        System.out.println("Success!");
        this.put_wrapper(request, "action", "get_last_entry_ids");
        this.put_wrapper(request, "session_id", this.session_id);
        this.put_wrapper(request, "entry_count", entry_count.toString());
        JSONObject result = this.send_json(request);
        this.check_for_success(result);

        JSONArray entry_id_list = null;
        try {
            entry_id_list = result.getJSONArray("entry_ids");
        } catch (JSONException e) {
            throw new SBSBaseException();
        }
        Entry_id_timestamp[] entry_references = null;
        //FIXME this needs to be changed as we changed the return type
        //entry_references = new Integer[entry_id_list.length()];
        //for (int i = 0; i < entry_id_list.length(); i++) {
          //  entry_ids[i] = Integer(entry_id_list[i]);
        //}
        return entry_references;
    }
}