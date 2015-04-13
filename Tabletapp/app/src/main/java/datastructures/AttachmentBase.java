package datastructures;

public abstract class AttachmentBase {
    public static AttachmentBase deserialize(int attachment_type, String attachment_serialized) {
        switch (attachment_type) {
            case 1:
                return new AttachmentText(attachment_serialized);
            case 2:
                return new AttachmentTable(attachment_serialized);
            /*case 3:  return new AttachmentImage(attachment_serialized);
            */
            default:
                return new AttachmentText(attachment_serialized);
        }
    }

    public abstract Object getContent(); //FIXME der return Wert hier solle nicht Object sein!!!

    public int getTypeNumber() {
        return -1;
    };

    public abstract String serialize();
    //this is the serialized data used by scon

    public abstract String getReference();
    //this is a short local reference which can be used to reconstruc the attachment
    //for example this could be the name of a picture or a video or a string for a text entry
}





