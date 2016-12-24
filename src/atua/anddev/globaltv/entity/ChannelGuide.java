package atua.anddev.globaltv.entity;

public class ChannelGuide {
    private String id;
    private String lang;
    private String displayName;

    public ChannelGuide(String id, String lang, String displayName) {
        this.id = id;
        this.lang = lang;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
