package datastructures;

public class Entry_Remote_Identifier {
    private Integer id;
    private Long last_change;

    public Entry_Remote_Identifier(Integer id, Long last_change){
        this.id = id;
        this.last_change = last_change;
    }

    public Integer getId() {
        return this.id;
    }

    public Long getLast_change() {
        return this.last_change;
    }
}



