package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Services;

import java.util.List;

public class SearchServiceImpl implements SearchService, Services {

    @Override
    public List<String> searchChannelsByName(String search) {
        return channelDb.searchChannelsByName(search);
    }

    @Override
    public List<String> searchChannelsProvByName(String search) {
        return channelDb.searchChannelsProvByName(search);
    }

    @Override
    public List<String> searchChannelsUrlByName(String search) {
        return channelDb.searchChannelsUrlByName(search);
    }

    @Override
    public List<String> searchChannelsByPlistAndName(String plist, String search) {
        return channelDb.searchChannelsByPlistAndName(plist, search);
    }

    @Override
    public List<String> searchChannelsUrlByPlistAndName(String plist, String search) {
        return channelDb.searchChannelsUrlByPlistAndName(plist, search);
    }

}
