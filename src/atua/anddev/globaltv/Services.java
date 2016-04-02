package atua.anddev.globaltv;

import atua.anddev.globaltv.service.*;

public interface Services {
    PlaylistService playlistService = new PlaylistServiceImpl();
    ChannelService channelService = new ChannelServiceImpl();
    FavoriteService favoriteService = new FavoriteServiceImpl();
    SearchService searchService = new SearchServiceImpl();
    TranslationService tService = new TranslationServiceImpl();
    String myPath = "";
}
