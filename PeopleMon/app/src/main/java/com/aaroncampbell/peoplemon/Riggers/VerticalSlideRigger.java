package com.aaroncampbell.peoplemon.Riggers;

import android.app.Application;

import com.aaroncampbell.peoplemon.R;
import com.davidstemmer.screenplay.stage.rigger.AnimResources;
import com.davidstemmer.screenplay.stage.rigger.TweenRigger;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class VerticalSlideRigger extends TweenRigger {
    private static final AnimResources params = new AnimResources();

    static {
        params.forwardIn = R.anim.slide_in_up;
        params.backIn = R.anim.slide_in_down;
        params.backOut = R.anim.slide_out_down;
        params.forwardOut = R.anim.slide_out_up;
    }

    public VerticalSlideRigger(Application context) {
        super(context, params);
    }
}
