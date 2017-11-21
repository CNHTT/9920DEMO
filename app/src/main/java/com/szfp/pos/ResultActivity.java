package com.szfp.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.szfp.pos.adapter.ItemAdapter;
import com.szfp.pos.model.Item;
import com.szfp.pos.model.LoginRecord;
import com.szfp.pos.model.PosRecord;
import com.szfp.pos.print.PrintFont;
import com.szfp.pos.print.PrintManager;
import com.szfp.pos.utils.DbHelper;
import com.szfp.pos.utils.JsonUtil;
import com.szfp.utils.AppManager;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static com.szfp.utils.DataUtils.isNullString;

public class ResultActivity extends BaseActivity {



    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_list)
    ListView lvList;


    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_enter)
    Button btEnter;
    @BindView(R.id.tv_validity)
    TextView tv_validity;
    @BindView(R.id.tv_match_play)
    TextView tv_match_play;

    protected Flowable<Integer> flowable;
    protected Subscriber<Integer> subscriber;
    private PosRecord posRecord;
    private String posRecordStr;
    private String itemListStr;
    private List<Item> items;
    private int  total=0;

    private boolean is =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);

        if (getIntent().getBooleanExtra("IS",false)){
            is =false;
            btCancel.setVisibility(View.GONE);
            posRecord = (PosRecord) getIntent().getSerializableExtra("POS");
        }else {
            posRecordStr = SPUtils.getContent(this, PrintFont.POSRECORDSTR);
            posRecord = (PosRecord) JsonUtil.stringToObject(posRecordStr,PosRecord.class);
        }


        items  = JsonUtil .stringToList(posRecord.getList(),Item.class);
        toolbar.setTitle(posRecord.getTsn());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_match_play.setText(TimeUtils.date3String(posRecord.getMatchPlayed()));
        tv_validity.setText(TimeUtils.date3String(posRecord.getValidity()));

        ItemAdapter itemAdapter = new ItemAdapter(this,items);
        lvList.setAdapter(itemAdapter);



    }












    @OnClick({R.id.bt_cancel, R.id.bt_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                AppManager.getAppManager().finishAllActivityAndExit(this);
                SPUtils.putString(this,PrintFont.POSRECORDSTR,"");
                startActivity(new Intent(this,OperateActivity.class));
                break;
            case R.id.bt_enter:
                if (is){
                    SPUtils.putInt(this,PrintFont.soldQty,SPUtils.getInt(this, PrintFont.soldQty)+1);
                    SPUtils.putInt(this,PrintFont.soldAmount,SPUtils.getInt(this, PrintFont.soldAmount)+posRecord.getTotalStake());

                    DbHelper.AddPosRecord(posRecord);
                    PrintManager.getmInstance(ResultActivity.this).printPosRecord(posRecord);

                    AppManager.getAppManager().finishAllActivityAndExit(this);
                    SPUtils.putString(this,PrintFont.POSRECORDSTR,"");
                    startActivity(new Intent(this,OperateActivity.class));
                }else PrintManager.getmInstance(ResultActivity.this).printPosRecord(posRecord);


//                enter();
                break;
        }
    }

    private void enter() {
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

                PrintManager.getmInstance(ResultActivity.this).printPosRecord(posRecord);

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                subscription.cancel();
            }
        };


        flowable.subscribe(subscriber);
    }


    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }
    @Override
    protected void showDisconnecting() {

    }

}
