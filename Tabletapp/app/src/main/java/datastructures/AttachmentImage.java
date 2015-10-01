package datastructures;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentImage extends AttachmentText {
    public AttachmentImage(String titel) {
        super(titel);
    }
    @Override
    public int getTypeNumber(){return 3;}
}
