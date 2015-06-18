package com.google.samples.apps.topeka.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.google.samples.apps.topeka.R;
import com.google.samples.apps.topeka.widget.outlineprovider.AvatarOutlineProvider;

/**
 * A simple view that wraps an avatar.
 */
public class AvatarView extends ImageView implements Checkable {

    private boolean mChecked;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initOutlineProvider();
    }

    private void initOutlineProvider() {
        setOutlineProvider(new AvatarOutlineProvider());
        setClipToOutline(true);
    }

    @Override
    public void setChecked(boolean b) {
        mChecked = b;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            Drawable border = getResources().getDrawable(R.drawable.selector_avatar, null);
            border.setBounds(0, 0, getWidth(), getHeight());
            border.draw(canvas);
        }
    }
}
