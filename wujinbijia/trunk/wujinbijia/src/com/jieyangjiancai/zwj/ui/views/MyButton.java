
package com.jieyangjiancai.zwj.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 按钮的点击效果
 * 
 * @author ldr
 */
public class MyButton extends Button {
    private boolean enabled = true;

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enabled) {
            try {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setAlpha(0.5f);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    setAlpha(1f);
                }
            } catch (Exception e) {

            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.enabled = enabled;
        try {
            if(!enabled)
                setAlpha(0.5f);
            else
                setAlpha(1f);
        } catch (Exception e) {
        }
        
    }
}
