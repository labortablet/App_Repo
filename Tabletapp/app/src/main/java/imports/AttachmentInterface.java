package imports;


/**
 * Created by Grit on 08.12.2014.
 */
public interface AttachmentInterface {
public Object getContent();
//Nur so als Vorschlag
//public int getTypeNumber(); 0->Text, 1->Table, 2->Image
//public byte[] serialize(); -> Content wird zu bytes damit ich es Übertragen kann oder du es in die DB packen kansnt
//vlt wäre eien Abstrakte Klasse aber besser, bin mir da noch etwas uneinig, Freddy

}
