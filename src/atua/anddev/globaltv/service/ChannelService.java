package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

import java.util.ArrayList;
import java.util.List;

public interface ChannelService {
    List<Channel> channels = new ArrayList<>();

    List<Channel> getAllChannels();

    void addToChannelList(Channel chn);

    Channel getChannelById(int id);

    void clearAllChannel();

    int sizeOfChannelList();

    List<String> getCategoriesList();

    void openChannel(Channel channel);

    void openURL(final String chURL);

    String getUpdatedUrl(String chName, int provId);
}
