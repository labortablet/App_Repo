package imports;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Grit on 07.09.2014.
 */
public class BaseEntry {

    /**
     *This is the Remote ID an LocalEntry can get if it comes from the Server.
     * @value
     * @since 1.0
     */
    public Integer remote_id;



    /**
     *This is the title of an LocalEntry
     * @value
     * @since 1.0
     */

    protected Integer Experiment_id;
    /**
     *This is the title of an LocalEntry
     * @value
     * @since 1.0
     */
    protected String title;
    /**
     *This is the Attachment Object Where every entry hold its content.
     * @value
     * @since 1.0
     */
    protected AttachmentBase attachment; //no access yet
    /**
     *This is the Attachment Typ every LocalEntry got.
     * @value
     * @since 1.0
     */
    protected int attachment_type = 0; //no access yet
    /**
     *This is the sync_time a entry only got if it's synced with the Server, this means that it is the time when the server received the LocalEntry.
     * @value
     * @since 1.0
     */
    public Long sync_time;
    /**
     *This is the entry_time it holds the time, the LocalEntry was finished in the app.
     * @value
     * @since 1.0
     */
    protected Long entry_time;
    /**
     *This Value holds the UserObject.
     * @value
     * @since 1.0
     */
    protected Long change_time;
    /**
     *This Value holds the UserObject.
     * @value
     * @since 1.0
     */
    protected User user;
    public BaseEntry(Integer experiment_id, Long entry_time,String title, Integer attachment_type,AttachmentBase attachmentBase){
        this.Experiment_id = experiment_id;
        this.entry_time = entry_time;
        this.title = title;
        this.attachment_type = attachment_type;
        this.attachment =attachmentBase;

    }

    public Long  getChange_time() {
        return change_time;
    }

    public Long  getSync_time() {
        return sync_time;
    }

    public Long  getEntry_time() {
        return entry_time;
    }

    public Integer getExperiment_id() {
        return Experiment_id;
    }

    public Integer getRemote_id() {
        return remote_id;
    }

    public String getTitle() {
        return title;
    }

    public AttachmentBase getAttachment() {
        return attachment;
    }

    public int getAttachment_type() {
        return attachment_type;
    }

    public User getUser() {
        return user;
    }
}