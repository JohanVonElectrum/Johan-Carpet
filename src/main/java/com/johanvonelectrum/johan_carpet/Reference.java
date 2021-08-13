package com.johanvonelectrum.johan_carpet;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.Optional;

public class Reference {

    private static final ModMetadata METADATA;
    public static final String MOD_NAME;// = "Johan Carpet Addon";
    public static final String MOD_ID = "johan-carpet";
    public static final Version MOD_VERSION;

    static {
        if ((METADATA = init()) != null) {
            MOD_NAME = METADATA.getName();
            MOD_VERSION = METADATA.getVersion();
        } else {
            MOD_NAME = "Johan Carpet Addon";
            MOD_VERSION = null;
        }
    }

    private static ModMetadata init() {
        Optional<ModContainer> a = FabricLoader.getInstance().getModContainer(MOD_ID);
        if (a.isPresent())
            return a.get().getMetadata();

        JohanExtension.LOGGER.fatal("Can't get ModContainer from FabricLoader.");
        return null;
    }

}
