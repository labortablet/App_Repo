package datastructures;


public class AttachmentText extends AttachmentBase {
    private String TextContent;

    public String serialize(){
        return this.TextContent;
    };
	public AttachmentText(String text){
        this.TextContent = text;
	}
    public int getTypeNumber(){return 1;};
    public String getContent() {
        return this.TextContent;
    }
    public String getReference() {return this.TextContent;}


}
