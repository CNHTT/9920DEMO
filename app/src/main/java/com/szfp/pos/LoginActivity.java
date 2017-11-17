package com.szfp.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szfp.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.text_dialog_input_tips)
    TextView textDialogInputTips;
    @BindView(R.id.edit_dialog_input)
    EditText editDialogInput;
    @BindView(R.id.linearLayout1)
    RelativeLayout linearLayout1;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_enter)
    Button btEnter;


    private String pinStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_cancel, R.id.bt_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                editDialogInput.setText("");
                break;
            case R.id.bt_enter:
                pinStr = editDialogInput.getText().toString();

                if (isNullString(pinStr)){
                    ToastUtils.showToast(R.string.enter_pin);
                }else {
                    if (pinStr.equals(App.PIN)){
                        startActivity(new Intent(LoginActivity.this,StepActivity.class));

                    }else ToastUtils.showToast(R.string.error_input);
                }
                break;
        }
    }
}
