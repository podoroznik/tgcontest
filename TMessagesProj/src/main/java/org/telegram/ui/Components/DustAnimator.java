package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class DustAnimator {
    private ValueAnimator valueAnimator = new ValueAnimator();
    private final ParticleSmasher particleSmasher;
    private CroppingImageView mAnimatorView;
    private Bitmap bitmap;
    private Rect rect;
    private Paint paint;
    private Particle[][] particles;
    private float endValue = 4f;
    private final long duration = 4000L;
    private final long startDelay = 150L;
    private int radius = 4;
    private int maxX = Integer.MIN_VALUE;

    public DustAnimator(ParticleSmasher view, CroppingImageView animatorView) {
        particleSmasher = view;
        init(animatorView);
    }

    private void init(CroppingImageView animatorView) {
        mAnimatorView = animatorView;
        bitmap = particleSmasher.createBitmapFromView(animatorView);
        rect = particleSmasher.getViewRect(animatorView);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void start() {
        setValueAnimator();
        calculateParticles(bitmap);
        hideView(mAnimatorView);
        valueAnimator.start();
        particleSmasher.invalidate();
    }

    private void setValueAnimator() {
        valueAnimator.setDuration(duration);
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                particleSmasher.removeAnimator(DustAnimator.this);
            }

            @Override
            public void onAnimationStart(Animator animation) {
            }
        });
    }

    private void calculateParticles(Bitmap bitmap) {
        int col = bitmap.getWidth() / (radius * 2);
        int row = bitmap.getHeight() / (radius * 2);
        particles = new Particle[row][col];
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int x = j * radius * 2 + radius;
                int y = i * radius * 2 + radius;
                int color = bitmap.getPixel(x, y);
                Point point = new Point(rect.left + x, rect.top + y);
                if (point.x > maxX) {
                    maxX = point.x;
                }
                if (point.x < minX) {
                    minX = point.x;
                }
                particles[i][j] = new Particle(point, color);
            }
        }
        this.maxX = maxX;
        valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(minX, maxX * 10);
        valueAnimator.setDuration(1750L);
        endValue = maxX;
        this.bitmap.recycle();
        this.bitmap = null;
        particleSmasher.invalidate();
    }

    private void hideView(CroppingImageView view) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(600).setFloatValues(0f, 1f);
        valueAnimator.addUpdateListener(valueAnimator1 -> view.setImageCrop((Float) valueAnimator1.getAnimatedValue()));
        valueAnimator.addListener(
                new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        view.setVisibility(View.GONE);
                    }
                });
        valueAnimator.start();
    }

    public boolean draw(Canvas canvas) {
        if (!valueAnimator.isStarted()) {
            return false;
        }
        for (Particle[] particle : particles) {
            for (Particle p : particle) {
                p.advance((Float) valueAnimator.getAnimatedValue(), endValue, maxX);
                if (p.alpha > 0) {
                    paint.setColor(p.color);
                    paint.setAlpha((int) (Color.alpha(p.color) * p.alpha));
                    if (p.baseCx <= (Float) valueAnimator.getAnimatedValue()) {
                        canvas.drawRect(p.calculatedX, p.calculatedY, p.calculatedX + p.radius, p.calculatedY + p.radius, paint);
                    }
                }
            }
        }
        particleSmasher.invalidate();
        return true;
    }
}
