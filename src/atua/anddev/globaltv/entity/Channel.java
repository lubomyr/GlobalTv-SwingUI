package atua.anddev.globaltv.entity;

public class Channel {
    private String name;
    private String url;
    private String category;
    private String plist;

    public Channel(String name, String url, String category) {
        this.name = name;
        this.url = url;
        this.category = category;
        this.plist = null;
    }

    public Channel(String name, String url, String category, String plist) {
        this.name = name;
        this.url = url;
        this.category = category;
        this.plist = plist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
