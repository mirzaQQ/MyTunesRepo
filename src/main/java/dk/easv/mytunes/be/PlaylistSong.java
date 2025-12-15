package dk.easv.mytunes.be;

public class PlaylistSong {
    private int Playlist_id;
    private int Song_id;
    private int Position;
    private String title;

    public PlaylistSong(int Playlist_id, int Song_id, int Position, String title){
        this.Playlist_id = Playlist_id;
        this.Song_id = Song_id;
        this.Position = Position;
        this.title = title;
    }

    public int getPlaylists_id() {
        return Playlist_id;
    }
    public int  getSongs_id() {
        return Song_id;
    }
    public int getPosition() {
        return Position;
    }
    public String getTitle() {
        return title;
    }
    public void setPosition(int Position) {
        this.Position = Position;
    }
    public String toString(){
        return title;
    }
}
