/*
Class to define the most current layout of the database.
Later, we will also have a patching system so we can migrate from one version to the next

 I am not sure where to put the defined statements yet but I think this is already a lot nicer
 as the magic value mess in DBAdapter
 */
package database;

public class layout {
    public static final int db_version = 1;
    public static final String DATABASE_NAME = "LabletDB";
    public static final table entries = new table("entries",
           new table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
           new table_field("remote_id", "INTEGER", "UNIQUE"),
           new table_field("experiment_id", "INTEGER", "NOT NULL"),
           new table_field("user_id", "INTEGER NOT NULL"),
           new table_field("title", "STRING", "NOT NULL"),
           new table_field("date_creation", "INTEGER", "NOT NULL"),
           new table_field("date_user", "INTEGER", "NOT NULL"),
           new table_field("date_current", "INTEGER"),
           new table_field("attachment_ref", "STRING", "NOT NULL"),
           new table_field("attachment_type", "INTEGER", "NOT NULL")
    );

    public static final table users = new table("users",
            new table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table_field("remote_id", "INTEGER", "UNIQUE"),
            new table_field("lastname", "STRING"),
            new table_field("firstname", "STRING"),
            new table_field("login", "STRING"),
            new table_field("hashed_pw", "STRING"),
            new table_field("server", "STRING")
    );

    public static final table experiments = new table("experiments",
            new table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table_field("remote_id", "INTEGER", "UNIQUE"),
            new table_field("user_id", "INTEGER", "NOT NULL"),
            new table_field("name", "STRING", "NOT NULL"),
            new table_field("description", "STRING"),
            new table_field("date_creation", "INTEGER")
    );

    public static final table projects = new table("projects",
            new table_field("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT NOT NULL"),
            new table_field("remote_id", "INTEGER", "UNIQUE"),
            new table_field("user_id", "INTEGER", "NOT NULL"),
            new table_field("name", "STRING", "NOT NULL"),
            new table_field("description", "STRING"),
            new table_field("date_creation", "INTEGER")
    );

    //Define Sqlite Statements for later use in Prepared Statements
    //These are more of an idea and a basis for discussion right now and should not yet be used
    //private static final String DATABASE_updateFooString = "SELECT * FROM TEST WHERE id = ?";

}
