package AsyncTasks;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import exceptions.SBSBaseException;
import scon.RemoteProject;
import scon.ServerDatabaseSession;

/**
 * Created by Grit on 17.12.2014.
 */
public class AsyncTaskNetworkAvaible extends AsyncTask<ServerDatabaseSession,Integer,HashMap<Integer,LinkedList<RemoteProject>>> {



    @Override
    protected HashMap<Integer, LinkedList<RemoteProject>> doInBackground(ServerDatabaseSession... params) {


        ServerDatabaseSession SDS = params[0];
        LinkedList<RemoteProject> projects;
        HashMap<Integer, LinkedList<RemoteProject>> remoteProjectMap = null;
        try {
            SDS.start_session();
            projects = SDS.get_projects();
            remoteProjectMap.put(0,projects);
            return remoteProjectMap;
                }


         catch (SBSBaseException e) {
            e.printStackTrace();
             remoteProjectMap.put(2,null);
            return remoteProjectMap;
        }

    }
    @Override
    protected void onPostExecute(HashMap<Integer,LinkedList<RemoteProject>> result) {
        super.onPostExecute(result);


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    public interface TaskListener {
        public Integer onFinished(Integer result);
    }

}
