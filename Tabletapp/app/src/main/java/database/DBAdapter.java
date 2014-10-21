package database;

/**
 * Created by Grit on 26.06.2014.
 */
/*
public class DBAdapter extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static final String DB_NAME = "lablet.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String Create_Table_User = "CREATE TABLE IF NOT EXISTS _user ( User_ID INTEGER PRIMARY KEY AUTOINCREMENT , User_EMail TEXT NOT NULL, User_Password TEXT NOT NULL) ;";
    private static DBAdapter mDBConnection;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     *
    private DBAdapter(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = "/data/data/"
                + context.getApplicationContext().getPackageName()
               + "/databases/";
        // The Android's default system path of your application database is
        // "/data/data/mypackagename/databases/"
    }

    /**
     * getting Instance
     * @param context
     * @return DBAdapter
     *
    public static synchronized DBAdapter getDBAdapterInstance(Context context) {
        if (mDBConnection == null) {
            mDBConnection = new DBAdapter(context);
        }
        return mDBConnection;
    }

    /**
     * Creates an empty database on the system and rewrites it with your own database.
     **
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
        } else {
            // By calling following method
            // 1) an empty database will be created into the default system path of your application
            // 2) than we overwrite that database with our database.
            this.getReadableDatabase();
         /*   try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }*
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     *
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * *
    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Open the database
     * @throws SQLException
     *
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    /**
     * Close the database if exist
     *
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /**
     * Call on creating data base for example for creating tables at run time
     *
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_User);
    }

    /**
     * can used for drop tables then call onCreate(db) function to create tables again - upgrade
     *
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // ----------------------- CRUD Functions ------------------------------





    }*/