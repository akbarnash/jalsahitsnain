package app.com.jalsahitsnain.models;

import com.google.firebase.database.Exclude;

public class SongModel {
    public String songTitle, songDuration, songLink, mKey;

    public SongModel(){

    }

    public SongModel(String songTitle, String songDuration, String songLink) {
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.songLink = songLink;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
