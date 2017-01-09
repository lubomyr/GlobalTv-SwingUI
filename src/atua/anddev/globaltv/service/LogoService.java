package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Logo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public interface LogoService {
    List<Logo> logoList = new ArrayList<>();

    void setupLogos();

    String getLogoByName(String str);

    ImageIcon scaleImage(ImageIcon icon, int w, int h);
}
