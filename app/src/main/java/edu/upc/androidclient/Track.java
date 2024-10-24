package edu.upc.androidclient;

public class Track {
    String id;
    String title;
    String singer;

    public Track(String title, String singer) {
        this.title = title;
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
            return singer;
    }

    public String getId() {return id;}

}

