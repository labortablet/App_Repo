package database;

public class table_field {
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
        if(!this.type.matches("INTEGER|STRING|TEXT|NUMBER")){
            throw new IllegalArgumentException("Wrong type for sqlite type");
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
