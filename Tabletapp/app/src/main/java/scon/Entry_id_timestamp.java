package scon;

import java.sql.Timestamp;

import exceptions.ArraySizeMismatch;

public class Entry_id_timestamp{
    private Integer[] ids;
    private Timestamp[] timestamps;

    public Entry_id_timestamp(Integer[] ids, Timestamp[] timestamps) throws ArraySizeMismatch {
        if(!(ids.length == timestamps.length)){
            throw new ArraySizeMismatch();
        }
        this.ids = ids;
        this.timestamps = timestamps;
    }

    public Integer[] getIds(){
        return this.ids;
    }

    public Timestamp[] getTimestamps(){
        return this.timestamps;
    }

}
