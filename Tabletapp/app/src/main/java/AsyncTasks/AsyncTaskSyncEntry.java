package AsyncTasks;

import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;

import company.DBAdapter;
import exceptions.SBSBaseException;
import imports.AttachmentTable;
import imports.AttachmentText;
import imports.BaseEntry;
import imports.CursorSDS;
import imports.LocalEntry;
import imports.Project;
import imports.User;
import scon.Entry_id_timestamp;
import scon.ServerDatabaseSession;

/**
 * Created by Grit on 17.12.2014.
 */
public class AsyncTaskSyncEntry extends AsyncTask<CursorSDS,Integer,String> {


    @Override
    protected String doInBackground(CursorSDS... params) {
        CursorSDS cursorSDS = params[0];
        Cursor cursor = cursorSDS.getCursor();
        DBAdapter myDb = cursorSDS.getMyDB();
        ServerDatabaseSession SDS = cursorSDS.getSDS();
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                try {
                    Entry_id_timestamp entry_id_timestamp = SDS.send_entry(new BaseEntry(cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntryCreationDate), cursor.getString(DBAdapter.COL_EntryTitle), cursor.getInt(DBAdapter.COL_EntryTyp), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent))));
                    if (entry_id_timestamp.getId() != null) {
                        myDb.open();
                        myDb.updateEntryAfterSync(entry_id_timestamp);
                        myDb.close();
                    } else
                        break;

                } catch (SBSBaseException e) {
                    e.printStackTrace();
                }

           /*     switch (cursor.getInt(DBAdapter.COL_EntryTyp)) {
                    case 1:
                        if (cursor.getInt(DBAdapter.COL_EntrySync) == 1)
                         //   entries.add(new LocalEntry(cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentText(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getLong(DBAdapter.COL_EntryCreationDate), new User(cursor.getString(DBAdapter.COL_EntryUserID)), false, cursor.getInt(DBAdapter.COL_EntryID), cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntrySync), cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                        break;
                    case 2:
                         //   entries.add(new LocalEntry(cursor.getString(DBAdapter.COL_EntryTitle), new AttachmentTable(cursor.getString(DBAdapter.COL_EntryContent)), cursor.getInt(DBAdapter.COL_EntryTyp), cursor.getLong(DBAdapter.COL_EntryCreationDate), new User(cursor.getString(DBAdapter.COL_EntryUserID)), false, cursor.getInt(DBAdapter.COL_EntryID), cursor.getInt(DBAdapter.COL_EntryExperimentID), cursor.getLong(DBAdapter.COL_EntrySync), cursor.getLong(DBAdapter.COL_EntryChangeDate)));
                        break;
                }*/

            } while (cursor.moveToNext());
        }
        // Close the cursor to avoid a resource leak.
        cursor.close();
        return null;
    }
}
