package imports;

public class AttachmentBase implements AttachmentInterface {
    String attachment;
    public static AttachmentBase deserialize(int attachment_type, byte[] attachment_serialized) {
        switch (attachment_type) {
            case 0:  return new AttachmentText(attachment_serialized);
            /*case 1:  return new AttachmentTable(attachment_serialized);
            case 2:  return new AttachmentImage(attachment_serialized);
            */
            default: return new AttachmentText(attachment_serialized);
        }
    }
    
    public Object getContent() {
        return null;
    }
    public AttachmentBase(String string)
    {
        this.attachment = string;
    }
    public AttachmentBase(byte[] string)
    {
        this.attachment = String.valueOf(string);
    }
public AttachmentBase (AttachmentBase a)
{this.attachment = a.attachment; }
    public int getTypeNumber(){return 0;};
}



