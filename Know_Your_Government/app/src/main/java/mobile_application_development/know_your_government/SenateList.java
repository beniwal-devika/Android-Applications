package mobile_application_development.know_your_government;

/**
 * Created by devikabeniwal on 04/04/17.
 */

public class SenateList {

    private String designation;
    private String name;
    private String party = "Unknown";

    private String photoUrl = "";
    private String address = "";
    private String phone = "";
    private String email = "";
    private String website = "";

    private String facebook_id = "";
    private String twitter_id = "";
    private String googleplus_id = "";
    private String youtube_id = "";


    public SenateList(String des, String na, String pty, String url, String adr,String ph,String em,String web,String fb,String tw,String gplus,String ytube){
        this.designation = des;
        this.name = na;
        this.party = pty;
        this.photoUrl = url;
        this.address = adr;
        this.phone = ph;
        this.website = web;
        this.email = em;
        this.facebook_id = fb;
        this.twitter_id = tw;
        this.googleplus_id = gplus;
        this.youtube_id = ytube;
    }

    public  String getDesignation() {
        return designation;
    }
    public  String getParty() {
        return party;
    }

    public  String getName() {
        return name;
    }

    public void setDesignation(String des) {
        this.designation = des;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String url) {
        this.photoUrl = url;
    }

    public  String getAddress() {
        return address;
    }

    public void setAddress(String adr) {
        this.photoUrl = adr;
    }

    public  String getPhone() {
        return phone;
    }

    public void setPhone(String ph) {
        this.phone = ph;
    }

    public  String getEmail() {
        return email;
    }

    public  String getFacebook_id() {
        return facebook_id;
    }
    public  String getTwitter_id() {
        return twitter_id;
    }
    public  String getGoogleplus_id() {
        return googleplus_id;
    }
    public  String getYoutube_id() {
        return youtube_id;
    }

    public void setEmail(String em) {
        this.email = em;
    }

    public  String getWebsite() {
        return website;
    }

    public void setWebsite(String web) {
        this.website = web;
    }

    public void setParty(String pty) {
        this.party = pty;
    }

    public void setFacebook_id(String fb) {
        this.facebook_id = fb;
    }
    public void setTwitter_id(String tw) {
        this.twitter_id = tw;
    }
    public void setGoogleplus_id(String gplus) {
        this.googleplus_id = gplus;
    }
    public void setYoutube_id(String ytube) {
        this.youtube_id = ytube;
    }


}
