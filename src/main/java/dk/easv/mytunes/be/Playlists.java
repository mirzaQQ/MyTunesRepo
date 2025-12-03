package dk.easv.mytunes.be;

public class Playlists {
    private int Playlist_id;
    private String Name;
    private String TotalTime;
    private int SongsNumber;

    public Playlists(int Playlist_id, String Name, int  SongsNumber, String TotalTime) {
        this.Playlist_id = Playlist_id;
        this.Name = Name;
        this.SongsNumber = SongsNumber;
        this.TotalTime = TotalTime;
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
    public String getTotalTime(){
        return TotalTime;
    }
    public void setTotalTime(String TotalTime){
        this.TotalTime = TotalTime;
    }
    public int getSongsNumber(){
        return SongsNumber;
    }
    public void setSongsNumber(int SongsNumber){
        this.SongsNumber = SongsNumber;
    }

}
