package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Player;
import atua.anddev.globaltv.Services;
import atua.anddev.globaltv.entity.Channel;

import java.util.List;

public class ChannelServiceImpl implements ChannelService, Services {

    @Override
    public String getCategoryById(String name, int id) {
        return getCategoriesList(name).get(id);
    }

    @Override
    public String getChannelsUrlByPlistAndName(String plist, String name) {
        return channelDb.getChannelsUrlByPlistAndName(plist, name);
    }

    @Override
    public List<String> getChannelsByCategory(String plname, String catname) {
        return channelDb.getChannelsByCategory(plname, catname);
    }

    @Override
    public List<String> getChannelsUrlByCategory(String plname, String catname) {
        return channelDb.getChannelsUrlByCategory(plname, catname);
    }


    @Override
    public List<String> getChannelsUrlByPlist(String name) {
        return channelDb.getChannelsUrlByPlist(name);
    }

    @Override
    public void insertAllChannels(List<Channel> channels, String plist) {
        channelDb.insertAllChannels(channels, plist);
    }

    @Override
    public void deleteChannelbyPlist(String plist) {
        channelDb.deleteChannelbyPlist(plist);
    }

    @Override
    public List<String> getChannelsByPlist(String name) {
        return channelDb.getChannelsByPlist(name);
    }

    @Override
    public int getCategoriesNumber(String name) {
        return channelDb.getCategoriesList(name).size();
    }

    @Override
    public List<String> getCategoriesList(String name) {
        return channelDb.getCategoriesList(name);
    }

    @Override
    public Channel getChannelById(int id) {
        List<Integer> idList = channelDb.getAllChannelId();
        int iddb = idList.get(id);
        return channelDb.getChannelById(iddb);
    }

    @Override
    public void clearAllChannel() {
        channelDb.deleteAllChannels();
    }

    @Override
    public int sizeOfChannelList() {
        return channelDb.numberOfRows();
    }

    public void openChannel(String chName) {
        for (int i = 0; i < sizeOfChannelList(); i++) {
            if (chName.equals(getChannelById(i).getName())) {
                openURL(getChannelById(i).getUrl());
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
