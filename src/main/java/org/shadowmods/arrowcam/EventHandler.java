package org.shadowmods.arrowcam;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class EventHandler {

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (!isRunningClient()) return;

        ArrowCam.isActive = ArrowCam.keyStartCam.getIsKeyPressed();
        if (event.entity instanceof EntityArrow && ArrowCam.isActive) {
            EntityArrow arrow = (EntityArrow) event.entity;
            EntityPlayer arrowShooter = arrow.worldObj.getClosestPlayerToEntity(arrow, 10);
            if (arrowShooter == null) return;

            if (player.getDisplayName().equals(arrowShooter.getDisplayName())) {
                EntityCamera.getInstance().startCam(arrow, true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!isRunningClient()) return;
        if (event.source.getDamageType().equals("arrow") && ArrowCam.isActive) {
            EntityCamera.getInstance().startCam(event.entity, false, ArrowCam.entityCamMaxLife);
        }
    }

    /**
     * Check that the world is loaded and that this is not a server/integrated server.
     *
     * @return The check's result.
     */
    private boolean isRunningClient() {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        return (side.isClient()) && (player != null);
    }
}