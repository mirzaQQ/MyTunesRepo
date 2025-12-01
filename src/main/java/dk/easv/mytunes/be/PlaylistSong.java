package dk.easv.mytunes.be;

public class PlaylistSong {
    private int Playlists_id;
    private int Songs_id;
    private int Position;

    public PlaylistSong(int Playlists_id, int Songs_id, int Position){
        this.Playlists_id = Playlists_id;
        this.Songs_id = Songs_id;
        this.Position = Position;
    }

    public int getPlaylists_id() {
        return Playlists_id;
    }
    public int  getSongs_id() {
        return Songs_id;
    }
    public int getPosition() {
        return Position;
    }
    public void setPosition(int Position) {
        this.Position = Position;
    }
}
