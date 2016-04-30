package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Services;
import atua.anddev.globaltv.entity.Favorites;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService, Services {

    @Override
    public List<String> getAllFavoritesProv() {
        return favoriteDb.getAllFavoritesProv();
    }

    @Override
    public List<String> getAllFavoritesName() {
        return favoriteDb.getAllFavoritesName();
    }

    @Override
    public List<String> getFavoritesNameByPlist(String plist) {
        return favoriteDb.getFavoritesNameByPlist(plist);
    }

    @Override
    public List<Favorites> getAllFavorites() {
        return favoriteDb.getAllFavorites();
    }

    @Override
    public List<Favorites> getFavoritesByPlist(String plist) {
        return favoriteDb.getFavoritesByPlist(plist);
    }

    @Override
    public int indexOfFavoriteByNameAndProv(String name, String prov) {
        int result = -1;
        for (int i = 0; i < sizeOfFavoriteList(); i++) {
            if (name.equals(getFavoriteById(i).getName()) && prov.equals(getFavoriteById(i).getProv())) {
                result = i;
            }
        }
        return result;
    }

    @Override
    public boolean containsNameForFavorite(String name) {
        return favoriteListName.contains(name);
    }

    @Override
    public void deleteFromFavoritesById(int id) {
        List<Integer> idList = favoriteDb.getAllFavoritesID();
        int iddb = idList.get(id);
        favoriteListName.remove(id);
        favoriteProvList.remove(id);
        favoriteDb.deleteFromFavoritesById(iddb);
    }

    @Override
    public void addToFavoriteList(String name, String prov) {
        favoriteListName.add(name);
        favoriteProvList.add(prov);
        favoriteDb.insertIntoFavorites(name, prov);
    }

    @Override
    public Favorites getFavoriteById(int id) {
        List<Integer> idList = favoriteDb.getAllFavoritesID();
        int iddb = idList.get(id);
        return favoriteDb.getFavoriteById(iddb);
    }

    @Override
    public void clearAllFavorites() {
        favoriteListName.clear();
        favoriteProvList.clear();
        favoriteDb.deleteAllFavorites();
    }

    @Override
    public int sizeOfFavoriteList() {
        return favoriteDb.numberOfRows();
    }

    @Override
    public int indexNameForFavorite(String name) {
        return favoriteListName.indexOf(name);
    }
}
