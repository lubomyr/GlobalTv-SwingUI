package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

import java.util.List;

public class SearchServiceImpl implements SearchService {

    @Override
    public List<Channel> getSearchList() {
        return searchList;
    }

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
