package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CroppingImageView extends ImageView {
    private Rect mClipRect;

    public CroppingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initClip();
    }

    public CroppingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClip();
    }

    public CroppingImageView(Context context) {
        super(context);
        initClip();
    }

    private void initClip() {
        post(() -> setImageCrop(0f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (clip()) {
            canvas.clipRect(mClipRect);
        }
        super.onDraw(canvas);
    }

    private boolean clip() {
        if (mClipRect == null) {
            mClipRect = new Rect(0, 0, getWidth(), getHeight());
        }
        return !mClipRect.isEmpty() && !clipEqualsBounds();
    }

    private boolean clipEqualsBounds() {
        int width = getWidth();
        int height = getHeight();
        return mClipRect.width() == width && mClipRect.height() == height;
    }

    public void setImageCrop(float value) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        mClipRect = new Rect((int) (value * width), 0, width, height);
        invalidate();
    }
}
