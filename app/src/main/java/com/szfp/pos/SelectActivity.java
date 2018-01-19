package com.szfp.pos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.szfp.pos.adapter.ListStringAdapter;
import com.szfp.pos.model.Item;
import com.szfp.pos.model.PosRecord;
import com.szfp.pos.print.PrintFont;
import com.szfp.pos.utils.AidlUtil;
import com.szfp.pos.utils.JsonUtil;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.dialog.DialogEditSureCancel;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.flow.FlowAdapter;
import com.szfp.view.flow.FlowTagLayout;
import com.szfp.view.flow.OnTagSelectListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

public class SelectActivity extends BaseActivity {


    @BindView(R.id.fl_number)
    FlowTagLayout flNumber;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String numbersStr;
    private List<String> numbers;
    private List<String> rsults =new ArrayList<>();
    private List<Integer> list = new ArrayList<>();
    private FlowAdapter flowAdapter;
    private StringBuffer str;

    private int amount;
    private String amountStr;

    private BaseDialog dialog;
    private ListView listView;
    private ListStringAdapter adapter;
    private List<String> listString;
    private DialogEditSureCancel editDialog;
    private int maxSelectSize;
    private Item item;
    private PosRecord posRecord;
    private String posRecordStr;
    private String itemListStr;
    private List<Item> items;

    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);

        posRecordStr = SPUtils.getContent(this, PrintFont.POSRECORDSTR);

        maxSelectSize = getIntent().getIntExtra("maxSelectSize",49);

        if (isNullString(posRecordStr)) {
            posRecord = new PosRecord();
            posRecord.setSn(TimeUtils.generateSequenceNo());
            posRecord.setCreateTime(new Date());
            posRecord.setMatchPlayed(TimeUtils.getCurTimeDate());
            posRecord.setMatchPlayed(TimeUtils.getLastDayWeek());
            posRecord.setValidity(TimeUtils.getLastDayMonth());
            posRecord.setOperator(App.TID);
            posRecord.setSubmitTime(posRecord.getCreateTime());


            items = new ArrayList<>();
        } else {
            posRecord = (PosRecord) JsonUtil.stringToObject(posRecordStr, PosRecord.class);
            if (isNullString(posRecord.getList())) {
                items = new ArrayList<>();
            } else {
                items = JsonUtil.stringToList(posRecord.getList(), Item.class);
            }
        }


        item = (Item) getIntent().getSerializableExtra("item");

        if (item.getUnder()==null)toolbar.setTitle(item.getGameType() );
        else toolbar.setTitle(item.getGameType() + ">" + item.getUnder());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        numbersStr = getResources().getString(R.string.game_str).replace("(", "").replace(")", "");
        numbers = Arrays.asList(numbersStr.split(" "));
        flowAdapter = new FlowAdapter(this);
        flNumber.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        flNumber.setAdapter(flowAdapter);
        flowAdapter.onlyAddAll(numbers);
        flNumber.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                str = new StringBuffer();
                list = selectedList;
                rsults= new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    rsults.add( numbers.get(list.get(i)) );
                }
                Log.d("SELECT", selectedList.toString());
                for (int i = 0; i < rsults.size(); i++) {
                    str.append(rsults.get(i)+" ");
                }


                tvResult.setText(str.toString());

            }
        });

        flNumber.setMaxSelectSize(maxSelectSize);


    }


    @OnClick(R.id.bt_next)
    public void onClick() {
        if (list.size() > 2) {
            showSelectAmount();
        } else ToastUtils.showToast("The minimum number of selection is 3");

    }

    private void showSelectAmount() {

        if (editDialog == null) editDialog = new DialogEditSureCancel(mContext);
        editDialog.getTvTitle().setText("Please Input Amount");
        editDialog.getTvSure().setOnClickListener(OnClickListener);
        editDialog.getTvCancel().setOnClickListener(OnClickListener);
        editDialog.show();
    }


    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.tv_sure:
                    amountStr = editDialog.getEditText().getText().toString();
                    if (isNullString(amountStr)) {
                        ToastUtils.showToast("Please input amount");
                        return;
                    }

                    amount = Integer.parseInt(amountStr);

                    item.setAmount(amount);
                    item.setList(rsults);

                    items.add(item);
                    posRecord.setList(JsonUtil.objectToString(items));
                    posRecord.setTotalStake(posRecord.getTotalStake() + amount);
                    SPUtils.putString(SelectActivity.this, PrintFont.POSRECORDSTR, JsonUtil.objectToString(posRecord));

                    if (editDialog != null) editDialog.cancel();
                    showNext();

                    break;

                case R.id.tv_cancel:
                    if (editDialog != null) editDialog.cancel();
                    break;
            }
        }
    };
    /**
     *
     */
    private DialogSureCancel printDialog;

    private void showNext() {
        if (printDialog == null) {
            printDialog = new DialogSureCancel(this);
            printDialog.getTvContent().setText("Next it ask if there are other games.");
            printDialog.setCancelable(false);
            printDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(SelectActivity.this, StepActivity.class));
                    printDialog.cancel();
                }
            });
            printDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printDialog.cancel();
                    finish();
                    Intent intent = new Intent(SelectActivity.this, ResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("POS", posRecord);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        printDialog.show();
    }

    private Bitmap bitmap;

    /**
     * 打印
     */
    private void print() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_costomer_logo);

        String model = Build.MODEL;
        Log.d("Model", model);
        if (model.equals("V1-B18")) {
            AidlUtil.getInstance().printText("POS SOFTWARE\n", 0, 36, true, false);
            AidlUtil.getInstance().printText("**************************\n", 0, 28, false, false);
            AidlUtil.getInstance().printText(App.companyName, 1, 36, true, false);
            AidlUtil.getInstance().printText("[COD]\n", 1, 36, true, false);
            AidlUtil.getInstance().printBitmap(scaleBitmap(bitmap, 0.3F));
            AidlUtil.getInstance().printText("\n" + App.slogan + "\n", 1, 20, false, true);
        }

    }


    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }


    private Bitmap scaleImage(Bitmap bitmap1) {
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        // 设置想要的大小
        int newWidth = (width / 8 + 1) * 8;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, 1);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix,
                true);
        return newbm;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void showConnecting() {
        ToastUtils.showToast("connection ........");
    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {
        ToastUtils.showToast("connection succeeded");
    }


}
