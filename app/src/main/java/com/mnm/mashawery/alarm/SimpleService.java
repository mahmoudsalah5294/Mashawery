package com.mnm.mashawery.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mnm.mashawery.R;
import com.siddharthks.bubbles.FloatingBubbleConfig;
import com.siddharthks.bubbles.FloatingBubbleService;

import java.util.ArrayList;
import java.util.List;

public class SimpleService extends FloatingBubbleService {
    List<String> notes2=new ArrayList<>();

    @Override
    protected FloatingBubbleConfig getConfig() {
        notes2.add("book");
        notes2.add("shanta");
        notes2.add("ayhaga");
        Context context = getApplicationContext();
     //   Intent intent =
        return new FloatingBubbleConfig.Builder()
                .bubbleIcon(ContextCompat.getDrawable(context, R.drawable.m))
                .removeBubbleIcon(ContextCompat.getDrawable(context, com.siddharthks.bubbles.R.drawable.close_default_icon))
                .bubbleIconDp(70)
                .expandableView(getInflater().inflate(R.layout.sample_view, null))
                .removeBubbleIconDp(54)
                .paddingDp(4)
                .borderRadiusDp(0)
                .physicsEnabled(true)
                .expandableColor(Color.WHITE)
                .triangleColor(0xFF215A64)

                .gravity(Gravity.AXIS_PULL_AFTER)
                .build()
                ;
    }

    @Override
    protected boolean onGetIntent(@NonNull Intent intent) {
        return super.onGetIntent(intent);
    }
}
