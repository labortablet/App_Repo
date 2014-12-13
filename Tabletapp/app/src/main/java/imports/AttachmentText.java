package imports;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentText extends AttachmentBase {

   String Attachment;
	public AttachmentText(String text){
        this.Attachment = text;
	}

    @Override
    public String getAttachment() {
        return this.Attachment;
    }

}
