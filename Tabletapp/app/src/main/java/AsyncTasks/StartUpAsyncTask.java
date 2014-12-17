package AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import company.DBAdapter;
import company.ProjectExperimentEntry;
import company.Start;
import exceptions.SBSBaseException;
import imports.ServersideDatabaseConnectionObject;
import scon.Entry_id_timestamp;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;
import scon.ServerDatabaseSession;

/**
 * Created by Grit on 21.10.2014.
 */
public class StartUpAsyncTask extends AsyncTask<ServersideDatabaseConnectionObject,String,Integer> {
    ProgressDialog progressDialog;
    @Override
    protected Integer doInBackground(ServersideDatabaseConnectionObject... params) {
        ServersideDatabaseConnectionObject SDCO = params[0];
        DBAdapter myDb = SDCO.getMyDB();
        ServerDatabaseSession SDS = SDCO.getSDS();
        LinkedList<RemoteProject> projects;
        LinkedList<RemoteExperiment> experiments;
        LinkedList<RemoteEntry> entries = new LinkedList<RemoteEntry>();
        try {
            SDS.start_session();
            projects = SDS.get_projects();
            experiments = SDS.get_experiments();
            //TODO: ADD Get Entries here!
       /*     for (int i = 0; experiments.size() >= i; i++) {
                LinkedList<Entry_id_timestamp> entry_id_timestamps = SDS.get_last_entry_references(experiments.get(i).get_id(), 10, null);
                for (int j = 0; entry_id_timestamps.size() >= j; j++) {
                    entries.add(SDS.get_entry(entry_id_timestamps.get(j)));
                }

            }*/
            myDb.open();
            for (RemoteProject project : projects) {
                myDb.insertRemoteProject(project);
            }

            for (RemoteExperiment experiment : experiments) {
                myDb.insertRemoteExperiment(experiment);
            }

            for (RemoteEntry entry : entries) {
                myDb.insertRemoteEntry(entry);
            }
            myDb.close();
            return 0;
        } catch (SBSBaseException e) {
            e.printStackTrace();
           return 3;
        }


    }
@Override
    protected void onPostExecute(Integer result) {
    super.onPostExecute(result);


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // progressDialog= ProgressDialog.show(, "Progress Dialog Title Text","Process Description Text", true);
 }
    protected void onProgressUpdate(String... progress) {

    }
    }


