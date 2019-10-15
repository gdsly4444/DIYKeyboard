package com.catclaw.keyboard.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.KeyboardShortcutGroup;

import com.catclaw.keyboard.R;

import java.util.List;

public class CertificationKeyboardView extends KeyboardView {
    private Context context;


    public CertificationKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public CertificationKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public CertificationKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try{
            List<Keyboard.Key> keys=getKeyboard().getKeys();
            for (Keyboard.Key key:keys){
                if (key.codes[0]==-5){
                    Drawable backgroundDrawable=(Drawable)context.getResources().getDrawable(R.drawable.keyboard_change);
                    backgroundDrawable.setBounds(key.x,key.y,key.x+key.width,key.y+key.height);
                    backgroundDrawable.draw(canvas);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
