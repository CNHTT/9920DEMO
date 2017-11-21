package com.szfp.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.szfp.pos.model.PosRecord;
import com.szfp.pos.utils.Constant;
import com.szfp.pos.utils.DbHelper;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isEmpty;
import static com.szfp.utils.DataUtils.isNullString;

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.transaction_details_root)
    LinearLayout mTransaction_details_root;
    @BindView(R.id.history_search_layout)
    LinearLayout mHistory_search_layout;
    @BindView(R.id.history_search_edit)
    EditText mHistory_search_edit;
    @BindView(R.id.history_search)
    ImageView mHistory_search;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.history_lv)
    ListView mHistory_lv;

    private PosRecord posRecord;

    private String tsn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
    }

    @OnClick({R.id.history_search,R.id.scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_search:

                tsn = mHistory_search_edit.getText().toString();
                if (isNullString(tsn)){
                    ToastUtils.showToast(R.string.please_input_traceno_hint);
                    return;
                }

                posRecord  = DbHelper.getPosRecordByTsn(tsn);
                if (isEmpty(posRecord)){
                    ToastUtils.showToast(R.string.not_any_record);
                    return;
                }

                Intent intent = new Intent(SearchActivity.this,ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("POS",posRecord);
                bundle.putBoolean("IS",true);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.scan:

                intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivity(intent);

                break;
        }
    }
}
