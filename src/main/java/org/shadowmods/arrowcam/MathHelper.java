package org.shadowmods.arrowcam;

public class MathHelper {
    public static float getShortAngle(float startAng, float targetAng) {
        startAng = normalize(startAng);
        targetAng = normalize(targetAng);

        float shortestAngle = targetAng - startAng;
        shortestAngle = normalize(shortestAngle);

        return shortestAngle;
    }

    public static float normalize(float ang) {
        ang = ang % 360;
        if (ang > 180) ang -= 360;
        if (ang < -180) ang += 360;
        return ang;
    }
}
