package AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.LinkedList;

import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import imports.DBAdapter;
import exceptions.SBSBaseException;
import imports.ServersideDatabaseConnectionObject;
import datastructures.Entry_Remote_Identifier;
import datastructures.RemoteEntry;
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
        LinkedList<Project> projects;
        LinkedList<Experiment> experiments;
        LinkedList<Entry> entries = new LinkedList<Entry>();
        try {
            SDS.start_session();
            projects = SDS.get_projects();
            experiments = SDS.get_experiments();
            Entry remoteEntry;
            for (int i = 0; experiments.size() > i; i++) {
                LinkedList<Entry_Remote_Identifier> entry_remoteIdentifiers = SDS.get_last_entry_references(experiments.get(i).get_id(), 10, null);
                for (int j = 0; entry_remoteIdentifiers.size() > j; j++) {
                    remoteEntry = SDS.get_entry(entry_remoteIdentifiers.get(j));
                    Log.d("Attachmentconent1", remoteEntry.getAttachment().getContent().toString());
                    entries.add(remoteEntry);
                   Log.d("Attachmentcontent2", entries.get(j).getAttachment().getContent().toString());
                }
            }

            myDb.open();
            for (Project project : projects) {
                myDb.insertRemoteProject(project);
            }

            for (Experiment experiment : experiments) {
                myDb.insertRemoteExperiment(experiment);
            }

            for (int i = 0; i <entries.size();i++)
            {
               // Log.d("Titel",entries.get(i).getTitle());
                entries.get(i).getAttachment();
                myDb.insertRemoteEntry(entries.get(i).getTitle(), entries.get(i).getAttachment().getTypeNumber(), entries.get(i).getAttachment().getContent().toString(), entries.get(i).getExperiment_id(), entries.get(i).getUser().getUser_email(),  entries.get(i).getEntry_time(), entries.get(i).getSync_time(), entries.get(i).getChange_time());
            }
       //     for (RemoteEntry entry : entries) {
       //     myDb.insertRemoteEntry(entry.getTitle(),entry.getAttachment().getTypeNumber(), entry.getAttachment().getContent().toString(),entry.getExperiment_id(), entry.getUser().getUser_email(),entry.getRemote_id(), entry.getEntry_time(), entry.getSync_time(), entry.getChange_time());
         //       Log.d("attachment",entry.getAttachment().getContent().toString());
 // myDb.insertRemoteEntry(entry,entry.getAttachment().getTypeNumber());
      //      }
            myDb.close();
            return 0;

    } catch (SBSBaseException e) {
            e.printStackTrace();
           return 3;
        }}



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


