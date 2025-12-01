package dk.easv.mytunes.be;

public class Songs {
    private String Title;
    private String Artist;
    private int Category;
    private String time;
    private String Filepath;
    private int Song_id;

    public Songs(String Title, String Artist, int Category, String time, String Filepath,  int Song_id) {
        this.Title = Title;
        this.Artist = Artist;
        this.Category = Category;
        this.time = time;
        this.Filepath = Filepath;
        this.Song_id = Song_id;

    }

    public Songs(String Title, String Artist, int Category) {
        this.Title = Title;
        this.Artist = Artist;
        this.Category = Category;

    }
    public Songs(String Title, String Artist) {
    this.Title = Title;
    this.Artist = Artist;
    }
    public Songs(String Title) {
        this.Title = Title;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String Title) {
        this.Title = Title;
    }
    public String getArtist() {
        return Artist;
    }
    public void setArtist(String Artist) {
        this.Artist = Artist;
    }
    public int getCategory() {
        return Category;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getFilepath() {
        return Filepath;
    }
    public void setFilepath(String Filepath) {
        this.Filepath = Filepath;
    }
    public int getSong_id() {
        return Song_id;
    }

}
