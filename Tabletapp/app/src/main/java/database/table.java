package database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class table {
    private String name;
    private List<table_field> fields;
    private int size;
    private static final String prefix = "CREATE TABLE IF NOT EXISTS ";
    private HashMap<String,table_field> field_by_name = new HashMap();

    table(String name, table_field...fields){
        this.name = name;
        this.fields = new LinkedList<table_field>();
        this.size = name.length() + prefix.length() + 3 - 1;
        //+3 for "(...);"  -1 for the last "," which is added in the loop but is not needed
        for (table_field tmp : fields) {
            this.fields.add(tmp);
            this.size += tmp.getSize() + 1;
            this.field_by_name.put(tmp.getName(), tmp);
        }
    };

    String getName(){
        return this.name;
    };

    List<table_field> getFields(){
        return this.fields;
    };

    table_field getField(String field_name){
        return this.field_by_name.get(field_name);
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

    public static class table_field {
        private String name;
        private String type;
        private String extra_options;
        private int size;

        public table_field(String name, String type) {
            this.name = name;
            this.extra_options = null;
            this.size = this.name.length();
            if(this.type != null){
                this.size += this.type.length() + 1;
            }
            if(this.extra_options != null){
                this.size += this.extra_options.length() + 1;
            }
        }

        public table_field(String name, String type, String extra_options) {
            this.name = name;
            this.type = type;
            this.type = type.toUpperCase();
            if(!this.type.matches("INTEGER|STRING|TEXT|NUMBER|BLOB")){
                throw new IllegalArgumentException("Wrong sqlite type");
            }
            this.extra_options = extra_options;
        }

        public String getName(){
            return this.name;
        };

        public String getType(){
            return this.type;
        };

        public String getExtra_options(){
            return this.extra_options;
        };

        public int getSize(){
            return this.size;
        };

        public String getSqliteDescription(){
            StringBuilder tmp = new StringBuilder(this.size);
            tmp.append(this.name);
            if(this.type != null){
                tmp.append(" ");
                tmp.append(this.type);
            }
            if(this.extra_options != null){
                tmp.append(" ");
                tmp.append(this.extra_options);
            }
            return tmp.toString();
        };
    }
}
