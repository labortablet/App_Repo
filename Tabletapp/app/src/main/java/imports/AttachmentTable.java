package imports;

//FIXME this should really be its won class handling its own problems and not use AttachmentText
public class AttachmentTable extends AttachmentText{
    public AttachmentTable(String text){
        super(text);
    }

    @Override
    public int getTypeNumber(){return 2;};

    /**
     * Returns the 2D Array, which holds the content of the Table
     * @return   table_array
     */



/*
    public String getText() {
        String finalstr ="";
        for (String[] s : table_array) {
            String temp = "";
            for (String string : s) {
                temp += (string + ",");
            }
            finalstr += temp.substring(0, temp.length() - 1);
            finalstr += ";";
        }
        return finalstr;
    }

    public void StringTo2DArray(String strings){
        String[] string = strings.split(";");
        int x = countLetter(string[0],",");
        int y = string.length;
        String[][] temp =new String[y][x];
        for (int i = 0; i < y ; i++) {
            for (int j = 0;j < x;j++) {
                int pos = string[y].indexOf(",");
                temp[i][j] = string[y].substring(pos);
                string[y] = string[y].substring(pos+1,strings.length());
            }
        }
        table_array = temp;
    }
    private static int countLetter(String str, String letter) {
        int count = 0;
        for (int pos = -1; (pos = str.indexOf(letter, pos+1)) != -1; count++);
        return count;
    }*/
}
