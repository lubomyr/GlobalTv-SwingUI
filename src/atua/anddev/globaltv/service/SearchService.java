package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Search;

import java.util.ArrayList;
import java.util.List;

public interface SearchService {
    List<Search> searchList = new ArrayList<>();

    void addToSearchList(String name, String url, String prov);

    Search getSearchListById(int id);

    void clearSearchList();

    int sizeOfSearchList();
}
