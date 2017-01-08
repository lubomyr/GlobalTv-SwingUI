package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

import java.util.ArrayList;
import java.util.List;

public interface ChannelService {
    List<Channel> channel = new ArrayList<>();

    List<Channel> getAllChannels();

    void addToChannelList(String name, String url, String category);

    Channel getChannelById(int id);

    void clearAllChannel();

    int sizeOfChannelList();

    List<String> getCategoriesList();

    void openChannel(String chName);

    void openURL(final String chURL);
}
