package cocoelf.codeelfandroid.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.LineBackgroundSpan;

/**
 * Created by shea on 2018/3/6.
 */

public class PaddingBackgroundColorSpan implements LineBackgroundSpan {
    private int mBackgroundColor;
    private int mPadding;
    private Rect mBgRect;

    public PaddingBackgroundColorSpan(int backgroundColor, int padding) {
        super();
        mBackgroundColor = backgroundColor;
        mPadding = padding;
        // Precreate rect for performance
        mBgRect = new Rect();
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        final int textWidth = Math.round(p.measureText(text, start, end));
        final int paintColor = p.getColor();
        // Draw the background
        mBgRect.set(left + mPadding,
                top - (lnum == 0 ? mPadding / 2 : - (mPadding / 2)),
                left + textWidth + mPadding,
                bottom + mPadding);
        p.setColor(mBackgroundColor);
        c.drawRect(mBgRect, p);
        p.setColor(paintColor);
    }
}
