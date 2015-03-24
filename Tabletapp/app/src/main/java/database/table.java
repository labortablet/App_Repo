package database;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class table {
    private String name;
    private List<table_field> fields;
    private int size;
    private static final String prefix = "CREATE TABLE IF NOT EXISTS ";

    table(String name, table_field...fields){
        this.name = name;
        this.fields = new LinkedList<table_field>();
        this.size = name.length() + prefix.length() + 3 - 1;
        //+3 for "(...);"  -1 for the last "," which is added in the loop but is not needed
        for (table_field tmp : fields) {
            this.fields.add(tmp);
            this.size += tmp.getSize() + 1;
        }
    };

    String getName(){
        return this.name;
    };

    List<table_field> getFields(){
        return this.fields;
    };

    public String getSqliteDescription(){
        StringBuilder tmp = new StringBuilder(this.size);
        tmp.append(this.prefix);
        tmp.append(this.name);
        tmp.append("(");
        Iterator<table_field> it = fields.iterator();
        table_field temp = null;
        while(it.hasNext()){
            temp = it.next();
            tmp.append(temp.getSqliteDescription());
            if(it.hasNext()){
                tmp.append(",");
            }
        }
        tmp.append(");");
        return tmp.toString();
    };

}
