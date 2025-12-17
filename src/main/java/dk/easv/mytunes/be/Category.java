package dk.easv.mytunes.be;

public class Category {
    private int  Category_id;
    private String Name;

    public Category(int Category_id, String Name) {
        this.Category_id = Category_id;
        this.Name = Name;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
}
