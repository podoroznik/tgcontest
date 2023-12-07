package org.telegram.ui.Components;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.util.ArrayList;
import java.util.List;

public class ParticleSmasher extends View {
    private final List<DustAnimator> dustAnimators;
    private Canvas canvas;

    public ParticleSmasher(Context mActivity) {
        super(mActivity);
        dustAnimators = new ArrayList<>();
        addView2Window(mActivity);
        init();
    }

    private void addView2Window(Context activity) {
        ViewGroup rootView = ((Activity) activity).findViewById(Window.ID_ANDROID_CONTENT);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        rootView.addView(this, layoutParams);
    }

    private void init() {
        canvas = new Canvas();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DustAnimator animator : dustAnimators) {
            animator.draw(canvas);
        }
    }

    public DustAnimator with(CroppingImageView view) {
        DustAnimator animator = new DustAnimator(this, view);
        dustAnimators.add(animator);
        return animator;
    }

    public Rect getViewRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int[] location = new int[2];
        getLocationOnScreen(location);
        rect.offset(-location[0], -location[1]);
        return rect;
    }

    public Bitmap createBitmapFromView(View view) {
        view.clearFocus();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = this.canvas;
        canvas.setBitmap(bitmap);
        view.draw(canvas);
        canvas.setBitmap(null);

        return bitmap;
    }

    public void removeAnimator(DustAnimator animator) {
        dustAnimators.remove(animator);
    }
}