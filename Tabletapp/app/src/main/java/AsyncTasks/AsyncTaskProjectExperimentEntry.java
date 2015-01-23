package AsyncTasks;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import company.DBAdapter;
import company.ExperimentEntry;
import company.ProjectExperimentEntry;
import company.Start;
import exceptions.SBSBaseException;
import imports.AttachmentTable;
import imports.AttachmentText;
import imports.Experiment;
import imports.LocalEntry;
import imports.Project;
import imports.User;
import scon.RemoteProject;
import scon.ServerDatabaseSession;

/**
 * Created by Grit on 17.12.2014.
 */
public class AsyncTaskProjectExperimentEntry extends AsyncTask<DBAdapter,Integer, ArrayList<ProjectExperimentEntry>> {
    @Override

    protected ArrayList<ProjectExperimentEntry> doInBackground(DBAdapter... params) {
        ArrayList<ProjectExperimentEntry> projectExperimentEntries = new ArrayList<ProjectExperimentEntry>();
        DBAdapter myDb = params[0];
        try {
            myDb.open();
            LinkedList<Project> remoteProject_list = displayProjects(myDb.getAllProjectRows());
            LinkedList<Experiment> remoteExperiment_list = displayExperiments(myDb.getAllExperimentRows());
            LinkedList<LocalEntry> remoteEntry_list = displayEntries(myDb.getAllEntryRows());
            myDb.close();
            ArrayList<LocalEntry> entries;


            for (int i = 0; i < remoteProject_list.size(); i++) {
                ArrayList<ExperimentEntry> experimentEntries = new ArrayList<ExperimentEntry>();
                projectExperimentEntries.add(new ProjectExperimentEntry((remoteProject_list.get(i)), experimentEntries));
            }

            for (int i = 0; i < remoteExperiment_list.size(); i++) {

                for (int j = 0; j < remoteProject_list.size(); j++) {

                    if (projectExperimentEntries.get(j).getProject().get_id().equals(remoteExperiment_list.get(i).get_project_id())) {
                        entries = new ArrayList<LocalEntry>();
                        projectExperimentEntries.get(j).getExperimentEntry().add(new ExperimentEntry(remoteExperiment_list.get(i), entries));
                        break;
                    }
                }
            }

            for (int i = 0; i < remoteEntry_list.size(); i++) {
                int ID = 0;
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

    private LinkedList<LocalEntry> displayEntries(Cursor cursor) {
        try {

            LinkedList<LocalEntry> entries;
            entries = new LinkedList<LocalEntry>();
            // populate the message from the cursor

            // Reset cursor to start, checking to see if there's data:
            if (cursor.moveToFirst()) {
                do {
                    // Process the data:
                    switch (cursor.getInt(DBAdapter.COL_EntryTyp)) {
                        case 1:
                            if (cursor.getInt(DBAdapter.COL_EntrySync) == 1)
                                entries.add(new LocalEntry(cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getLong(DBAdapter.COL_EntryCreationDate), new User(cursor.getString(DBAdapter.COL_EntryUserID)), true, cursor.getInt(DBAdapter.COL_EntryID), cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntrySync), cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                            else
                                entries.add(new LocalEntry(cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getLong(DBAdapter.COL_EntryCreationDate), new User(cursor.getString(DBAdapter.COL_EntryUserID)), false, cursor.getInt(DBAdapter.COL_EntryID), cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntrySync), cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                            break;
                        case 2:
                            if (cursor.getInt(DBAdapter.COL_EntrySync) == 1)
                                entries.add(new LocalEntry(cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getLong(DBAdapter.COL_EntryCreationDate), new User(cursor.getString(DBAdapter.COL_EntryUserID)), true, cursor.getInt(DBAdapter.COL_EntryID), cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntrySync), cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                            else
                                entries.add(new LocalEntry(cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getLong(DBAdapter.COL_EntryCreationDate), new User(cursor.getString(DBAdapter.COL_EntryUserID)), false, cursor.getInt(DBAdapter.COL_EntryID), cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntrySync), cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                            break;
                    }

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

