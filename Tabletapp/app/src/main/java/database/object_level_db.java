package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.URL;
import java.util.LinkedList;

import datastructures.AttachmentBase;
import datastructures.Entry;
import datastructures.Experiment;
import datastructures.Project;
import datastructures.User;
import datastructures.Entry_Remote_Identifier;

public class object_level_db {
    private low_level_adapter db_helper;
    private SQLiteDatabase db;
    private boolean opened = false;

    private void check_open(){
        if(!this.opened){
            //trow exception so we know we haven't opened the interface yet!
        }
    }
/* TODO: outcommentet because of bug
    public object_level_db(Context ctx) {
        this.db_helper = new low_level_adapter(ctx);
    }*/

    public void open() {
       this.opened = true;
        this.db = this.db_helper.getWritableDatabase();
    }
    public void close() {
        check_open();
        this.opened = false;
        this.db.close();
        this.db_helper.close();
    }


    public LinkedList<User> get_all_user_logins(){
        check_open();
        return null;
    };

    public User register_user(String login, String password, URL server) {
        check_open();
        long result;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.users.getField("login").getName(), login);
        initialValues.put(layout.users.getField("hashed_pw").getName(), User.hashedPW(password));
        initialValues.put(layout.users.getField("server").getName(), server.toString());
        result = this.db.insert(layout.users.getName(), null, initialValues);
        if(result == -1){
            //FIXME
            //what happens if the user already exists?
            //should we load it from db?
            //and then update?
            return null;
        }else{
            return new User(login, password, server, result);
        }
    }

    public Project register_project(User user, String project_name) {
        check_open();
        long result;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.projects.getField("user_id").getName(),user.getUser_id());
        initialValues.put(layout.projects.getField("name").getName(), project_name);
        result = this.db.insert(layout.projects.getName(), null, initialValues);
        if(result == -1){
            //This should never happen
            return null;
        }else{
            return new Project(result, project_name);
        }
    }

    public Experiment register_experiment(User user, Project project, String experiment_name) {
        check_open();
        long id;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.experiments.getField("user_id").getName(), user.getUser_id());
        initialValues.put(layout.experiments.getField("project_id").getName(), project.get_id());
        initialValues.put(layout.experiments.getField("name").getName(), experiment_name);
        id = this.db.insert(layout.experiments.getName(), null, initialValues);
        if(id == -1){
            //this should never happen
            return null;
        }else{
            return new Experiment(id, project.get_id(), experiment_name);
        }
    }
    public static final table entries = new table("entries",
            new table.table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table.table_field("remote_id", "INTEGER", "UNIQUE"),
            new table.table_field("experiment_id", "INTEGER", "NOT NULL"),
            new table.table_field("user_id", "INTEGER NOT NULL"),
            new table.table_field("title", "STRING", "NOT NULL"),
            new table.table_field("date_creation", "INTEGER", "NOT NULL"),
            new table.table_field("date_user", "INTEGER", "NOT NULL"),
            new table.table_field("date_current", "INTEGER"),
            new table.table_field("attachment_ref", "STRING", "NOT NULL"),
            new table.table_field("attachment_type", "INTEGER", "NOT NULL"));

    public Entry new_Entry(User user, Experiment experiment, String title, AttachmentBase attachment, long date_user) {
        check_open();
        long current_time = (long)(System.currentTimeMillis() / 1000);
        long id;
        ContentValues initialValues = new ContentValues();
        initialValues.put(layout.entries.getField("experiment_id").getName(), experiment.get_id());
        initialValues.put(layout.entries.getField("user_id").getName(), user.getId());
        initialValues.put(layout.entries.getField("title").getName(), title);
        initialValues.put(layout.entries.getField("date_creation").getName(), current_time);
        initialValues.put(layout.entries.getField("date_user").getName(), date_user);
        initialValues.put(layout.entries.getField("date_current").getName(), current_time);
        initialValues.put(layout.entries.getField("attachment_ref").getName(), attachment.getTypeNumber());
        //FIXME how to store attachments in the local db? also with a ref or with a blob?
        //initialValues.put(layout.entries.getField("attachment_type").getName(), attachment.getContent());

        id = this.db.insert(layout.entries.getName(), null, initialValues);
        if(id == -1){
            //this should never happen
            return null;
        }else{
            return new Entry(id, user, experiment.get_id(), title, attachment, current_time);
    }}


    public LinkedList<Project> match_project(Project project) {
        check_open();
        return null;
    }


    public LinkedList<Project> match_project(Project project, Project real_project) {
        check_open();
        return null;
    }


    public LinkedList<Experiment> match_experiment(Experiment experiment) {
        check_open();
        return null;
    }


    public LinkedList<Experiment> match_experiment(Experiment experiment, Experiment real_experiment) {
        check_open();
        return null;
    }

    private LinkedList<String> get_project_names(User user){
        check_open();
        return null;
    }


    private Project get_project(User user, String project_name) {
        Cursor c = db.rawQuery("SELECT * FROM " + layout.projects.getName()
                + " WHERE "
                + layout.projects.getField("user_id") + "="
                +  user.getUser_id() + " AND "
                + layout.projects.getField("name") + "=" + project_name, null);
        if (c != null) {
            c.moveToFirst();
        }
        int index;
        Long id, remote_id, user_id, date_creation;
        String name, description;
        //TODO: can produce nullpointer except
        index = c.getColumnIndex(layout.projects.getField("id").getName());
        id = c.getLong(index);
        index = c.getColumnIndex(layout.projects.getField("name").getName());
        name = c.getString(index);

        index = c.getColumnIndex(layout.projects.getField("description").getName());
        if (!c.isNull(index)) {
            description = c.getString(index);
        }else{
            description = null;
        }

        index = c.getColumnIndex(layout.projects.getField("date_creation").getName());
        if (!c.isNull(index)) {
            date_creation = c.getLong(index);
        }else{
            date_creation = null;
        }
        c.close();

        return new Project(id, name, description, date_creation);
    }

    public LinkedList<Project> get_projects(User user) {
        check_open();
        return null;
    }

    public LinkedList<Experiment> get_experiments(User user, Project project) {
        check_open();
        return null;
    }


    public LinkedList<Entry> get_entries(User user, Experiment experiment, int number) {
        check_open();
        return null;
    }

    public LinkedList<Integer> get_all_users_with_login(){
        check_open();
        return null;
    };

    public LinkedList<Entry_Remote_Identifier> get_all_entry_timestamps(Integer user_id) {
        check_open();
        return null;
    }

}