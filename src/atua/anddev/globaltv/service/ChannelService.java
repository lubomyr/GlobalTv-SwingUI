package atua.anddev.globaltv.service;

import java.util.ArrayList;
import java.util.List;

import atua.anddev.globaltv.entity.Channel;

public interface ChannelService {
    List<Channel> channel = new ArrayList<Channel>();
    List<String> channelName = new ArrayList<String>();

    List<Channel> getAllChannels();

    void addToChannelList(String name, String url, String category);

    Channel getChannelById(int id);

    void clearAllChannel();

    int sizeOfChannelList();

    int indexNameForChannel(String name);

    List<String> getCategoriesList();

    void openChannel(String chName);

    void openURL(final String chURL);
}
