package com.nidaappdev.congratulator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;

import java.io.IOException;

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
     * and out
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
     * Prevents clicking and disturbing the
     * animations while they're running.
     */
    private boolean isLayoutInProgress;

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
     * Sound player
     */
    private MediaPlayer mediaPlayer;

    /**
     * Sound resource
     */
    private Uri soundUri;

    /**
     * Determines whether sound is
     * played or not
     */
    private boolean soundEnabled;

    /**
     * Title TextView
     */
    private AutoResizeTextView titleTV;

    /**
     * Content TextView
     */
    private TextView contentTV;

    private TextView dismissTV;

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
        soundUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.sound_effect);
        isLayoutCompleted = false;
        isReady = false;
        soundEnabled = true;

        /**
         * initialize objects
         */
        handler = new Handler();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getContext(), soundUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        titleTV = (AutoResizeTextView) LayoutInflater.from(getContext()).inflate(R.layout.title_tv, null);
        titleTV.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        contentTV = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.content_tv, null);
        contentTV.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        dismissTV = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.dismiss_blinking_tv, null);
        dismissTV.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

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
        v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLayoutInProgress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;
                case MotionEvent.ACTION_UP:
                    dismiss();
                    return true;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Shows congratulation view with fade in
     * animation
     *
     * @param activity
     */
    private void show(Activity activity) {
        isLayoutInProgress = true;
        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        setReady(true);

        handler.postDelayed(() -> AnimationFactory.animateFadeIn(
                        CongratulationView.this,
                        fadeAnimationDuration,
                        () -> {
                            setVisibility(VISIBLE);
                            isLayoutInProgress = false;
                        }),
                delayMillis
        );
    }

    /**
     * Dismiss Congratulation View
     */
    public void dismiss() {
        isLayoutInProgress = true;
        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, () -> {
            setVisibility(GONE);
            removeMaterialView();
            isLayoutInProgress = false;
        });
    }

    private void removeMaterialView() {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    private void setTextViews() {
        if (titleTV.getParent() != null)
            ((ViewGroup) titleTV.getParent()).removeView(titleTV);
        if (contentTV.getParent() != null)
            ((ViewGroup) contentTV.getParent()).removeView(contentTV);
        if (dismissTV.getParent() != null) {
            ((ViewGroup) dismissTV.getParent()).removeView(dismissTV);
        }

        float dp = getResources().getDisplayMetrics().scaledDensity;

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins((int) (5 * dp), (int) (100 * dp), (int) (5 * dp), (int) (10 * dp));
        titleTV.setLayoutParams(titleParams);
        titleTV.postInvalidate();
        addView(titleTV);
        titleTV.setText(title);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.addRule(BELOW, R.id.title);
        contentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentParams.addRule(RelativeLayout.ABOVE, R.id.dismiss);
        contentParams.setMargins((int) (5 * dp), (int) (20 * dp), (int) (5 * dp), (int) (5 * dp));
        contentTV.setLayoutParams(contentParams);
        contentTV.postInvalidate();
        addView(contentTV);
        contentTV.setText(content);

        RelativeLayout.LayoutParams dismissParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dismissParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        dismissParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        dismissParams.setMargins((int) (5 * dp), (int) (0 * dp), (int) (5 * dp), (int) (50 * dp));
        dismissTV.setLayoutParams(dismissParams);
        dismissTV.postInvalidate();
        addView(dismissTV);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        dismissTV.startAnimation(anim);
    }

    private void startConfetti() {
        CommonConfetti.explosion(this, 0, 0, confettiColors).infinite()
                .setBound(new Rect(0, 0, width, height));

        CommonConfetti.explosion(this, width, 0, confettiColors).infinite()
                .setBound(new Rect(0, 0, width, height));

        CommonConfetti.explosion(this, 0, height / 2, confettiColors).infinite()
                .setBound(new Rect(0, 0, width, height));

        CommonConfetti.explosion(this, width, height / 2, confettiColors).infinite()
                .setBound(new Rect(0, 0, width, height));

        CommonConfetti.explosion(this, 0, height, confettiColors).infinite()
                .setBound(new Rect(0, 0, width, height));

        CommonConfetti.explosion(this, width, height, confettiColors).infinite()
                .setBound(new Rect(0, 0, width, height));
    }

    private void playSound() {
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    private void setLayout() {
        handler.post(() -> {
            isLayoutCompleted = true;
            setTextViews();
            startConfetti();
            if (soundEnabled)
                playSound();
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

    private void setSoundUri(Uri soundSrc) {
        this.soundUri = soundSrc;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void enableSound(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
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

        public Builder setSoundUri(Uri soundRes) {
            congratulationView.setSoundUri(soundRes);
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

        public Builder enableSound(boolean enable) {
            congratulationView.enableSound(enable);
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
