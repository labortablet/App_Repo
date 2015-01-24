package imports;

public class AttachmentText extends AttachmentBase {

	public AttachmentText(String text){
        super(text);
	}
   @Override
    public int getTypeNumber(){return 1;}

    //das hier ist etwas merkwürdig, warum macht AttachmentBase den bitte Strings?
    // weil es keinen konstruktor in base gab mit einem byte array
    public AttachmentText(byte[] serialized){
        super(serialized);
    }

    @Override
    public String getContent() {
        return this.attachment;
    }

}
