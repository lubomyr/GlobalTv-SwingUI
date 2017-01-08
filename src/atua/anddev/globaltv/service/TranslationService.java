package atua.anddev.globaltv.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TranslationService {
    Map<String, String> localMap = new HashMap<>();
    List<String> origNames = new ArrayList<>();
    List<String> tranNames = new ArrayList<>();

    void getTranslationData(String path);

    String getString(String input);

    String translateCategory(String input);
}
