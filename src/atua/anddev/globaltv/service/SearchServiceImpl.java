package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Search;

public class SearchServiceImpl implements SearchService {

    @Override
    public void addToSearchList(String name, String url, String prov) {
        searchList.add(new Search(name, url, prov));
    }

    @Override
    public Search getSearchListById(int id) {
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
