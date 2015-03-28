package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class low_level_adapter extends SQLiteOpenHelper {
    public static final layout db_layout = new layout();

    //always call interface ready before doing anything else!
    //once patching is in place, initalizing the db might take some time
    //and we need to wait for this
    public boolean interface_ready(){
        return true;
    };

    public low_level_adapter(Context context) {
        super(context, this.db_layout.NAME, null, this.db_layout.VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        for (table tmp : this.db_layout.tables) {
            db.execSQL(tmp.getSqliteDescription());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no patching yet, we jsut drop the table
        //for release, we will need a patching system in place
        for (table tmp : this.db_layout.tables) {
            db.execSQL("DROP TABLE " + tmp.getName() + ";" );
        }
        this.onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (table tmp : this.db_layout.tables) {
            db.execSQL("DROP TABLE " + tmp.getName() + ";" );
        }
        this.onCreate(db);
    }
}