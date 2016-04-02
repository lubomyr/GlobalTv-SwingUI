package atua.anddev.globaltv.entity;

public class Channel {
    private String name;
    private String url;
    private String category;

    public Channel(String name, String url, String category) {
        this.name = name;
        this.url = url;
        this.category = category;
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
