package com.nidaappdev.congratulator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CongratulationView extends RelativeLayout {

    /**
     * Mask color
     */
    private int maskColor;

    /**
     * View shows in these millis delay
     */
    private long delayMillis;

    /**
     * Duration of view's fading in
     * and ou
     */
    private long fadeAnimationDuration;

    /**
     * We don't draw CongratulationView
     * until isReady field set to true
     */
    private boolean isReady;

    /**
     * Handler will be used to
     * delay CongratulationView
     */
    private Handler handler;

    /**
     * All views will be drawn to
     * this bitmap and canvas then
     * bitmap will be drawn to canvas
     */
    private Bitmap bitmap;
    private Canvas canvas;

    /**
     * Layout width/height
     */
    private int width;
    private int height;

    /**
     * When layout completed, we set this true
     * Otherwise onGlobalLayoutListener stuck on loop.
     */
    private boolean isLayoutCompleted;

    /**
     * Congratulation title
     */
    private String title;

    /**
     * Congratulation content
     */
    private String content;

    /**
     * Confetti colors
     */
    private int[] confettiColors;

    /**
     * Title TextView
     */
    private AutoResizeTextView titleTV;

    /**
     * Content TextView
     */
    private TextView contentTV;

    public CongratulationView(Context context) {
        super(context);
        init();
    }

    public CongratulationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CongratulationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CongratulationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);

        /**
         * Set default values
         */
        maskColor = Constants.DEFAULT_MASK_COLOR;
        delayMillis = Constants.DEFAULT_DELAY_MILLIS;
        fadeAnimationDuration = Constants.DEFAULT_FADE_DURATION;
        confettiColors = Constants.DEFAULT_CONFETTI_COLORS;
        title = Constants.DEFAULT_TITLE;
        content = Constants.DEFAULT_CONTENT;
        isReady = false;

        /**
         * initialize objects
         */
        handler = new Handler();

        titleTV = (AutoResizeTextView) LayoutInflater.from(getContext()).inflate(R.layout.title_tv, null);
        titleTV.setText(title);
        titleTV.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        contentTV = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.content_tv, null);
        contentTV.setText(content);
        contentTV.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isLayoutCompleted) {
                    setLayout();
                    removeOnGlobalLayoutListener(CongratulationView.this, this);
                }
            }
        });
    }

    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady) return;

        if (bitmap == null || canvas == null) {
            if (bitmap != null) bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(bitmap);
        }

        /**
         * Draw mask
         */
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.canvas.drawColor(maskColor);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * Shows congratulation view with fade in
     * animation
     *
     * @param activity
     */
    private void show(Activity activity) {

        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        setReady(true);

        handler.postDelayed(() -> AnimationFactory.animateFadeIn(
                        CongratulationView.this,
                        fadeAnimationDuration,
                        () -> setVisibility(VISIBLE)),
                delayMillis
        );
    }

    /**
     * Dismiss Congratulation View
     */
    public void dismiss() {

        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, () -> {
            setVisibility(GONE);
            removeMaterialView();
        });
    }

    private void removeMaterialView() {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    private void setLayout() {
        handler.post(() -> {
            isLayoutCompleted = true;
            if (titleTV.getParent() != null)
                ((ViewGroup) titleTV.getParent()).removeView(titleTV);
            if (contentTV.getParent() != null)
                ((ViewGroup) contentTV.getParent()).removeView(contentTV);

            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            titleParams.addRule(ALIGN_PARENT_TOP, TRUE);
            titleParams.setMargins(5, 20, 5, 10);
            titleTV.setLayoutParams(titleParams);
            titleTV.postInvalidate();
            addView(titleTV);

            RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            titleParams.addRule(BELOW, R.id.title);
            titleParams.setMargins(5, 20, 5, 5);
            contentTV.setLayoutParams(titleParams);
            contentTV.postInvalidate();
            addView(contentTV);
        });
    }

    private void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    private void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    private void setFadeAnimationDuration(long fadeAnimationDuration) {
        this.fadeAnimationDuration = fadeAnimationDuration;
    }

    private void setConfettiColors(int[] confettiColors) {
        this.confettiColors = confettiColors;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    /**
     * Builder Class
     */
    public static class Builder {

        private CongratulationView congratulationView;

        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
            congratulationView = new CongratulationView(activity);
        }

        public Builder setMaskColor(int maskColor) {
            congratulationView.setMaskColor(maskColor);
            return this;
        }

        public Builder setDelayMillis(long delayMillis) {
            congratulationView.setDelayMillis(delayMillis);
            return this;
        }

        public Builder setFadeAnimationDuration(long fadeAnimationDuration) {
            congratulationView.setFadeAnimationDuration(fadeAnimationDuration);
            return this;
        }

        public Builder setConfettiColors(int[] confettiColors) {
            congratulationView.setConfettiColors(confettiColors);
            return this;
        }

        public Builder setTitle(String title) {
            congratulationView.setTitle(title);
            return this;
        }

        public Builder setContent(String content) {
            congratulationView.setContent(content);
            return this;
        }

        public CongratulationView build() {
            return congratulationView;
        }

        public CongratulationView show() {
            build().show(activity);
            return congratulationView;
        }
    }

}
