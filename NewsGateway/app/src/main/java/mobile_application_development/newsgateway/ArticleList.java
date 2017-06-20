package mobile_application_development.newsgateway;

import java.io.Serializable;

/**
 * Created by devikabeniwal on 04/05/17.
 */

public class ArticleList implements Serializable {

    private String id;
    private String author;
    private String title;
    private String description;
    private String imageUrl;
    private String url;
    private String publishedAt;

    public ArticleList(String ident,String au,String tit,String des,String img,String url,String time){
        this.id = ident;
        this.author = au;
        this.title = tit;
        this.description = des;
        this.imageUrl = img;
        this.url = url;
        this.publishedAt = time;
    }

    public  String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public  String getAuthor() {
        return author;
    }

    public void setAuthor(String auth) {
        this.author = auth;
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String tit) {
        this.title = tit;
    }

    public  String getDescription() {
        return description;
    }

    public void setDescription(String des) {
        this.description = des;
    }

    public  String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String img) {
        this.imageUrl = img;
    }

    public  String getUrl() {
        return url;
    }

    public void setUrl(String ur) {
        this.url = ur;
    }

    public  String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String time) {
        this.publishedAt = time;
    }


}
