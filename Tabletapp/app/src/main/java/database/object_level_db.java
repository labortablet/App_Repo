package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import datastructures.AttachmentBase;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.User;
import exceptions.SBSBaseException;
import scon.Entry_Remote_Identifier;
import scon.RemoteEntry;
import scon.RemoteExperiment;
import scon.RemoteProject;
import scon.RemoteUser;

public class object_level_db {
    private low_level_adapter db_helper;
    private SQLiteDatabase db;
    private boolean opened = false;

    private static final HashMap<Long, WeakReference<User>> user_object_cache = new HashMap<Long, WeakReference<User>>();
    private static final HashMap<Long, WeakReference<Project>> project_object_cache = new HashMap<Long, WeakReference<Project>>();
    private static final HashMap<Long, WeakReference<Experiment>> experiment_object_cache = new HashMap<Long, WeakReference<Experiment>>();
    private static final HashMap<Long, WeakReference<Entry>> entry_object_cache = new HashMap<Long, WeakReference<Entry>>();

    private Long resolve_remote_id_to_local_id(table table_for_resolve, long remote_id) {
        Cursor c;

        //c = db.rawQuery(" SELECT "+table_for_resolve.getField("id").getName()+" FROM " + table_for_resolve.getName() + " WHERE " + "remote_id"+ " = ?", new String[]{String.valueOf(remote_id)});

        c = this.db.rawQuery("SELECT " + table_for_resolve.getField("id").getName()
                + " FROM " + table_for_resolve.getName()
                + " WHERE "
                + table_for_resolve.getField("remote_id").getName() + "="
                + remote_id, null);
        for (int i = 0; i < c.getColumnNames().length; i++)


            if (c == null) {
                return null;
            }
        if (!c.moveToFirst())
            return null;
        c.moveToFirst();
        int index;

        index = c.getColumnIndex(table_for_resolve.getField("id").getName());
        return c.getLong(index);
    }

    private long insert_project(User user, RemoteProject project) {
   //     ContentValues initialValues = new ContentValues();
       // initialValues.put(layout.projects.getField("remote_id").getName(), project.getId());
      //  initialValues.put(layout.projects.getField("name").getName(), project.getName());
     //   initialValues.put(layout.projects.getField("user_id").getName(), user.getId());
    //    initialValues.put(layout.projects.getField("description").getName(), project.getDescription());
   //     initialValues.put(layout.projects.getField("date_creation").getName(), project.getDate_creation());

        SQLiteStatement sqLiteStatement = db.compileStatement("" + "INSERT INTO " +" projects " + " ( remote_id  ,name ,user_id , description ) " + "VALUES (?,?,?,?)");
        sqLiteStatement.bindLong(1, project.getId());
        sqLiteStatement.bindString(2, project.getName());
        sqLiteStatement.bindLong(3, user.getId());
        sqLiteStatement.bindString(4, project.getDescription());

        return sqLiteStatement.executeInsert();
        //return this.db.insert(layout.projects.getName(), null, initialValues);
    }

    private long update_project(long id, RemoteProject project) {
        ContentValues newValues = new ContentValues();
        newValues.put(layout.projects.getField("name").getName(), project.getName());
        newValues.put(layout.projects.getField("description").getName(), project.getDescription());
        newValues.put(layout.projects.getField("date_creation").getName(), project.getDate_creation());
        return db.update(layout.projects.getName(), newValues, "id=" + id, null);
    }

    /**
     * deleting projects
     * @return
     */
    private Cursor getProjectRow() {
        Cursor c = db.rawQuery(" SELECT id FROM "+ layout.projects.getName() +" WHERE 1",new String[]{});
        c.moveToFirst();
        return c;
    }

    public void deleteAllSyncedProjects(){
        Cursor c = getProjectRow();
        long rowId = c.getColumnIndexOrThrow(layout.projects.getField("id").getName());
        if (c.moveToFirst()) {
            do {
                deleteProjectByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();

    }
    private boolean deleteExperimentByID(long rowId) {
        String where =  "id" + " = " + rowId;
        return db.delete(layout.experiments.getName(), where, null) != 0;
    }

    private Cursor getExperimentRow() {
        Cursor c = db.rawQuery(" SELECT id FROM "+ layout.experiments.getName() +" WHERE 1",new String[]{});
        c.moveToFirst();
        return c;
    }

    public void deleteAllSyncedExperiments(){
        Cursor c = getExperimentRow();
        long rowId = c.getColumnIndexOrThrow(layout.experiments.getField("id").getName());
        if (c.moveToFirst()) {
            do {
                deleteExperimentByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();

    }
    private boolean deleteProjectByID(long rowId) {
        String where =  "id" + " = " + rowId;
        return db.delete(layout.projects.getName(), where, null) != 0;
    }

    private boolean deleteEntryByID(long rowId) {
        String where =  "id" + " = " + rowId;
        return db.delete(layout.entries.getName(), where, null) != 0;
    }

    private Cursor getEntryRow() {
        Cursor c = db.rawQuery(" SELECT id FROM "+ layout.entries.getName() +" WHERE 1",new String[]{});
        c.moveToFirst();
        return c;
    }

    public void deleteAllSyncedEntry(){
        Cursor c = getEntryRow();
        long rowId = c.getColumnIndexOrThrow(layout.entries.getField("id").getName());
        if (c.moveToFirst()) {
            do {
                deleteEntryByID(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();

    }
//**************

    public void insert_or_update_project(User user, RemoteProject project) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        //first get the local id if it exists
        Long id = this.resolve_remote_id_to_local_id(layout.projects, project.getId());
        long result;
        if (id == null) {
            //the remote project needs to be inserted


            result = this.insert_project(user, project);

            if (result == -1) {
                throw new RuntimeException("Could not insert a Remote Project, this should not happen");
            }
        } else {
            //the local project exists already and needs to be updated
            result = update_project(id, project);
            if (result == -1) {
                throw new RuntimeException("Could not update a Remote Project, this should not happen");
            }
            //it is now in the db, afterwards, check if it is already in memory
         //   WeakReference<Project> ref = project_object_cache.get(id);
          //  if (ref != null) {
           //     Project in_cache = ref.get();
            //    if (in_cache != null) {
             //       in_cache.update(project);
             //   }
           // }
            this.release_lock();
        }
    }

    private long insert_experiment(User user, RemoteExperiment experiment, long project_id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.experiments.getField("remote_id").getName(), experiment.getId());
        initialValues.put(layout.experiments.getField("user_id").getName(), user.getId());
        initialValues.put(layout.experiments.getField("name").getName(), experiment.getName());
        initialValues.put(layout.experiments.getField("description").getName(), experiment.getDescription());
        initialValues.put(layout.experiments.getField("date_creation").getName(), experiment.getDate_creation());
        initialValues.put(layout.experiments.getField("project_id").getName(), project_id);
        return this.db.insert(layout.experiments.getName(), null, initialValues);
    }

    private long update_experiment(long id, RemoteExperiment experiment, long project_id) {
        ContentValues newValues = new ContentValues();
        newValues.put(layout.experiments.getField("name").getName(), experiment.getName());
        newValues.put(layout.experiments.getField("description").getName(), experiment.getDescription());
        newValues.put(layout.experiments.getField("date_creation").getName(), experiment.getDate_creation());
        newValues.put(layout.experiments.getField("project_id").getName(), project_id);
        return db.update(layout.experiments.getName(), newValues, "id=" + id, null);
    }

    public void insert_or_update_experiment(User user, RemoteExperiment experiment) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        //first get the local id if it exists
        Long id = this.resolve_remote_id_to_local_id(layout.experiments, experiment.getId());
        Long tmp_project_id = this.resolve_remote_id_to_local_id(layout.projects, experiment.getProject_id());
        if (tmp_project_id == null) {
            throw new RuntimeException("Experiment without a parent Project detected. This should never happen.");
        }
        long project_id = tmp_project_id;
        long result;
        if (id == null) {
            //the remote experiment needs to be inserted
            //TODO find out why this bug happens  Caused by: java.lang.NullPointerException
            //     at database.object_level_db.insert_experiment(object_level_db.java:119)
            //   at database.object_level_db.insert_or_update_experiment(object_level_db.java:146)
            System.out.println("Debug info for crash in insert experiment");
            System.out.println(user);
            System.out.println(experiment);
            System.out.println(project_id);
            result = this.insert_experiment(user, experiment, project_id);
            if (result == -1) {
                throw new RuntimeException("Could not insert a Remote Experiment, this should not happen");
            }
        } else {
            //the local experiment exists already and needs to be updated
            result = update_experiment(id, experiment, project_id);
            if (result == -1) {
                throw new RuntimeException("Could not update a Remote Experiment, this should not happen");
            }
            //it is now in the db, afterwards, check if it is already in memory
            WeakReference<Experiment> ref = experiment_object_cache.get(id);
            if (ref != null) {
                Experiment in_cache = ref.get();
                if (in_cache != null) {
                    in_cache.update(experiment, project_id);
                }
            }
            this.release_lock();
        }
    }

    private long insert_user(RemoteUser user) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.users.getField("firstname").getName(), user.getFirstname());
        initialValues.put(layout.users.getField("lastname").getName(), user.getLastname());
        initialValues.put(layout.users.getField("remote_id").getName(), user.getId());
        return this.db.insert(layout.users.getName(), null, initialValues);
    }

    private long update_user(long id, RemoteUser user) {
        ContentValues newValues = new ContentValues();
        newValues.put(layout.users.getField("firstname").getName(), user.getFirstname());
        newValues.put(layout.users.getField("lastname").getName(), user.getLastname());
        newValues.put(layout.users.getField("remote_id").getName(), user.getId());
        return db.update(layout.users.getName(), newValues, "id=" + id, null);
    }


    public void insert_or_update_user(RemoteUser user) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        //first get the local id if it exists
        Long id = this.resolve_remote_id_to_local_id(layout.users, user.getId());
        long result;
        if (id == null) {
            //the remote user needs to be inserted
            result = this.insert_user(user);
            if (result == -1) {
                throw new RuntimeException("Could not insert a Remote User, this should not happen");
            }
        } else {
            //the local user exists already and needs to be updated
            result = update_user(id, user);
            if (result == -1) {
                throw new RuntimeException("Could not update a Remote User, this should not happen");
            }
            //it is now in the db, afterwards, check if it is already in memory
            WeakReference<User> ref = user_object_cache.get(id);
            if (ref != null) {
                User in_cache = ref.get();
                if (in_cache != null) {
                    in_cache.update(user);
                }
            }
            this.release_lock();
        }
    }

    private long insert_entry(RemoteEntry entry,long id) throws SBSBaseException {
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.entries.getField("remote_id").getName(),entry.getId());
        initialValues.put(layout.entries.getField("experiment_id").getName(), this.resolve_remote_id_to_local_id(layout.experiments, entry.getExperiment_id()));
        initialValues.put(layout.entries.getField("user_id").getName(), id);
        initialValues.put(layout.entries.getField("title").getName(),entry.getTitle());
        initialValues.put(layout.entries.getField("current_time").getName(), entry.getSync_time());
        initialValues.put(layout.entries.getField("date_user").getName(), entry.getEntry_time());
        initialValues.put(layout.entries.getField("date").getName(), entry.getChange_time());
        initialValues.put(layout.entries.getField("attachment_ref").getName(), entry.getAttachment_ser());
        initialValues.put(layout.entries.getField("attachment_type").getName(), entry.getAttachment_type());
        return db.insert(layout.entries.getName(), null, initialValues);

    }

    private long update_entry(long id,RemoteEntry entry,long userid) {
        ContentValues newValues = new ContentValues();
        newValues.put(layout.entries.getField("remote_id").getName(),entry.getId());
        newValues.put(layout.entries.getField("experiment_id").getName(), this.resolve_remote_id_to_local_id(layout.experiments, entry.getExperiment_id()));
        newValues.put(layout.entries.getField("user_id").getName(), userid);
        newValues.put(layout.entries.getField("title").getName(),entry.getTitle());
        newValues.put(layout.entries.getField("current_time").getName(), entry.getSync_time());
        newValues.put(layout.entries.getField("date_user").getName(), entry.getEntry_time());
        newValues.put(layout.entries.getField("date").getName(), entry.getChange_time());
        newValues.put(layout.entries.getField("attachment_ref").getName(), entry.getAttachment_ser());
        newValues.put(layout.entries.getField("attachment_type").getName(), entry.getAttachment_type());
        return db.update(layout.entries.getName(), newValues, "id=" + id, null);
    }

    public void insert_or_update_entry(RemoteEntry entry,Long userid) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        //first get the local id if it exists
        Long id = this.resolve_remote_id_to_local_id(layout.entries, entry.getId());

        long result;
        if (id == null) {
            //the remote project needs to be inserted
            result = this.insert_entry(entry,userid);
            System.out.println("neue Entryid"+result);
            if (result == -1) {
                throw new RuntimeException("Could not insert a Remote Project, this should not happen");
            }
        } else {
            //the local project exists already and needs to be updated
           // result = update_project(id, project);
            result = update_entry(id,entry,userid);
            if (result == -1) {
                throw new RuntimeException("Could not update a Remote Project, this should not happen");
            }
            this.release_lock();
        this.release_lock();

    }

    }

    public Boolean is_entry_up_to_date(Entry_Remote_Identifier eri) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        Long id = this.resolve_remote_id_to_local_id(layout.entries, eri.getId());
        if (id == null) {
            //entry is not in the db, therefore it cannot be up to date
            return Boolean.FALSE;
        }
        ;
        //check if it is already in memory
        WeakReference<Entry> ref = entry_object_cache.get(id);
        Entry cache = null;
        if (ref != null) {
            cache = ref.get();
        }
        if (cache != null) {
            return cache.getChange_time() >= eri.getLast_change();
        }
        //it is not in memory, so we have to check the db

        Cursor c = db.rawQuery("SELECT " + layout.entries.getField("current_time").getName()
                + " FROM " + layout.entries.getName()
                + " WHERE "
                + layout.entries.getField("id") + "="
                + id, null);
        //c cannot be null as we already got the id in advance
        c.moveToFirst();
        int index = c.getColumnIndex(layout.entries.getField("current_time").getName());
        this.release_lock();
        return c.getLong(index) >= eri.getLast_change();
    }

    private long get_row_id(Cursor c) {
        return c.getLong(c.getColumnIndex("id"));
    }

    private void check_open() throws SBSBaseException {
        if (!this.opened) {
            this.open();

      //      throw new RuntimeException("Interface not opened yet, " +
        //            "call open before using the interface");
        }


    }

    private void get_lock() throws SBSBaseException{
        //FIXME, not doing anything yet, lockign loging needs to go here
    };

    private void release_lock() throws SBSBaseException{
        //FIXME, not doing anything yet, lockign loging needs to go here
    };

    public object_level_db(Context ctx) {
        if (this.db_helper == null) {
            this.db_helper = new low_level_adapter(ctx);
        }
    }

    public void open() throws SBSBaseException {
        this.get_lock();
        if(!this.opened){
            this.opened = true;
            this.db = this.db_helper.getWritableDatabase();
        }
        this.release_lock();
    }

    public void close() throws SBSBaseException {
        this.get_lock();
        if(this.opened) {
            this.opened = false;
            this.db.close();
            this.db_helper.close();
            this.db_helper = null;
        }
        this.release_lock();
    }

    public User register_user(String login, String password, URL server) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        long result;
        deleteAllSyncedProjects();
        deleteAllSyncedExperiments();
        deleteAllSyncedEntry();
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.users.getField("login").getName(), login);
        initialValues.put(layout.users.getField("hashed_pw").getName(), User.hashedPW(password));
        initialValues.put(layout.users.getField("server").getName(), server.toString());
        result = this.db.insert(layout.users.getName(), null, initialValues);
        if (result == -1) {
            //TODO
            //what happens if the user already exists?
            //should we load it from db?
            //and then update?
            //should the compare the password?
            //because we need a way to check if a user is using a offline pw
            //or not
            this.release_lock();
            throw new RuntimeException("Not yet implemented yet!");
            //return null;
        } else {
            this.release_lock();
            return new User(login, password, server, result);
        }
    }

    public Project register_project(User user, String project_name) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        long result;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.projects.getField("user_id").getName(), user.getId());
        initialValues.put(layout.projects.getField("name").getName(), project_name);
        result = this.db.insert(layout.projects.getName(), null, initialValues);
        this.release_lock();
        if (result == -1) {
            this.release_lock();
            throw new RuntimeException("Registering a new project failed," +
                    " this should never happen");
        } else {
            Project tmp = new Project(result, project_name);
            project_object_cache.put(result, new WeakReference<Project>(tmp));
            this.release_lock();
            return tmp;
        }

    }

    public Experiment register_experiment(User user, Project project, String experiment_name)
            throws SBSBaseException {
        this.get_lock();
        this.check_open();
        long id;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.experiments.getField("user_id").getName(), user.getId());
        initialValues.put(layout.experiments.getField("project_id").getName(), project.get_id());
        initialValues.put(layout.experiments.getField("name").getName(), experiment_name);
        id = this.db.insert(layout.experiments.getName(), null, initialValues);
        if (id == -1) {
            this.release_lock();
            throw new RuntimeException("Registering a new experiment failed," +
                    " this should never happen");
        } else {
            Experiment tmp = new Experiment(id, project.get_id(), experiment_name);
            experiment_object_cache.put(id, new WeakReference<Experiment>(tmp));
            this.release_lock();
            return tmp;
        }
    }

    public Entry new_Entry(User user, Experiment experiment, String title,
                           AttachmentBase attachment, long date_user) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        long current_time = (System.currentTimeMillis() / 1000);
        long id;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.entries.getField("experiment_id").getName(), experiment.get_id());
        initialValues.put(layout.entries.getField("user_id").getName(), user.getId());
        initialValues.put(layout.entries.getField("title").getName(), title);
        initialValues.put(layout.entries.getField("date_creation").getName(), current_time);
        initialValues.put(layout.entries.getField("date_user").getName(), date_user);
        initialValues.put(layout.entries.getField("date_current").getName(), current_time);
        initialValues.put(layout.entries.getField("attachment_ref").getName(),
                attachment.getReference());
        initialValues.put(layout.entries.getField("attachment_type").getName(),
                attachment.getTypeNumber());
        id = this.db.insert(layout.entries.getName(), null, initialValues);
        if (id == -1) {
            this.release_lock();
            throw new RuntimeException("Registering a new entry failed, this should never happen");
        } else {
            Entry tmp = new Entry(id, user, experiment.get_id(), title, attachment, current_time);
            entry_object_cache.put(id, new WeakReference<Entry>(tmp));
            this.release_lock();
            return tmp;
        }
    }

    private void merge_experiment_or_project_with_underlying_references(
            Long id_to_be_merged, Long id_to_merge_into,
            table upper_table,
            table underlying_table_with_references,
            String field_name_for_reference_in_underlying_table
    ) {
        int index;
        Cursor c;
        //first, check that the to be merged row does not have a remote_id
        c = db.rawQuery("SELECT " + upper_table.getField("remote_id").getName()
                + " FROM " + upper_table.getName()
                + " WHERE "
                + upper_table.getField("id") + "="
                + id_to_be_merged, null);
        c.moveToFirst();
        index = c.getColumnIndex(upper_table.getField("remote_id").getName());
        if (!c.isNull(index)) {
            throw new RuntimeException("Entries to be merged into need to be synced!");
        }
        c.close();
        //second, update the references in the underlying table
        ContentValues newValues = new ContentValues();
        newValues.put(field_name_for_reference_in_underlying_table, id_to_merge_into);
        db.update(underlying_table_with_references.getName(), newValues,
                field_name_for_reference_in_underlying_table + "=" + id_to_be_merged, null);
        //third, delete the row with the id_to_be_merged
        int deleted_rows;
        deleted_rows = db.delete(upper_table.getName(), upper_table.getField("id") + "="
                + id_to_be_merged, null);
        if (deleted_rows != 1) {
            throw new RuntimeException(
                    "During a merge, mor then or less " +
                            "than 1 row was deleted! This should never happen!");
        }
    }


    public void merge_project_into(Project project, Project real_project) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        this.merge_experiment_or_project_with_underlying_references(project.get_id(),
                real_project.get_id(),
                layout.projects,
                layout.experiments,
                layout.experiments.getField("project_id").getName()
        );
        project_object_cache.remove(project.get_id());
        this.release_lock();
    }

    public void merge_experiment_into(Experiment experiment, Experiment real_experiment)
            throws SBSBaseException {
        this.get_lock();
        this.check_open();
        this.merge_experiment_or_project_with_underlying_references(experiment.get_id(),
                real_experiment.get_id(),
                layout.experiments,
                layout.entries,
                layout.entries.getField("experiment_id").getName()
        );
        experiment_object_cache.remove(experiment.get_id());
        this.release_lock();
    }

    private User convert_cursor_to_user(Cursor c) {
        int index;
        String firstname, lastname, login, server_string;
        byte[] hashed_pw;
        long id;
        URL server;

        index = c.getColumnIndex(layout.users.getField("id").getName());
        id = c.getLong(index);

        index = c.getColumnIndex(layout.users.getField("firstname").getName());
        if (!c.isNull(index)) {
            firstname = c.getString(index);
        } else {
            firstname = null;
        }

        index = c.getColumnIndex(layout.users.getField("lastname").getName());
        if (!c.isNull(index)) {
            lastname = c.getString(index);
        } else {
            lastname = null;
        }

        index = c.getColumnIndex(layout.users.getField("login").getName());
        if (!c.isNull(index)) {
            login = c.getString(index);
        } else {
            login = null;
        }

        index = c.getColumnIndex(layout.users.getField("server").getName());
        if (!c.isNull(index)) {
            server_string = c.getString(index);
            try {
                server = new URL(server_string);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Url in database is not a valid url. " +
                        "This should never happen as" +
                        "we check the urls before we write them into the db!");
            }
        } else {
            server = null;
        }

        index = c.getColumnIndex(layout.users.getField("hashed_pw").getName());
        if (!c.isNull(index)) {
            hashed_pw = c.getBlob(index);
        } else {
            hashed_pw = null;
        }
        return new User(login, hashed_pw, server, id, firstname, lastname);
    }

    private Project convert_cursor_to_project(Cursor c) {
        int index;
        Long date_creation;
        long id;
        String name, description;
        index = c.getColumnIndex(layout.projects.getField("id").getName());
        id = c.getLong(index);
        index = c.getColumnIndex(layout.projects.getField("name").getName());
        name = c.getString(index);

        index = c.getColumnIndex(layout.projects.getField("description").getName());
        if (!c.isNull(index)) {
            description = c.getString(index);
        } else {
            description = null;
        }

        index = c.getColumnIndex(layout.projects.getField("date_creation").getName());
        if (!c.isNull(index)) {
            date_creation = c.getLong(index);
        } else {
            date_creation = null;
        }

        return new Project(id, name, description, date_creation);
    }

    private Experiment convert_cursor_to_experiment(Cursor c) {
        int index;
        Long date_creation;
        long id, project_id;
        String name, description;
        index = c.getColumnIndex(layout.experiments.getField("id").getName());
        id = c.getLong(index);
        index = c.getColumnIndex(layout.experiments.getField("name").getName());
        name = c.getString(index);
        index = c.getColumnIndex(layout.experiments.getField("project_id").getName());
        project_id = c.getLong(index);

        index = c.getColumnIndex(layout.experiments.getField("description").getName());
        if (!c.isNull(index)) {
            description = c.getString(index);
        } else {
            description = null;
        }

        index = c.getColumnIndex(layout.experiments.getField("date_creation").getName());
        if (!c.isNull(index)) {
            date_creation = c.getLong(index);
        } else {
            date_creation = null;
        }
        return new Experiment(id, project_id, name, description, date_creation);
    }

    private Entry convert_cursor_to_entry(Cursor c) {
        long id;
        long experiment_id;
        String title;
        Long sync_time;
        long entry_time;
        long change_time;
        User user;
        long user_id;
        String attachment_ref;
        int attachment_type;
        AttachmentBase attachment;

        int index;

        index = c.getColumnIndex(layout.entries.getField("id").getName());
        id = c.getLong(index);

        index = c.getColumnIndex(layout.entries.getField("experiment_id").getName());
        experiment_id = c.getLong(index);

        index = c.getColumnIndex(layout.entries.getField("user_id").getName());
        user_id = c.getLong(index);

        index = c.getColumnIndex(layout.entries.getField("date_user").getName());
        entry_time = c.getLong(index);

        index = c.getColumnIndex(layout.entries.getField("current_time").getName());
        change_time = c.getLong(index);

        index = c.getColumnIndex(layout.entries.getField("attachment_type").getName());
        attachment_type = c.getInt(index);

        index = c.getColumnIndex(layout.entries.getField("title").getName());
        title = c.getString(index);

        index = c.getColumnIndex(layout.entries.getField("attachment_ref").getName());
        attachment_ref = c.getString(index);

        user = this.get_user_by_local_id(user_id);
        attachment = AttachmentBase.dereference(attachment_type, attachment_ref);

        index = c.getColumnIndex(layout.entries.getField("date").getName());
        if (!c.isNull(index)) {
            sync_time = c.getLong(index);
        } else {
            sync_time = null;
        }
        return new Entry(id, user, experiment_id, title, attachment, entry_time, sync_time,
                change_time);
    }

    public LinkedList<Project> get_projects(User user) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.projects.getName()
                + " WHERE "
                + layout.projects.getField("user_id").getName()
                + "="
                + user.getId(), null);
        LinkedList<Project> tmp = new LinkedList<Project>();
        Project cache = null;
        long id;
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                id = this.get_row_id(c);
                cache = this.convert_cursor_to_project(c);

              //  WeakReference<Project> ref = project_object_cache.get(id);
             //   if (ref != null) {
             //       cache = ref.get();
             //   }
                if (cache == null) { // this made the bug it not resseted the cache why ever


                //   project_object_cache.put(id, new WeakReference<Project>(cache));
                }
                tmp.add(cache);

            }
            c.close();
        }
        this.release_lock();
        return tmp;
    }

    public int getExperimentCountByProjectLocalID(Long id) throws SBSBaseException {
        check_open();

        Cursor c = db.rawQuery("SELECT COUNT(*)"
                + " FROM " + layout.experiments.getName()
                + " WHERE "
                + layout.experiments.getField("project_id").getName() + "="
                + id, null);

if (c == null)
    return 0;
else {
    c.moveToFirst();
    return c.getInt(0);
}
    }

    public ArrayList<Long> getExperiment_ids_by_user(User user){
        Cursor c = db.rawQuery("SELECT distinct remote_id FROM " + layout.experiments.getName()
                + " WHERE "
                + layout.experiments.getField("user_id").getName() + "="
                + user.getId(), null);
      return ExperimentId_cursor_to_longArray(c);
    }
    private ArrayList<Long> ExperimentId_cursor_to_longArray(Cursor c){
        c.moveToFirst();
        ArrayList<Long> longs = new ArrayList<Long>();
        do{
         longs.add(c.getLong(c.getColumnIndex("remote_id")));
        }while (c.moveToNext());
return longs;
    }
    public LinkedList<Experiment> get_experiments(User user, Project project) throws SBSBaseException {
        this.get_lock();
        this.check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.experiments.getName()
                + " WHERE "
                + layout.experiments.getField("user_id").getName() + "="
                + user.getId() + " AND "
                + layout.experiments.getField("project_id").getName() + "=" + project.get_id(), null);
        LinkedList<Experiment> tmp = new LinkedList<Experiment>();
        Experiment cache = null;
        long id;
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                id = this.get_row_id(c);
                cache = this.convert_cursor_to_experiment(c);
                /*
                WeakReference<Experiment> ref = experiment_object_cache.get(id);
                if (ref != null) {
                    cache = ref.get();
                }
                if (cache == null) {
                    cache = this.convert_cursor_to_experiment(c);
                 //   experiment_object_cache.put(id, new WeakReference<Experiment>(cache));
                }*/
                tmp.add(cache);
            }
            c.close();
        }
        this.release_lock();
        return tmp;
    }

    private User get_user_by_local_id(long user_id) {
        Cursor c = db.rawQuery("SELECT * FROM " + layout.users.getName()
                + " WHERE "
                + layout.users.getField("id").getName() + "="
                + user_id, null);
        c.moveToFirst();
        return this.convert_cursor_to_user(c);
    }

    public LinkedList<Entry> get_entries(User user, Experiment experiment, int number) throws SBSBaseException {
        this.get_lock();
        this.check_open();



 Cursor c = db.rawQuery("SELECT * FROM " + layout.entries.getName()
 + " WHERE "
 + layout.entries.getField("user_id").getName() + " = "
 + user.getId()+" AND "
 + layout.entries.getField("experiment_id").getName() + " = "
 + experiment.get_id()+" Limit "+number, null);


    //Cursor c = db.rawQuery(" SELECT * FROM " + layout.entries.getName() + " WHERE " +layout.entries.getField("user_id").getName()+" = ? AND "+layout.entries.getField("experiment_id").getName()+" = ? LIMIT ?", new String[]{String.valueOf(user.getId()), String.valueOf(experiment.get_id()), String.valueOf(number)});

    //  Cursor c = db.rawQuery("SELECT * FROM " + layout.entries.getName()  , new String[]{});


        LinkedList<Entry> tmp = new LinkedList<Entry>();
        Entry cache = null;
        long id;
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                id = this.get_row_id(c);
               // WeakReference<Entry> ref = entry_object_cache.get(id);
            //    if (ref != null) {
                 //   cache = ref.get();
              //  }
        //        if (cache == null) {
                    cache = this.convert_cursor_to_entry(c);
                System.out.println("Debug info for crash in insert entries");
                System.out.println(user.getId());
                System.out.println(experiment.get_name());
                System.out.println(cache.getTitle());

            //        entry_object_cache.put(id, new WeakReference<Entry>(cache));
            //    }
                tmp.add(cache);
            }
            c.close();
        }
        this.release_lock();
        return tmp;
    }

    public LinkedList<User> get_all_users_with_login() throws SBSBaseException {
        this.get_lock();
        this.check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.users.getName()
                + " WHERE "
                + layout.users.getField("login").getName()
                + " IS NOT NULL AND "
                + layout.users.getField("hashed_pw").getName()
                + " IS NOT NULL", null);
        LinkedList<User> tmp = new LinkedList<User>();
        User cache = null;
        long id;
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                id = this.get_row_id(c);
                WeakReference<User> ref = user_object_cache.get(id);
                if (ref != null) {
                    cache = ref.get();
                }
                if (cache == null) {
                    cache = this.convert_cursor_to_user(c);
                    user_object_cache.put(id, new WeakReference<User>(cache));
                }
                tmp.add(cache);
            }
            c.close();
        }
        this.release_lock();
        return tmp;
    }

    //TODO
    //Not sure if we need this for the gui, I guess we only need it for the background service
    public LinkedList<Entry_Remote_Identifier> get_all_entry_timestamps(User user)
            throws SBSBaseException {
        this.get_lock();
        this.check_open();
        this.release_lock();
        throw new RuntimeException("Not Implemented yet");
    }

}