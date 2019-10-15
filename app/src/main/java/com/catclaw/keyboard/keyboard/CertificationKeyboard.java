package com.catclaw.keyboard.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.catclaw.keyboard.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;

public class CertificationKeyboard {
    private static final String TAG = "CertificationKeyboard";
    private Context mContext;
    private LinearLayout keyboardParentView;
    private View keyboardContainer;
    private CertificationKeyboardView keyboardView;
    private int keyboardLayoutResId;
    private int mKeyboardViewId;
    private EditText mCurrentEditText;
    private View.OnTouchListener onEditTouchListener;

    private View rootView;
    private View mScrollLayout;

    private ViewTreeObserver.OnGlobalFocusChangeListener onGlobalFocusChangeListener;
    private ViewTreeObserver treeObserver;
    private ViewPoint downPoint;
    private ViewPoint upPoint;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mNeedScroll;
    private Keyboard keyboard;

    CertificationKeyboard(Context context,LinearLayout keyboardParentView,int keyboardLayoutResId,int mKeyboardViewId,View rootView,
                          View mScrollLayout){
        this.mContext=context;
        this.keyboardParentView=keyboardParentView;
        this.keyboardLayoutResId=keyboardLayoutResId;
        this.mKeyboardViewId=mKeyboardViewId;
        this.rootView=rootView;
        this.mScrollLayout=mScrollLayout;
        initData();
        initKeyboard();
    }

    private void initData(){
        downPoint=new ViewPoint();
        upPoint=new ViewPoint();
        WindowManager wm= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (null!=wm){
            DisplayMetrics metrics =new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            mScreenWidth=metrics.widthPixels;
            mScreenHeight=metrics.heightPixels;
        }
    }

    private void initKeyboard(){
        System.out.println("gaodisen---->here---->initKeyboard");
        keyboardContainer = LayoutInflater.from(mContext).inflate(keyboardLayoutResId,keyboardParentView,true);
        keyboardContainer.setVisibility(View.GONE);
        keyboard=new Keyboard(mContext, R.xml.keyboard_id_card_zn);
        keyboardView=keyboardContainer.findViewById(mKeyboardViewId);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
        FrameLayout done=keyboardContainer.findViewById(R.id.keyboardDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isKeyboardShown()){
                    hideCertificationKeyborad();
                }
            }
        });

//        keyboardView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return event.getAction() == MotionEvent.ACTION_MOVE;
//            }
//        });


    }





    //隐藏系统键盘关键代码
    public void hideSystemKeyBoard(EditText edit) {
        this.mCurrentEditText = edit;
        InputMethodManager imm = (InputMethodManager) this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }

        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            edit.setInputType(0);
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(methodName, Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, Boolean.FALSE);
            } catch (NoSuchMethodException e) {
                edit.setInputType(0);
                e.printStackTrace();
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isKeyboardShown() {
        return keyboardContainer.getVisibility() == View.VISIBLE;
    }

    public void hideCertificationKeyborad(){
        if (keyboardContainer.getVisibility()!=View.GONE){
            keyboardContainer.setVisibility(View.GONE);
        }
    }

    public void showCertificationKeyboard(){
        setKeyboard(keyboard);
        keyboardContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 设置键盘的监听
     */
    private KeyboardView.OnKeyboardActionListener listener= new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mCurrentEditText.getText();
            int start = mCurrentEditText.getSelectionStart();
            int end=mCurrentEditText.getSelectionEnd();
            if (primaryCode == keyboard.KEYCODE_DELETE){
                if (start == end) { //光标开始和结束位置相同, 即没有选中内容
                    editable.delete(start - 1, start);
                } else { //光标开始和结束位置不同, 即选中EditText中的内容
                    editable.delete(start, end);
                }
            }else{
                //输入的是键盘的值
                editable.replace(start, end, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }
        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {
        }
    };
    public void setKeyboard(Keyboard keyboard){
        keyboardView.setKeyboard(keyboard);
    }

    public void setmCurrentEditText(EditText editText){
        mCurrentEditText=editText;
    }

    /**
     * 顶起EditText
     */
    private void doScrollLayout(){

    }

    /**
     * 计算是否会遮挡editText
     */
    private void editNeedScroll(EditText mEditText){

    }


    /**
     * 释放资源
     */

    public void release() {
        mContext = null;
        if (treeObserver != null && onGlobalFocusChangeListener != null && treeObserver.isAlive()) {
            treeObserver.removeOnGlobalFocusChangeListener(onGlobalFocusChangeListener);
        }
        treeObserver = null;
        onGlobalFocusChangeListener = null;
    }
}