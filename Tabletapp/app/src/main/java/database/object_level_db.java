package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import datastructures.AttachmentBase;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.User;
import exceptions.SBSBaseException;
import scon.Entry_Remote_Identifier;

public class object_level_db {
    private low_level_adapter db_helper;
    private SQLiteDatabase db;
    private boolean opened = false;


    private void check_open()throws SBSBaseException{
        if(!this.opened){
            //TODO
            throw new RuntimeException("Check for interface open. Not implemented yet");
        }
    }

    public object_level_db(Context ctx) {
        if(this.db_helper == null) {
            this.db_helper = new low_level_adapter(ctx);
        }
    }

    public void open() {
       this.opened = true;
       this.db = this.db_helper.getWritableDatabase();
    }

    public void close()throws SBSBaseException{
        check_open();
        this.opened = false;
        this.db.close();
        this.db_helper.close();
        this.db_helper = null;
    }

    public User register_user(String login, String password, URL server) throws SBSBaseException{
        check_open();
        long result;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.users.getField("login").getName(), login);
        initialValues.put(layout.users.getField("hashed_pw").getName(), User.hashedPW(password));
        initialValues.put(layout.users.getField("server").getName(), server.toString());
        result = this.db.insert(layout.users.getName(), null, initialValues);
        if(result == -1){
            //TODO
            //what happens if the user already exists?
            //should we load it from db?
            //and then update?
            //should the compare the password?
            //because we need a way to check if a user is using a offline pw
            //or not
            throw new RuntimeException("Not yet implemented yet!");
            //return null;
        }else{
            return new User(login, password, server, result);
        }
    }

    public Project register_project(User user, String project_name) throws SBSBaseException{
        check_open();
        long result;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.projects.getField("user_id").getName(),user.getUser_id());
        initialValues.put(layout.projects.getField("name").getName(), project_name);
        result = this.db.insert(layout.projects.getName(), null, initialValues);
        if(result == -1){
            throw new RuntimeException("Registering a new project failed," +
                    " this should never happen");
        }else{
            return new Project(result, project_name);
        }
    }

    public Experiment register_experiment(User user, Project project, String experiment_name)
            throws SBSBaseException{
        check_open();
        long id;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.experiments.getField("user_id").getName(), user.getUser_id());
        initialValues.put(layout.experiments.getField("project_id").getName(), project.get_id());
        initialValues.put(layout.experiments.getField("name").getName(), experiment_name);
        id = this.db.insert(layout.experiments.getName(), null, initialValues);
        if(id == -1){
            throw new RuntimeException("Registering a new experiment failed," +
                    " this should never happen");
        }else{
            return new Experiment(id, project.get_id(), experiment_name);
        }
    }

    public Entry new_Entry(User user, Experiment experiment, String title,
                           AttachmentBase attachment, long date_user) throws SBSBaseException{
        check_open();
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
        if(id == -1){
            throw new RuntimeException("Registering a new entry failed, this should never happen");
        }else{
            return new Entry(id, user, experiment.get_id(), title, attachment, current_time);
        }
    }

    private void merge_experiment_or_project_with_underlying_references(
            Long id_to_be_merged, Long id_to_merge_into,
            table upper_table,
            table underlying_table_with_references,
            String field_name_for_reference_in_underlying_table
    )
    {
        int index;
        Cursor c;
        //first, check that the to be merged row does not have a remote_id
        c = db.rawQuery("SELECT "+ upper_table.getField("remote_id").getName()
                + " FROM " + upper_table.getName()
                + " WHERE "
                + upper_table.getField("id") + "="
                +  id_to_be_merged , null);
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
        if(deleted_rows!=1){
            throw new RuntimeException(
                    "During a merge, mor then or less " +
                            "than 1 row was deleted! This should never happen!");
        }
    }


    public void merge_project_into(Project project, Project real_project)throws SBSBaseException {
        check_open();
        this.merge_experiment_or_project_with_underlying_references(project.get_id(),
                real_project.get_id(),
                layout.projects,
                layout.experiments,
                layout.experiments.getField("project_id").getName()
        );
    }

    public void match_experiment(Experiment experiment, Experiment real_experiment) throws SBSBaseException{
        check_open();
        this.merge_experiment_or_project_with_underlying_references(experiment.get_id(),
                real_experiment.get_id(),
                layout.experiments,
                layout.entries,
                layout.entries.getField("experiment_id").getName()
        );
    }

    private User convert_cursor_to_user(Cursor c){
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
            }catch (MalformedURLException e){
                throw new RuntimeException("Url in database is not a valid url. This should never happen as" +
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

    private Project convert_cursor_to_project(Cursor c){
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

    private Experiment convert_cursor_to_experiment(Cursor c){
        int index;
        Long date_creation;
        long id, project_id;
        String name, description;
        index = c.getColumnIndex(layout.projects.getField("id").getName());
        id = c.getLong(index);
        index = c.getColumnIndex(layout.projects.getField("name").getName());
        name = c.getString(index);
        index = c.getColumnIndex(layout.projects.getField("project_id").getName());
        project_id = c.getLong(index);

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
        return new Experiment(id, project_id, name, description, date_creation);
    }

    private Entry convert_cursor_to_entry(Cursor c){
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

        index = c.getColumnIndex(layout.users.getField("id").getName());
        id = c.getLong(index);

        index = c.getColumnIndex(layout.users.getField("experiment_id").getName());
        experiment_id = c.getLong(index);

        index = c.getColumnIndex(layout.users.getField("user_id").getName());
        user_id = c.getLong(index);

        index = c.getColumnIndex(layout.users.getField("entry_time").getName());
        entry_time = c.getLong(index);

        index = c.getColumnIndex(layout.users.getField("change_time").getName());
        change_time = c.getLong(index);

        index = c.getColumnIndex(layout.users.getField("attachment_type").getName());
        attachment_type = c.getInt(index);

        index = c.getColumnIndex(layout.users.getField("title").getName());
        title = c.getString(index);

        index = c.getColumnIndex(layout.users.getField("attachment_ref").getName());
        attachment_ref = c.getString(index);

        user = this.get_user_by_local_id(user_id);
        attachment = AttachmentBase.dereference(attachment_type, attachment_ref);

        index = c.getColumnIndex(layout.users.getField("sync_time").getName());
        if (!c.isNull(index)) {
            sync_time = c.getLong(index);
        } else {
            sync_time = null;
        }

        return new Entry(id, user, experiment_id, title, attachment, entry_time, sync_time,
                change_time);
    }

    public LinkedList<Project> get_projects(User user)throws SBSBaseException {
        check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.projects.getName()
                + " WHERE "
                + layout.projects.getField("user_id") + "="
                +  user.getUser_id(), null);
        LinkedList<Project> tmp = new LinkedList<Project>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                tmp.add(this.convert_cursor_to_project(c));
            }
            c.close();
        }
        return tmp;
    }

    public LinkedList<Experiment> get_experiments(User user, Project project)throws SBSBaseException {
        check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.experiments.getName()
                + " WHERE "
                + layout.experiments.getField("user_id") + "="
                +  user.getUser_id() + " AND "
                + layout.experiments.getField("project_id") + "=" + project.get_id(), null);
        LinkedList<Experiment> tmp = new LinkedList<Experiment>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                tmp.add(this.convert_cursor_to_experiment(c));
            }
            c.close();
        }
        return tmp;
    }

    private User get_user_by_local_id(long user_id){
        Cursor c = db.rawQuery("SELECT * FROM " + layout.users.getName()
                + " WHERE "
                + layout.users.getField("id") + "="
                + user_id, null);
        c.moveToFirst();
        return this.convert_cursor_to_user(c);
    }

    public LinkedList<Entry> get_entries(User user, Experiment experiment, int number) throws SBSBaseException{
        check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.entries.getName()
                + " WHERE "
                + layout.entries.getField("user_id") + "="
                + user.getUser_id() + " AND "
                + layout.entries.getField("experiment_id") + "="
                + experiment.get_id() + " LIMIT " + number, null);
        LinkedList<Entry> tmp = new LinkedList<Entry>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                tmp.add(this.convert_cursor_to_entry(c));
            }
            c.close();
        }
        return tmp;
    }

    public LinkedList<User> get_all_users_with_login()throws SBSBaseException{
        check_open();
        Cursor c = db.rawQuery("SELECT * FROM " + layout.users.getName()
                + " WHERE "
                + layout.users.getField("login")
                + " IS NOT NULL AND "
                + layout.users.getField("hashed_pw")
                + " IS NOT NULL", null);
        LinkedList<User> tmp = new LinkedList<User>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                tmp.add(this.convert_cursor_to_user(c));
            }
            c.close();
        }
        return tmp;
    }

    //TODO
    //Not sure if we need this for the gui, I guess we only need it for the background service
    public LinkedList<Entry_Remote_Identifier> get_all_entry_timestamps(User user)
            throws SBSBaseException {
        check_open();
        throw new RuntimeException("Not Implemented yet");
    }

}