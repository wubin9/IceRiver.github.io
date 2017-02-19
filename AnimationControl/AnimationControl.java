package com.fortyseven.www.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fortyseven.www.testapp.R;

/**
 * Created by wubin on 17-2-19.
 */

public class AnimationControl extends ImageView {

    private Bitmap resourceBip; //播放的图片资源
    private int duration; //时间间隔
    private boolean positive; //正向还是反向播放
    private boolean rowUnit; //一行还是全部播放
    private int playRow; //当前播放的动画所在的行数
    private int repeatCount; //重复播放的次数；0、循环
    private int playAfter; //播放之后的动作
    private MyAnimationDrawable animationDrawable; //播放的资源集合

    public AnimationControl(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.animationView);
        int resId = array.getResourceId(R.styleable.animationView_img, 0);
        resourceBip = BitmapFactory.decodeResource(context.getResources(), resId);
        duration = array.getInt(R.styleable.animationView_duration, 500);
        positive = array.getBoolean(R.styleable.animationView_positive, true);
        rowUnit = array.getBoolean(R.styleable.animationView_row_unit, true);
        playRow = array.getInt(R.styleable.animationView_play_row, 1);
        repeatCount = array.getInt(R.styleable.animationView_repeat_count, 0);
        playAfter = array.getInt(R.styleable.animationView_play_after, 0);
        int column = array.getInt(R.styleable.animationView_column_number, 1);
        int row = array.getInt(R.styleable.animationView_row_number, 1);

        int playedRow;
        if (rowUnit) {
            playedRow = playRow;
        } else {
            playedRow = 0;
        }

        array.recycle();

        animationDrawable = new MyAnimationDrawable();
        initAnimation(column, row, playedRow);
    }

    public void initAnimation(int column, int row, int playedRow) {
        int w = resourceBip.getWidth()/column;
        int h = resourceBip.getHeight()/row;

        int iBeginRow = 0;
        int iEndRow = row;

        if (playedRow > 0) {
            iBeginRow = playedRow - 1;
            iEndRow = playedRow;
        }

        if(positive) {
            for (int i = iBeginRow; i < iEndRow; i++) {
                int y = h*i;
                for (int j = 0; j < column; j++) {
                    int x = w*j;
                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createBitmap(resourceBip, x, y, w, h));
                    animationDrawable.addFrame(drawable, duration);
                }
            }
        } else {
            for (int i = iEndRow-1; i >= iBeginRow; i--) {
                int y = h*i;
                for (int j = column-1; j >= 0; j--) {
                    int x = w*j;
                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createBitmap(resourceBip, x, y, w, h));
                    animationDrawable.addFrame(drawable, duration);
                }
            }
        }

        setImageDrawable(animationDrawable);
        animationDrawable.setOneShot(false);
        animationDrawable.setRepeatCount(repeatCount);

        animationDrawable.setAnimationEndListener(new MyAnimationDrawable.AnimationEndListener() {

            @Override
            public void onEnd() {
                switch (playAfter) {
                    case 0:
                        break;
                    case 1:
                        setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        animationDrawable.start();
    }

    public Drawable getCurrentDrawable() {
        return animationDrawable.getCurrent();
    }

    public Drawable getIndexDrawable(int index) {
        return animationDrawable.getFrame(index);
    }
}
