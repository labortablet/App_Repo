package AsyncTasks;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

import imports.DBAdapter;
import datastructures.Entry;
import datastructures.ExperimentEntry;
import datastructures.ProjectExperimentEntry;
import datastructures.AttachmentBase;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.User;

/**
 * Created by Grit on 17.12.2014.
 */
public class AsyncTaskProjectExperimentEntry extends AsyncTask<DBAdapter,Integer, ArrayList<ProjectExperimentEntry>> { // object to get,Status,Object to return
    @Override

    protected ArrayList<ProjectExperimentEntry> doInBackground(DBAdapter... params) {
        ArrayList<ProjectExperimentEntry> projectExperimentEntries = new ArrayList<ProjectExperimentEntry>();
        DBAdapter myDb = params[0];
        try {
            myDb.open();
            LinkedList<Project> remoteProject_list = displayProjects(myDb.getAllProjectRows());
            LinkedList<Experiment> remoteExperiment_list = displayExperiments(myDb.getAllExperimentRows());
            LinkedList<Entry> remoteEntry_list = displayEntries(myDb.getAllEntryRows());
            myDb.close();
            ArrayList<Entry> entries;


            for (int i = 0; i < remoteProject_list.size(); i++) {
                ArrayList<ExperimentEntry> experimentEntries = new ArrayList<ExperimentEntry>();
                projectExperimentEntries.add(new ProjectExperimentEntry((remoteProject_list.get(i)), experimentEntries));
            }

            for (int i = 0; i < remoteExperiment_list.size(); i++) {

                for (int j = 0; j < remoteProject_list.size(); j++) {

                    if (projectExperimentEntries.get(j).getProject().get_id().equals(remoteExperiment_list.get(i).get_project_id())) {
                        entries = new ArrayList<Entry>();
                        projectExperimentEntries.get(j).getExperimentEntry().add(new ExperimentEntry(remoteExperiment_list.get(i), entries));
                        break;
                    }
                }
            }

            for (int i = 0; i < remoteEntry_list.size(); i++) {
                for (int j = 0; j < projectExperimentEntries.size(); j++) {
                    for (int k = 0; k < projectExperimentEntries.get(j).getExperimentEntry().size(); k++) {
                        if (projectExperimentEntries.get(j).getExperimentEntry().get(k).getExperiments().get_id().equals(remoteEntry_list.get(i).getExperiment_id())) {
                            projectExperimentEntries.get(j).getExperimentEntry().get(k).getEntriesList().add(remoteEntry_list.get(i));
                            Log.d("Entry" + k,remoteEntry_list.get(i).getTitle());
                            break;

                        }
                    }
                }
            }


        } catch (NullPointerException e) {
            return null;

        }
        return projectExperimentEntries;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public interface TaskListener {
        public Integer onFinished(Integer result);
    }

    private LinkedList<Project> displayProjects(Cursor cursor) {
        try {

            LinkedList<Project> remoteProjects = new LinkedList<Project>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    remoteProjects.add(new Project(cursor.getInt(DBAdapter.COL_ProjectID), cursor.getInt(DBAdapter.COL_ProjectRemoteID), cursor.getString(DBAdapter.COL_ProjectName), cursor.getString(DBAdapter.COL_ProjectDescription)));

                } while (cursor.moveToNext());
            }
            // Close the cursor to avoid a resource leak.
            cursor.close();
            return remoteProjects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private LinkedList<Experiment> displayExperiments(Cursor cursor) {
        try {

            LinkedList<Experiment> experiments = new LinkedList<Experiment>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    experiments.add(new Experiment(cursor.getInt(DBAdapter.COL_ExperimentID), cursor.getInt(DBAdapter.COL_ExperimentRemoteID), cursor.getInt(DBAdapter.COL_ExperimentProjectID), cursor.getString(DBAdapter.COL_ExperimentName), cursor.getString(DBAdapter.COL_ExperimentDescription)));

                } while (cursor.moveToNext());
            }
            // Close the cursor to avoid a resource leak.
            cursor.close();
            return experiments;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private LinkedList<Entry> displayEntries(Cursor cursor) {
        try {

            LinkedList<Entry> entries;
            entries = new LinkedList<Entry>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    entries.add(
                            new Entry(cursor.getInt(DBAdapter.COL_EntryID),
                                    new User(cursor.getString(DBAdapter.COL_EntryUserID)),
                                    cursor.getInt(DBAdapter.COL_EntryExperimentID),
                                    cursor.getString(DBAdapter.COL_EntryTitle),
                                    AttachmentBase.deserialize(cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getString(DBAdapter.COL_EntryContent)),
                                    cursor.getInt(DBAdapter.COL_EntrySync) == 1,
                                    cursor.getLong(DBAdapter.COL_EntryCreationDate),
                                    cursor.getLong(DBAdapter.COL_EntrySync),
                                    cursor.getLong(DBAdapter.COL_EntryChangeDate)));

                } while (cursor.moveToNext());
            }
            // Close the cursor to avoid a resource leak.
            cursor.close();
            return entries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





}

