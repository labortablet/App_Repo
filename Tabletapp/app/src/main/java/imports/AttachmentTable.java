package imports;

/**
 * Created by Hawky on 18.08.2014.
 */
public class AttachmentTable extends AttachmentBase{
	/**This is a 2D array which holds the content of the Table.
	 *
	 * @value
	 * @since 1.0
	 */


	private String[][] table_array;
	/**
	 * Returns the 2D Array, which holds the content of the Table
	 * @return   table_array
	 */
	public String[][] getTable_array(){
		return table_array;
	}

	public AttachmentTable(String[][] array){
		this.table_array = array;
	}
}
