package imports;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentText extends AttachmentBase {

	public AttachmentText(String text){
        super(text);
	}

    @Override
    public String getContent() {
        return this.attachment;
    }

}
