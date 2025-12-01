package dk.easv.mytunes.be;

public class Playlists {
    private int Playlist_id;
    private String Name;

    public Playlists(int Playlist_id, String Name){
        this.Playlist_id = Playlist_id;
        this.Name = Name;
    }
    public Playlists(String Name){
        this.Name = Name;
    }
    public int getPlaylist_id() {
        return Playlist_id;
    }
    public String getName(){
        return Name;
    }
    public void setName(String Name){
        this.Name = Name;
    }

}
