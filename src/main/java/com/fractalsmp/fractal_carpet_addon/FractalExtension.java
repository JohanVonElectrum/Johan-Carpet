package com.fractalsmp.fractal_carpet_addon;

import carpet.CarpetExtension;
import carpet.CarpetServer;

public class FractalExtension implements CarpetExtension {
    public static void noop() {}

    static {
        FractalExtension extension = new FractalExtension();
        CarpetServer.manageExtension(extension);
        extension.onGameStarted();
    }
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(FractalSimpleSettings.class);
    }
}
