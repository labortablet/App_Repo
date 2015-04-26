/*
Class to define the most current layout of the database.
Later, we will also have a patching system so we can migrate from one version to the next

 I am not sure where to put the defined statements yet but I think this is already a lot nicer
 as the magic value mess in DBAdapter
 */
package database;


public class layout {
    public static final int VERSION = 1;
    public static final String NAME = "LabletDB";

    public static final table entries = new table("entries",
           new table.table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
           new table.table_field("remote_id", "INTEGER", "UNIQUE"),
           new table.table_field("experiment_id", "INTEGER", "NOT NULL"),
           new table.table_field("user_id", "INTEGER NOT NULL"),
           new table.table_field("title", "STRING", "NOT NULL"),
           new table.table_field("current_time", "INTEGER", "NOT NULL"),
           new table.table_field("date_user", "INTEGER", "NOT NULL"),
           new table.table_field("date", "INTEGER"),
           new table.table_field("attachment_ref", "STRING", "NOT NULL"),
           new table.table_field("attachment_type", "INTEGER", "NOT NULL")
    );

    public static final table users = new table("users",
            new table.table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table.table_field("remote_id", "INTEGER", "UNIQUE"),
            new table.table_field("lastname", "STRING"),
            new table.table_field("firstname", "STRING"),
            new table.table_field("login", "STRING"),
            new table.table_field("hashed_pw", "BLOB"),
            new table.table_field("server", "STRING")
    );

    public static final table experiments = new table("experiments",
            new table.table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table.table_field("remote_id", "INTEGER", "UNIQUE"),
            new table.table_field("user_id", "INTEGER", "NOT NULL"),
            new table.table_field("project_id", "INTEGER"),
            new table.table_field("name", "STRING", "NOT NULL"),
            new table.table_field("description", "STRING"),
            new table.table_field("date_creation", "INTEGER")
    );

    public static final table projects = new table("projects",
            new table.table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table.table_field("remote_id", "INTEGER", "UNIQUE"),
            new table.table_field("user_id", "INTEGER", "NOT NULL"),
            new table.table_field("name", "STRING", "NOT NULL"),
            new table.table_field("description", "STRING"),
            new table.table_field("date_creation", "INTEGER")
    );

    public static final table[] tables = new table[]{
            entries, users, experiments, projects
    };
}
