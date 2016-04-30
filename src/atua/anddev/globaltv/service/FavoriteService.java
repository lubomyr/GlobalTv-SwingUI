package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Favorites;

import java.util.ArrayList;
import java.util.List;

public interface FavoriteService {
    ArrayList<String> favoriteListName = new ArrayList<String>();
    ArrayList<String> favoriteProvList = new ArrayList<String>();

    List<Favorites> getAllFavorites();

    List<Favorites> getFavoritesByPlist(String plist);

    List<String> getAllFavoritesName();

    List<String> getAllFavoritesProv();

    List<String> getFavoritesNameByPlist(String plist);

    void addToFavoriteList(String name, String prov);

    Favorites getFavoriteById(int id);

    void clearAllFavorites();

    int sizeOfFavoriteList();

    int indexNameForFavorite(String name);

    boolean containsNameForFavorite(String name);

    void deleteFromFavoritesById(int id);

    int indexOfFavoriteByNameAndProv(String name, String prov);
}
