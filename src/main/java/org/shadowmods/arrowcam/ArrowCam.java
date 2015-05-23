package org.shadowmods.arrowcam;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = ArrowCam.MODID, version = ArrowCam.VERSION)
public class ArrowCam {
    public static final String MODID = "arrowcam";
    public static final String VERSION = "1.1";
    public static Logger logger;
    public static int camMaxLife, entityCamMaxLife;
    public static boolean loadChunks, animReturn, isActive;
    public static KeyBinding keyStartCam;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        camMaxLife = config.get(Configuration.CATEGORY_GENERAL, "camLength", 120, "How long before the camera returns to the player automatically.").getInt(120);
        entityCamMaxLife = config.get(Configuration.CATEGORY_GENERAL, "entityCamLength", 40, "How long to follow an entity on hit.").getInt(40);
        loadChunks = config.get(Configuration.CATEGORY_GENERAL, "loadChunks", true, "Should chunks be loaded to keep the cam from despawning. (Requires the mod on the server)").getBoolean(true);
        animReturn = config.get(Configuration.CATEGORY_GENERAL, "animReturn", true, "Should the camera animate back to the player.").getBoolean(true);
        int startCamKey = config.get("keys", "keyStartCam", Keyboard.KEY_C).getInt(Keyboard.KEY_C);
        keyStartCam = new KeyBinding("key.arrow_cam", startCamKey, "key.categories.misc");
        config.save();

        ClientRegistry.registerKeyBinding(keyStartCam);

        EntityRegistry.registerModEntity(EntityCamera.class, "EntityCamera", EntityRegistry.findGlobalUniqueEntityId(), this, 64, 5, false);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
