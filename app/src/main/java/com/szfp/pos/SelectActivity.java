package com.szfp.pos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.szfp.pos.adapter.ListStringAdapter;
import com.szfp.pos.model.Item;
import com.szfp.pos.utils.AidlUtil;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.dialog.DialogEditSureCancel;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.flow.FlowAdapter;
import com.szfp.view.flow.FlowTagLayout;
import com.szfp.view.flow.OnTagSelectListener;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Integer> list = new ArrayList<>();
    private FlowAdapter flowAdapter;
    private StringBuffer str;

    private float amount;
    private String amountStr;

    private BaseDialog dialog;
    private ListView listView;
    private ListStringAdapter adapter;
    private List<String> listString;
    private DialogEditSureCancel editDialog;

    private Item item;

    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);


        item = (Item) getIntent().getSerializableExtra("item");
        toolbar.setTitle(item.getGameType()+">"+item.getGameOption()+">"+item.getOldType());
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
                Log.d("SELECT", selectedList.toString());
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        str.append(list.get(0));
                    } else if (i == list.size() - 1) {
                        if (list.size() != 1)
                            str.append("-" + list.get(list.size() - 1));
                    } else str.append("-" + list.get(i) + "-");

                }

                tvResult.setText(str.toString());

            }
        });

        flNumber.setMaxSelectSize(20);


    }


    @OnClick(R.id.bt_next)
    public void onClick() {
        if (list.size() > 2) {
//            if (dialog == null)
//            {
//                View view = ContextUtils.inflate(this,R.layout.dialog_select_option);
//                listView = (ListView) view.findViewById(R.id.ll_option_list);
//                listString = new ArrayList<>();
//                listString.add("80 TO 1 OPTIONS");
//                listString.add("100 TO 1 OPTIONS");
//                adapter = new ListStringAdapter(this,listString);
//                listView.setAdapter(adapter);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        dialog.cancel();
//                        BluetoothService.BT_Write("Szfffff");
//                    }
//                });
//
//                dialog = new BaseDialog(this,R.style.CustomDialog);
//                dialog.setContentView(view);
//                dialog.setCancelable(false);
//
//            }
//            dialog.show();


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
                    amountStr = editDialog.getEditText().toString();
                    if (isNullString(amountStr))
                    {
                        ToastUtils.showToast("Please input amount");
                        return;
                    }

                    amount = Float.parseFloat(amountStr);

                    item.setAmount(amount);
                    item.setList(list);


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
                    print();
                    printDialog.cancel();
                }
            });
            printDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printDialog.cancel();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_print:
                showDeviceList();
                break;
            case  android.R.id.home:
                onBackPressed();
                // 处理返回逻辑
                return true;
        }
        return super.onOptionsItemSelected(item);
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
