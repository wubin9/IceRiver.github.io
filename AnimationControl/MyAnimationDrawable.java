package com.fortyseven.www.view;

import android.graphics.drawable.AnimationDrawable;

/**
 * Created by wubin on 17-2-19.
 */

public class MyAnimationDrawable extends AnimationDrawable {

    private int repeatCount = 0;
    private boolean isRepeat = true;
    private AnimationEndListener listener;

    public MyAnimationDrawable() {
        super();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        if(!isRepeat) {
            repeatCount --;
            if(repeatCount == 1) {
                super.unscheduleSelf(this);
                if(listener != null) listener.onEnd();
            }
        }
    }

    public void setRepeatCount(int num) {
        if(num == 0) isRepeat = true;
        else {
            isRepeat = false;
            repeatCount = num*getNumberOfFrames();
        }
    }

    public void setAnimationEndListener(AnimationEndListener ls) {
        listener = ls;
    }

    public interface AnimationEndListener {
        public void onEnd();
    }
}
