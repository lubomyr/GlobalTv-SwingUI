package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Playlist;

import java.util.ArrayList;
import java.util.List;

public interface PlaylistService {
    List<Playlist> offeredPlaylist = new ArrayList<Playlist>();
    List<String> activePlaylistName = new ArrayList<String>();

    void addToActivePlaylist(String name, String url, int type, String md5, String update);

    void addToOfferedPlaylist(String name, String url, int type);

    void setActivePlaylistById(int id, String name, String url, int type);

    Playlist getActivePlaylistById(int id);

    Playlist getOfferedPlaylistById(int id);

    void clearActivePlaylist();

    void clearOfferedPlaylist();

    int sizeOfActivePlaylist();

    int sizeOfOfferedPlaylist();

    List<Playlist> getAllActivePlaylist();

    List<Playlist> getAllOfferedPlaylist();

    List<String> getAllNamesOfActivePlaylist();

    List<String> getAllNamesOfOfferedPlaylist();

    int indexNameForActivePlaylist(String name);

    int indexNameForOfferedPlaylist(String name);

    void deleteActivePlaylistById(int id);

    void addNewActivePlaylist(Playlist plst);

    void setMd5(int id, String md5);

    void setUpdateDate(int id, Long update);

    void setupProvider();

    void fillNamesOfPlaylist();

    List<Playlist> getSortedByDatePlaylists();

    void readPlaylist(int num);

    void addAllOfferedPlaylist();
}
