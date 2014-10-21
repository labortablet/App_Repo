package imports;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentText extends AttachmentBase{
	private String text;

	public AttachmentText(String text){
		this.text = text;
	};

    public String getText() {
        return text;
    }
};
