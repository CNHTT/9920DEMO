package com.szfp.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.szfp.pos.model.LoginRecord;
import com.szfp.pos.print.PrintFont;
import com.szfp.pos.print.PrintManager;
import com.szfp.pos.utils.JsonUtil;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class OperateActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.play_game)
    TextView playGame;
    @BindView(R.id.logout)
    TextView logout;

    private LoginRecord loginRecord;

    protected Flowable<Integer> flowable;
    protected Subscriber<Integer> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparent(this);
        toolbar.setTitle("Choose Operation");
        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.play_game, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_game:
                startActivity(new Intent(this,StepActivity.class));
                break;
            case R.id.logout:
                logout();

                break;
        }
    }

    private void logout(){
        loginRecord = (LoginRecord) JsonUtil.stringToObject(SPUtils.getString(this,App.LOGRECORD),LoginRecord.class);
        loginRecord.setLogoutDate(new Date());
        loginRecord.setSoldQty(SPUtils.getInt(this, PrintFont.soldQty));
        loginRecord.setSoldAmount(SPUtils.getFloat(this, PrintFont.soldAmount));
        loginRecord.setPaymentQty(SPUtils.getInt(this, PrintFont.paymentQty));
        loginRecord.setPaymentAmount(SPUtils.getFloat(this, PrintFont.paymentAmount));
        loginRecord.setNetAmount(SPUtils.getFloat(this, PrintFont.netAmount));

        flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                if (!e.isCancelled()) {
                    e.onNext(1);
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.DROP);
        subscriber = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                subscription.request(1);
            }

            @Override
            public void onNext(Integer s) {
                if (PrintManager.getmInstance(OperateActivity.this
                ).ptintLoginRecord(loginRecord)==0) ToastUtils.success("success");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                subscription.cancel();
            }
        };


//        flowable.subscribe(subscriber);

        SPUtils.putString(this,App.isLogin,"");
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}
