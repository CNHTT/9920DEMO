package com.szfp.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.szfp.pos.model.LoginRecord;
import com.szfp.pos.utils.JsonUtil;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;

import java.util.Date;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.edit_dialog_input)
    EditText editDialogInput;
    @BindView(R.id.et_pin)
    EditText etPin;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_enter)
    Button btEnter;
    private String pinStr;
    private String tidStr;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private LoginRecord logRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparent(this);
        toolbar.setTitle("LOGIN");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.bt_cancel, R.id.bt_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                editDialogInput.setText("");
                etPin.setText("");
                break;
            case R.id.bt_enter:
                pinStr = etPin.getText().toString();
                tidStr = editDialogInput.getText().toString();

                if (isNullString(pinStr)&&isNullString(tidStr)) {
                    ToastUtils.showToast(R.string.enter_pin);
                } else {
                    if (pinStr.equals(App.PIN)) {
                        logRecord = new LoginRecord();
                        logRecord.setTerminalId(tidStr);
                        logRecord.setLoginDate(new Date());
                        App.TID =tidStr;
                        SPUtils.putString(this,App.LOGRECORD, JsonUtil.objectToString(logRecord));
                        SPUtils.putBoolean(this,App.isLogin,true);
                        finish();
                        startActivity(new Intent(LoginActivity.this, OperateActivity.class));

                    } else ToastUtils.showToast(R.string.error_input);
                }
                break;
        }
    }
}
