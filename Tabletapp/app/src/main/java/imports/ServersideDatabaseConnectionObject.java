package imports;

import android.content.Context;

import company.DBAdapter;
import scon.ServerDatabaseSession;

/**
 * Created by Grit on 16.12.2014.
 */
public class ServersideDatabaseConnectionObject {

   private ServerDatabaseSession SDS;
   private DBAdapter myDB;
    public ServersideDatabaseConnectionObject(ServerDatabaseSession SDS,DBAdapter myDB)
    {
        this.myDB = myDB;
        this.SDS = SDS;

    }

    public ServerDatabaseSession getSDS() {
        return SDS;
    }

    public DBAdapter getMyDB() {
        return myDB;
    }
}
