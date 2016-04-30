package atua.anddev.globaltv.service;

import java.util.List;

public interface SearchService {
    List<String> searchChannelsByName(String search);

    List<String> searchChannelsProvByName(String search);

    List<String> searchChannelsUrlByName(String search);

    List<String> searchChannelsByPlistAndName(String plist, String search);

    List<String> searchChannelsUrlByPlistAndName(String plist, String search);
}
