package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

import java.util.List;

public interface ChannelService {

    Channel getChannelById(int id);

    List<String> getChannelsByPlist(String name);

    List<String> getChannelsByCategory(String plname, String catname);

    List<String> getChannelsUrlByCategory(String plname, String catname);

    List<String> getChannelsUrlByPlist(String name);

    void insertAllChannels(List<Channel> channels, String plist);

    void deleteChannelbyPlist(String plist);

    void clearAllChannel();

    int sizeOfChannelList();

    List<String> getCategoriesList(String name);

    String getCategoryById(String name, int id);

    int getCategoriesNumber(String name);

    String getChannelsUrlByPlistAndName(String plist, String name);

    void openChannel(String chName);

    void openURL(final String chURL);

}
