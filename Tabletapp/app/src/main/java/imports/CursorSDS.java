package imports;

import android.database.Cursor;

import scon.ServerDatabaseSession;

/**
 * Created by Grit on 17.12.2014.
 */
public class CursorSDS {
    private Cursor cursor;
    private ServerDatabaseSession SDS;
    private DBAdapter myDB;

    public DBAdapter getMyDB() {
        return myDB;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public ServerDatabaseSession getSDS() {
        return SDS;
    }

    public CursorSDS(Cursor cursor, ServerDatabaseSession serverDatabaseSession,DBAdapter myDB){
        this.cursor = cursor;
        this.SDS = serverDatabaseSession;
this.myDB = myDB;
    }
}
