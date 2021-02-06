package com.Codeminers.allInOne.free.videodownloader.statussaver.model;

public class CommonModel {
    String imgurl;
    String videopath;
    String upath;
    String title;

    public String getImagePath() {
        return this.imgurl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getVideoPath() {
        return this.videopath;
    }

    public String getVideoUniquePath() {
        return this.upath;
    }

    public void setImagePath(String str) {
        this.imgurl = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setVideoPath(String str) {
        this.videopath = str;
    }

    public void setVideoUniquePath(String str) {
        this.upath = str;
    }
}
