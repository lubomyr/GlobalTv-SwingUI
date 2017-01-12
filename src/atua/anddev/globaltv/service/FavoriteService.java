package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FavoriteService {
    List<Channel> favorites = new ArrayList<>();

    void addToFavoriteList(Channel channel);

    Channel getFavoriteById(int id);

    void clearAllFavorites();

    int sizeOfFavoriteList();

    void deleteFromFavoritesById(int id);

    List<Channel> getFavoriteListForSelProv();

    List<Channel> getFavoriteList();

    int indexOfFavoriteByChannel(Channel channel);

    void saveFavorites();

    void loadFavorites() throws IOException;
}
