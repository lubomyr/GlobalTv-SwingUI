package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Favorites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface FavoriteService {
    List<Favorites> favorites = new ArrayList<>();

    void addToFavoriteList(String name, String prov);

    Favorites getFavoriteById(int id);

    void clearAllFavorites();

    int sizeOfFavoriteList();

    void deleteFromFavoritesById(int id);

    List<String> getFavoriteListForSelProv();

    int indexOfFavoriteByNameAndProv(String name, String prov);

    void saveFavorites();

    void loadFavorites() throws IOException;
}
