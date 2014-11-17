package scon;

public class Entry_id_timestamp{
       private Integer id;
       private Long created;
       private long last_change;

           public Entry_id_timestamp(Integer id, long created, long last_change){

                   this.id = id;
                   this.created = created;
                    this.last_change = last_change;
        }
          public Integer getId(){
                      return this.id;
            }
                  public long getCreated(){
                           return this.created;
                }
                      public long getLast_change() {
                           return this.last_change;
                       }
            }



