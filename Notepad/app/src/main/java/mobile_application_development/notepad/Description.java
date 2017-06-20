package mobile_application_development.notepad;

/**
 * Created by devikabeniwal on 11/02/17.
 */

public class Description {

        private String text;
        private String saved_datetime;

        public String getNotes() {
            return text;
        }

        public void setNotes(String name) {
            this.text = name;
        }

        public String getLastUpdateDatetime() {
            return saved_datetime;
        }

        public void setLastUpdateDatetime(String description) {
            this.saved_datetime = description;
        }

}
