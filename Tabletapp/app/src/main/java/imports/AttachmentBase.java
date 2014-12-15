package imports;

import java.util.Objects;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentBase implements AttachmentInterface {
    String attachment;

    public Object getContent() {
        return null;
    }
    public AttachmentBase(String string)
    {
        this.attachment = string;
    }
public AttachmentBase (AttachmentBase a)
{this.attachment = a.attachment; }
}



