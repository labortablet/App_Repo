package datastructures;

import java.io.Serializable;

public abstract class AttachmentBase implements Serializable{
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

    public static AttachmentBase dereference(int attachment_type, String attachment_serialized) {
        switch (attachment_type) {
            case 1:
                return new AttachmentText(attachment_serialized, 0);
            case 2:
                return new AttachmentTable(attachment_serialized, 0);
            /*case 3:  return new AttachmentImage(attachment_serialized);
            */
            default:
                return new AttachmentText(attachment_serialized, 0);
        }
    }


    public abstract String getContent();
    //function used by the GUI to get the data for displaying

    public int getTypeNumber() {
        return -1;
    };

    public abstract String serialize();
    //this is the serialized data used by scon

    public abstract String getReference();
    //this is a short local reference which can be used to reconstruc the attachment
    //for example this could be the name of a picture or a video or a string for a text entry
}





