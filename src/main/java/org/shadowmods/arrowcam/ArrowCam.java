package org.shadowmods.arrowcam;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArrowCam.MODID, version = ArrowCam.VERSION)
public class ArrowCam {
    public static final String MODID = "arrowcam";
    public static final String VERSION = "1.0";
    public static Logger logger;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger = LogManager.getLogger(MODID);
        EntityRegistry.registerModEntity(EntityCamera.class, "EntityCamera", EntityRegistry.findGlobalUniqueEntityId(), this, 64, 5, false);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
