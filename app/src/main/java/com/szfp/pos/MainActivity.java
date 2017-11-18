package com.szfp.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.iv_company_logo)
    ImageView ivCompanyLogo;
    @BindView(R.id.tv_company_slogan)
    TextView tvCompanySlogan;
    @BindView(R.id.ll_home)
    LinearLayout llHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this);
    }

    @OnClick(R.id.ll_home)
    public void onClick() {
        if (SPUtils.getBoolean(this,App.isLogin))
        startActivity(new Intent(this,OperateActivity.class));
        else
        startActivity(new Intent(this,LoginActivity.class));
    }
}
