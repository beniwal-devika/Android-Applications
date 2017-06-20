package mobile_application_development.multinotes;

import java.io.Serializable;

/**
 * Created by devikabeniwal on 24/02/17.
 */

public class Notes implements Serializable { // Needed to add as extra{
    private String title;
    private String notes;
    private String datetime;

    public Notes(String tl, String dt, String no) {
        title = tl;
        notes = no;
        datetime = dt;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String note) {
        this.notes = note;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
