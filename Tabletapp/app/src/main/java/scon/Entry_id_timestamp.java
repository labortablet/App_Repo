package scon;







        import java.util.Calendar;



public class Entry_id_timestamp{





        private Integer id;

       private Calendar created;

       private Calendar last_change;





           public Entry_id_timestamp(Integer id, Calendar created, Calendar last_change){

                   this.id = id;

                   this.created = created;

                   this.last_change = last_change;

        }




               public Integer getId(){

                      return this.id;

            }




                  public Calendar getCreated(){

                           return this.created;

                }



                    public Calendar getLast_change() {

                           return this.last_change;

                       }

            }



