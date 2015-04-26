package datastructures;


public class AttachmentText extends AttachmentBase {
    private String TextContent;

    public String serialize(){
        return this.TextContent;
    };
	public AttachmentText(String text){
        this.TextContent = text;
	}
    public AttachmentText(String text, int a){
        //TODO
        //this is a hack
        //we need to construct attachments both from references as well as serialized strings
        //so I am using a second constructor for the references
        this.TextContent = text;
    }
    public int getTypeNumber(){return 1;};
    public String getContent() {
        return this.TextContent;
    }
    public String getReference() {return this.TextContent;}


}
