package AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.util.LinkedList;

import company.DBAdapter;
import exceptions.SBSBaseException;
import imports.AttachmentBase;
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
            RemoteEntry remoteEntry;
            for (int i = 0; experiments.size() > i; i++) {
                LinkedList<Entry_id_timestamp> entry_id_timestamps = SDS.get_last_entry_references(experiments.get(i).get_id(), 10, null);
                for (int j = 0; entry_id_timestamps.size() > j; j++) {
                    remoteEntry = SDS.get_entry(entry_id_timestamps.get(j));
                    Log.d("Attachmentconent1", remoteEntry.getAttachment().getContent().toString());
                    entries.add(remoteEntry);
                   Log.d("Attachmentcontent2", entries.get(j).getAttachment().getContent().toString());
                }
            }

            myDb.open();
            for (RemoteProject project : projects) {
                myDb.insertRemoteProject(project);
            }

            for (RemoteExperiment experiment : experiments) {
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


