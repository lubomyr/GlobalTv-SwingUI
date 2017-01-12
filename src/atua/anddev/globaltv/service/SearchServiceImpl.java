package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

public class SearchServiceImpl implements SearchService {

    @Override
    public void addToSearchList(Channel channel) {
        searchList.add(channel);
    }

    @Override
    public Channel getSearchChannelById(int id) {
        return searchList.get(id);
    }

    @Override
    public void clearSearchList() {
        searchList.clear();
    }

    @Override
    public int sizeOfSearchList() {
        return searchList.size();
    }

}
