package com.rockchips.android.leanbacklauncher.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rockchips.android.leanbacklauncher.R;
import com.rockchips.android.leanbacklauncher.animation.MassFadeAnimator.Direction;
import com.rockchips.android.leanbacklauncher.notifications.HomeScreenView;
import com.rockchips.android.leanbacklauncher.notifications.NotificationCardView;

public final class NotificationLaunchAnimator extends ForwardingAnimatorSet {
    public NotificationLaunchAnimator(ViewGroup root, NotificationCardView cause, Rect epicenter, ImageView circleLayerView, int color, View[] headers, HomeScreenView homeScreenView) {
        Resources res = root.getResources();
        int fadeDuration = res.getInteger(R.integer.app_launch_animation_header_fade_out_duration);
        int fadeDelay = res.getInteger(R.integer.app_launch_animation_header_fade_out_delay);
        Animator anim = new CircleTakeoverAnimator(cause, circleLayerView, color);
        anim.setDuration((long) res.getInteger(R.integer.app_launch_animation_explode_duration));
        Builder builder = ((AnimatorSet) this.mDelegate).play(anim);
        builder.with(new MassFadeAnimator.Builder(root).setDirection(Direction.FADE_OUT).setTarget(NotificationCardView.class).setDuration((long) res.getInteger(R.integer.app_launch_animation_rec_fade_duration)).build());
        builder.with(new MassSlideAnimator.Builder(root).setEpicenter(epicenter).setExclude((View) cause).setExclude(NotificationCardView.class).setFade(false).build());
        for (View fadeAnimator : headers) {
            anim = new FadeAnimator(fadeAnimator, FadeAnimator.Direction.FADE_OUT);
            anim.setDuration((long) fadeDuration);
            anim.setStartDelay((long) fadeDelay);
            builder.with(anim);
        }
    }
}
