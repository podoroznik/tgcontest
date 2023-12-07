package org.telegram.ui.Components;

import android.graphics.Point;
import java.util.concurrent.ThreadLocalRandom;

public class Particle {
    Point point;
    int color;
    float radius;
    float baseCx;
    private final float baseCy;
    float alpha;
    float calculatedX;
    float calculatedY;
    private final float xOffset;
    private final float yOffset;

    public Particle(Point point, int color) {
        this.point = point;
        this.color = color;
        this.radius = 5f;
        this.baseCx = (float) point.x;
        this.baseCy = (float) point.y;
        this.alpha = 1f;
        this.calculatedX = baseCx;
        this.calculatedY = baseCy;
        this.xOffset = ThreadLocalRandom.current().nextFloat() * 101 - 50;
        ;
        this.yOffset = ThreadLocalRandom.current().nextFloat() * (50 - (-400)) + (-400);
        ;
    }

    public void advance(float animateValue, float endValue, int maxX) {
        float progress = baseCx / maxX;
        if (progress >= 0.80f) {
            progress = 0.75f;
        }
        alpha = 1 - ((animateValue / endValue) * (1 - (progress)));
        calculatedX = baseCx + (xOffset * (1 - alpha));
        calculatedY = (baseCy + (yOffset * (1 - alpha) / 2) * (1 - alpha) / 2);
    }
}