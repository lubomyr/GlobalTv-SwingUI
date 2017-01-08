package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Player;
import atua.anddev.globaltv.entity.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelServiceImpl implements ChannelService {

    @Override
    public List<String> getCategoriesList() {
        List<String> arr = new ArrayList<>();
        boolean cat_exist;
        for (int i = 0; i < channel.size() - 1; i++) {
            cat_exist = false;
            for (int j = 0; j <= arr.size() - 1; j++)
                if (channel.get(i).getCategory().equalsIgnoreCase(arr.get(j)))
                    cat_exist = true;
            if (!cat_exist && !channel.get(i).getCategory().equals(""))
                arr.add(channel.get(i).getCategory());
        }
        return arr;
    }

    @Override
    public Channel getChannelById(int id) {
        return channel.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channel;
    }

    @Override
    public void addToChannelList(String name, String url, String category) {
        channel.add(new Channel(name, url, category));
    }

    @Override
    public void clearAllChannel() {
        channel.clear();
    }

    @Override
    public int sizeOfChannelList() {
        return channel.size();
    }

    public void openChannel(String chName) {
        for (Channel chn : channel) {
            if (chName.equals(chn.getName())) {
                openURL(chn.getUrl());
                return;
            }
        }
    }

    @Override
    public void openURL(final String chURL) {
        new Thread(new Runnable() {
            public void run() {
                new Player(chURL);
            }
        }).start();
    }
}
