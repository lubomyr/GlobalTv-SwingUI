package atua.anddev.globaltv.service;

import atua.anddev.globaltv.MainActivity;
import atua.anddev.globaltv.Player;
import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.entity.Playlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static atua.anddev.globaltv.Services.playlistService;

public class ChannelServiceImpl implements ChannelService {

    @Override
    public List<String> getCategoriesList() {
        List<String> arr = new ArrayList<>();
        boolean cat_exist;
        for (int i = 0; i < channels.size() - 1; i++) {
            cat_exist = false;
            for (int j = 0; j <= arr.size() - 1; j++)
                if (channels.get(i).getCategory().equalsIgnoreCase(arr.get(j)))
                    cat_exist = true;
            if (!cat_exist && !channels.get(i).getCategory().equals(""))
                arr.add(channels.get(i).getCategory());
        }
        return arr;
    }

    @Override
    public Channel getChannelById(int id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channels;
    }

    @Override
    public void addToChannelList(Channel ch) {
        channels.add(ch);
    }

    @Override
    public void clearAllChannel() {
        channels.clear();
    }

    @Override
    public int sizeOfChannelList() {
        return channels.size();
    }

    @Override
    public void openChannel(Channel channel) {
        for (Channel chn : channels) {
            if (channel.getName().equals(chn.getName())) {
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

    public String getUpdatedUrl(String chName, int provId) {
        Playlist plst = playlistService.getActivePlaylistById(provId);
        try {
            MainActivity.saveUrl(plst.getFile(), plst.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        playlistService.readPlaylist(provId);
        return getChannelsUrl(chName);
    }

    private String getChannelsUrl(String chName) {
        for (Channel chn : channels) {
            if (chName.equals(chn.getName())) {
                return chn.getUrl();
            }
        }
        return null;
    }
}
