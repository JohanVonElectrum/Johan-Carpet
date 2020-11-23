package net.alephsmp.aleph_carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;

public class AlephExtension implements CarpetExtension {
    public static void noop() {}

    static {
        AlephExtension extension = new AlephExtension();
        CarpetServer.manageExtension(extension);
        extension.onGameStarted();
    }
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(AlephSimpleSettings.class);
    }
}
