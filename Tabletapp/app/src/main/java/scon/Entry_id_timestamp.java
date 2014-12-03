package scon;

public class Entry_id_timestamp{
    private Integer id;
    private Long last_change;

    public Entry_id_timestamp(Integer id, Long last_change){
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



