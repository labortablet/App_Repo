package company;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import exceptions.SBSBaseException;
import imports.AttachmentBase;
import imports.AttachmentText;
import imports.User;
import scon.Entry_id_timestamp;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;
import scon.ServerDatabaseSession;

/**
 * Created by Grit on 15.12.2014.
 */
public class TestMethods {
    public static void main(String[] args) {
        System.out.println("Starting Test!");
        URL url = null;
        try {
            url = new URL("https://lablet.vega.uberspace.de/scon/db.cgi");
            //url = new URL("https://lablet.vega.uberspace.de/scon/json_bounce.cgi");
        } catch (MalformedURLException e) {
            System.exit(1);
        };
        System.out.println("URL not malformed");
        //these need to be set on server as well!
        String username = "fredi@uni-siegen.de";
        String password = "test";
        System.out.println("Username and PW set");
        User user = new User(username, password);
        ServerDatabaseSession SDS = new ServerDatabaseSession(url, user);
        try {
            System.out.println("Start session");
            SDS.start_session();
            System.out.println("Sesstion started");
            System.out.println("Get Remote Projects");
            LinkedList<RemoteProject> remoteProject_list = SDS.get_projects();
            System.out.println("Got Remote Projects");
            System.out.println("Get Remote Experiments");
            LinkedList<RemoteExperiment> remoteExperiment_list = SDS.get_experiments();
            System.out.println("Got Remote Experiments");
            System.out.println("Printing them");
            System.out.println(remoteProject_list);
            System.out.println(remoteExperiment_list);
            System.out.println("Get last Entrys list");
            LinkedList<Entry_id_timestamp> remoteEntry_list = SDS.get_last_entry_references(remoteExperiment_list.getFirst().get_project_id(), 10, null);
            System.out.println("Got last Entrys list");
            System.out.println(remoteEntry_list);

      //      Entry_id_timestamp new_entry_info;
           // AttachmentBase attachment = new AttachmentText("Hallo");

          // new_entry_info = SDS.send_entry(remoteExperiment_list.getFirst().get_id(), new Long(0), "Ref Client Test Entry", 0, attachment);

            RemoteEntry a;
            LinkedList<RemoteEntry> entries = new LinkedList<RemoteEntry>();
            LinkedList<Entry_id_timestamp> entry_id_timestamps;
            for (int i = 0; remoteExperiment_list.size() > i; i++) {
                entry_id_timestamps = SDS.get_last_entry_references(remoteExperiment_list.get(i).get_id(), 10, null);
                for (int j = 0; entry_id_timestamps.size() > j; j++) {
                    a = SDS.get_entry(remoteEntry_list.get(j));
                }}
        } catch (SBSBaseException e) {
            System.out.println(e);
        };
    };
};