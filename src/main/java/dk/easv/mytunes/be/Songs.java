package dk.easv.mytunes.be;

public class Songs {
    private String Title;
    private String Artist;
    private String Category;
    private String time;
    private String Filepath;
    private int Song_id;

    public Songs(String Title, String Artist, String Category, String Time, String Filepath, int Song_id) {
        this.Title = Title;
        this.Artist = Artist;
        this.Category = Category;
        this.time = Time;
        this.Filepath = Filepath;
        this.Song_id = Song_id;

    }

    public Songs(String Title, String Artist, String Category) {
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
    public String getArtist() {
        return Artist;
    }
    public String getCategory() {
        return Category;
    }
    public String getTime() {
        return time;
    }
    public String getFilepath() {
        return Filepath;
    }
    public int getSong_id() {
        return Song_id;
    }

}
