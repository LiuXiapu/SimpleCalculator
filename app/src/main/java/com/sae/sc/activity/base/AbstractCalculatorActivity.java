package com.sae.sc.activity.base;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.sae.sc.R;
import com.sae.sc.listener.AnimationFinishedListener;
import com.sae.sc.view.RevealView;


public abstract class AbstractCalculatorActivity extends AbstractNavDrawerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void animateRipple(final ViewGroup foreground,
                               @Nullable View sourceView,
                               int color, final Animator.AnimatorListener listener, final boolean out) {
        if (color == -1) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            color = typedValue.data;
        } else if (color == -2) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
        }
        //create new reveal view
        final RevealView revealView = new RevealView(this);
        revealView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        revealView.setRevealColor(color);
        //add to viewgroup
        foreground.addView(revealView);

        final Animator revealAnimator;
        final int[] clearLocation = new int[2];
        if (sourceView != null) {
            sourceView.getLocationInWindow(clearLocation);
            clearLocation[0] += sourceView.getWidth() / 2;
            clearLocation[1] += sourceView.getHeight() / 2;
        } else {
            clearLocation[0] = foreground.getWidth() / 2;
            clearLocation[1] = foreground.getHeight() / 2;
        }
        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop();

        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2);
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        if (out)
            revealAnimator = ViewAnimationUtils.createCircularReveal(
                    revealView, revealCenterX, revealCenterY, 0.0f, revealRadius);
        else
            revealAnimator = ViewAnimationUtils.createCircularReveal(
                    revealView, revealCenterX, revealCenterY, revealRadius, 0f);

        revealAnimator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        if (listener != null) revealAnimator.addListener(listener);
        revealAnimator.addListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                foreground.removeView(revealView);
            }
        });
        revealAnimator.start();
    }

}
