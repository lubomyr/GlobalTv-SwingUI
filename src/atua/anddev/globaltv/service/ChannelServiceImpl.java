package atua.anddev.globaltv.service;

import java.util.ArrayList;
import java.util.List;

import atua.anddev.globaltv.Player;
import atua.anddev.globaltv.entity.Channel;

public class ChannelServiceImpl implements ChannelService {

    @Override
    public List<String> getCategoriesList() {
        List<String> arr = new ArrayList<String>();
        boolean cat_exist = false;
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
    public int indexNameForChannel(String name) {
        return channelName.indexOf(name);
    }

    @Override
    public Channel getChannelById(int id) {
        return channel.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        List<Channel> arr = new ArrayList<Channel>();
        for (Channel chn : channel) {
            arr.add(chn);
        }
        return arr;
    }

    @Override
    public void addToChannelList(String name, String url, String category) {
        channel.add(new Channel(name, url, category));
        channelName.add(name);
    }

    @Override
    public void clearAllChannel() {
        channel.clear();
        channelName.clear();
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
