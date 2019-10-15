package com.catclaw.keyboard.keyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.catclaw.keyboard.R;

public class CertificationInputActivity extends Activity {
    EditText edInput;
    Button btnInput;
    int jumpSource;
     CertificationKeyboard safeKeyboard;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**这里要注意设置横屏时键盘不能进入全屏模式 */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.certification_input_activity);
        edInput=findViewById(R.id.ed_input);
        btnInput=findViewById(R.id.btn_input);
        edInput.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        View rootView=findViewById(R.id.main_root);
        View scrollLayout = findViewById(R.id.scroll_layout);
        LinearLayout keyboardContainer = findViewById(R.id.keyboardPlace);
        //获取跳转相关信息并弹出相应的键盘：
        Intent intent=getIntent();
        jumpSource=intent.getIntExtra("jumpSource",1);
        edInput.getFocusable();
        if (jumpSource==1){
            edInput.setHint("姓名");
            edInput.requestFocus();
        }else if (jumpSource==2){
            edInput.setHint("身份证");
            safeKeyboard = new CertificationKeyboard(getApplicationContext(), keyboardContainer,
                    R.layout.layout_keyboard_container, R.id.safeKeyboardLetter, rootView, scrollLayout);
            safeKeyboard.setmCurrentEditText(edInput);
            safeKeyboard.hideSystemKeyBoard(edInput);
            safeKeyboard.showCertificationKeyboard();
        }

        //以下为和跳转有关的设置

        btnInput.setText("确定");
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确定之后需要能够退出本activity并且将相应的信息反馈到webview中去
                returnWithData();
            }
        });


        //以下为和键盘有关的相关的流程和参数设置


    }
    /**
     * 返回并且将本界面中的信息写入到前端页面
     */
    public void returnWithData(){
        Intent backIntent=new Intent();
        String returnString=edInput.getText().toString();
        backIntent.putExtra("backInput",returnString);
        setResult(jumpSource,backIntent);
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        System.out.println("gaodisen:-----keycode"+keyCode+":keyevent:"+event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        returnWithData();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (safeKeyboard!=null){
            safeKeyboard.release();
            safeKeyboard=null;
        }
        super.onDestroy();
    }
}
