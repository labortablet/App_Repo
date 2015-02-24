package scon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import exceptions.SBSBaseException;
import imports.AttachmentBase;
import imports.AttachmentTable;
import imports.AttachmentText;
import imports.User;

public class ref_client {
    public static void main(String[] args){
        ref_client.main();
    }
	public static void main() {
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
            System.out.println("Check auth status of session");
            System.out.println(SDS.check_auth());
            System.out.println("Session status checked");
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
            System.out.println("Try sending an text entry");
            Entry_id_timestamp new_entry_info;
            AttachmentBase attachment = new AttachmentText("Test Text Entry");
            new_entry_info = SDS.send_entry(remoteExperiment_list.getFirst().get_id(), new Long(10), "Ref Client Test Entry Text", attachment);
            System.out.println(new_entry_info.getId());
            System.out.println(new_entry_info.getLast_change());
            System.out.println("Try sending an table entry");
            attachment = new AttachmentTable("Test Table Entry");
            new_entry_info = SDS.send_entry(remoteExperiment_list.getFirst().get_id(), new Long(20), "Ref Client Test Entry Table", attachment);
            System.out.println(new_entry_info.getId());
            System.out.println(new_entry_info.getLast_change());
            System.out.println("Entries send");
            System.out.println("Getting Entries");
            RemoteEntry a;
            AttachmentBase c;
            for (Entry_id_timestamp b: remoteEntry_list) {
                a = SDS.get_entry(b);
                System.out.println(a.getRemote_id());
                System.out.println(a.getTitle());
                c = a.getAttachment();
                System.out.println(c.getContent());
            }
        } catch (SBSBaseException e) {
			System.out.println(e);
		};
	};
};
