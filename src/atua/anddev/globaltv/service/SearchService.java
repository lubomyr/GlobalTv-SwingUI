package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

import java.util.ArrayList;
import java.util.List;

public interface SearchService {
    List<Channel> searchList = new ArrayList<>();

    void addToSearchList(Channel channel);

    Channel getSearchChannelById(int id);

    void clearSearchList();

    int sizeOfSearchList();
}
