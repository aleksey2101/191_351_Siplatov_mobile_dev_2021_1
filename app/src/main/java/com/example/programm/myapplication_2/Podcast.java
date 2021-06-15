package com.example.programm.myapplication_2;

public class Podcast {
    private String podcastName;

    // Image name (Without extension)
    private String imgLink;
    private String url;

    public Podcast(String podcastName, String imgLink, String url) {
        this.podcastName = podcastName;
        this.imgLink = imgLink;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    @Override
    public String toString()  {
        return this.podcastName +" (Population: "+ this.url +")";
    }
}
