package org.shadowmods.arrowcam;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;

public class EntityCamera extends EntityLiving {
    private static EntityCamera instance;
    private Entity target;
    private boolean enabled, invert;
    private int maxLife, despawnDelay;

    private boolean hideGUI;
    private float fovSetting;
    private int thirdPersonView;

    private static final int positionSmoother = 5;
    private static final int rotationSmoother = 5;

    private EntityCamera() {
        super(null);

        setSize(0.0F, 0.0F);
        yOffset = 0.0F;
    }

    public static EntityCamera getInstance() {
        if (instance == null) {
            instance = new EntityCamera();
        }
        return instance;
    }

    public void startCam(Entity target) {
        startCam(target, false);
    }

    public void startCam(Entity target, boolean invert) {
        startCam(target, invert, 120);
    }

    public void startCam(Entity target, boolean invert, int maxLife) {
        startCam(target, invert, maxLife, 20);
    }

    public void startCam(Entity target, boolean invert, int maxLife, int despawnDelay) {
        Minecraft mc = Minecraft.getMinecraft();
        if (enabled) stopCam();
        enabled = true;

        hideGUI = mc.gameSettings.hideGUI;
        fovSetting = mc.gameSettings.fovSetting;
        thirdPersonView = mc.gameSettings.thirdPersonView;

        this.target = target;
        this.invert = invert;
        this.maxLife = maxLife;
        this.despawnDelay = despawnDelay;
        this.isDead = false;
        worldObj = target.worldObj;
        worldObj.spawnEntityInWorld(this);

        setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        setRotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);

        doCameraMove();

        mc.gameSettings.hideGUI = true;
        mc.gameSettings.thirdPersonView = 1;
        mc.renderViewEntity = this;
    }

    public void stopCam() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!enabled) return;
        enabled = false;

        mc.gameSettings.hideGUI = hideGUI;
        mc.gameSettings.fovSetting = fovSetting;
        mc.gameSettings.thirdPersonView = thirdPersonView;
        mc.renderViewEntity = mc.thePlayer;
        worldObj.removeEntity(this);
    }

    private void doCameraMove() {
        double x = posX + (target.posX - posX) / positionSmoother;
        double y = posY + (target.posY - posY) / positionSmoother;
        double z = posZ + (target.posZ - posZ) / positionSmoother;

        float yaw = MathHelper.getShortAngle(rotationYaw, target.rotationYaw);
        float pitch = MathHelper.getShortAngle(rotationPitch, target.rotationPitch);
        if (invert) {
            yaw = -(rotationYaw + yaw);
            pitch = -(rotationPitch + pitch);
        } else {
            // TODO: Figure out why smoothing breaks if inverted.
            yaw /= rotationSmoother;
            pitch /= rotationSmoother;
            yaw += rotationYaw;
            pitch += rotationPitch;
        }
        yaw = MathHelper.normalize(yaw);
        pitch = MathHelper.normalize(pitch);

        setPosition(x, y, z);
        setRotation(yaw, pitch);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (!enabled) return;

        if (!Minecraft.getMinecraft().thePlayer.isSneaking()) {
            stopCam();
            return;
        } else if (maxLife < 0 || despawnDelay < 0) {
            stopCam();
            return;
        } else if (target.isDead) {
            --despawnDelay;
            return;
        } else {
            --maxLife;
        }

        doCameraMove();

        motionX = motionY = motionZ = 0;
    }

    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    /* ===== Don't save the entity ===== */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
    }
}