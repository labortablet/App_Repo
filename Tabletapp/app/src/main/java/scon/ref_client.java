package scon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import exceptions.SBSBaseException;

public class ref_client {
	public static void main(String[] args) {
		System.out.println("Starting Test!");
		URL url = null;
		try {
			url = new URL("https://lablet.vega.uberspace.de/scon/db.cgi");
			//url = new URL("https://lablet.vega.uberspace.de/scon/json_bounce.cgi");
		} catch (MalformedURLException e) {
			System.exit(1);
		};
        System.out.println("URL nor malformed");
		String user = "fredi@uni-siegen.de";
		String pw_h = "test";
        System.out.println("Username and PW set");
		ServerDatabaseSession SDS = new ServerDatabaseSession(url, user, pw_h);
		try {
            SDS.start_session();
            LinkedList<RemoteProject> remoteProject_list = SDS.get_projects();
			LinkedList<RemoteExperiment> remoteExperiment_list = SDS.get_experiments();
			System.out.println(remoteProject_list);
			System.out.println(remoteExperiment_list);
		} catch (SBSBaseException e) {
			System.out.println(e);
		};
	};
};
