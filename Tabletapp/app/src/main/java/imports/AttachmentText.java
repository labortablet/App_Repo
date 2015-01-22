package imports;

public class AttachmentText extends AttachmentBase {

	public AttachmentText(String text){
        super(text);
	}

    public static int getTypeNumber(){return 0;};

    //das hier ist etwas merkwürdig, warum macht AttachmentBase den bitte Strings?
    public AttachmentText(byte[] serialized){
        super(serialized.toString());
    }

    @Override
    public String getContent() {
        return this.attachment;
    }

}
