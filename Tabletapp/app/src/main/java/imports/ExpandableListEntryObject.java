package imports;

import datastructures.AttachmentBase;

/**
 * Created by Grit on 23.12.2014.
 */
public class ExpandableListEntryObject {
    private AttachmentBase attachmentBase ;
    private Boolean sync;
    private Long createTime;
    private String title;
    public AttachmentBase getAttachmentBase() {
        return attachmentBase;
    }

    public Boolean getSync() {
        return sync;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public String getTitle() {
        return title;
    }

    public ExpandableListEntryObject(AttachmentBase attachmentBase,Boolean sync,Long createTime,String title)
    {
    this.title = title;
    this.attachmentBase = attachmentBase;
    this.sync = sync;
    this.createTime = createTime;
    }
}
