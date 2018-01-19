package com.szfp.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.szfp.pos.adapter.ListStringAdapter;
import com.szfp.pos.model.Item;
import com.szfp.utils.StatusBarUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_game_type)
    ListView lvGameType;
    @BindView(R.id.ll_game_type)
    LinearLayout llGameType;
//    @BindView(R.id.lv_game_type_option)
//    ListView lvGameTypeOption;
//    @BindView(R.id.ll_game_option)
//    LinearLayout llGameOption;
//    @BindView(R.id.lv_game_odd_type)
//    ListView lvGameOddType;
//    @BindView(R.id.ll_game_odd_type)
//    LinearLayout llGameOddType;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_game_nap)
    ListView lvGameNap;
    @BindView(R.id.ll_game_nap)
    LinearLayout llGameNap;


    private ListStringAdapter gameTypeAdapter;
    private ListStringAdapter gameTypeOptionAdapter;
    private ListStringAdapter gameOddTypeAdapter;
    private ListStringAdapter gameNpaTypeAdapter;

    private List<String> gameTypeList;
    private List<String> gameTypeOptionList;
    private List<String> gameOddTypeList;
    private List<String> gameNpaTypeList;

    private int type = 0;

    private int maxSelectSize =49;

    private String gameTypeStr;
    private String gameOptionStr;
    private String under;

    private Item item = new Item();


    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this);
        initData();

    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }

    private void initData() {


        toolbar.setTitle(R.string.please_select);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        gameTypeList = Arrays.asList(getResources().getStringArray(R.array.array_game_type));
        gameOddTypeList = Arrays.asList(getResources().getStringArray(R.array.array_under));
        gameNpaTypeList = Arrays.asList(getResources().getStringArray(R.array.array_game_nap));

        gameTypeAdapter = new ListStringAdapter(this, gameTypeList);
        gameOddTypeAdapter = new ListStringAdapter(this, gameOddTypeList);
        gameNpaTypeAdapter = new ListStringAdapter(this, gameNpaTypeList);

//       / lvGameOddType.setAdapter(gameOddTypeAdapter);
        lvGameType.setAdapter(gameTypeAdapter);
        lvGameNap.setAdapter(gameNpaTypeAdapter);


        lvGameType.setOnItemClickListener(this);
//        lvGameTypeOption.setOnItemClickListener(this);
        lvGameNap.setOnItemClickListener(this);
//        lvGameOddType.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_game_type: //select game type NAP/PERM OR GROUPING
                gameTypeStr = gameTypeList.get(position);
                item.setGameType(gameTypeStr);
                toolbar.setTitle(gameTypeStr);
                type = 1;
                showSelect();
                break;
            case R.id.lv_game_nap: //select game type NAP/PERM OR GROUPING
                under = gameNpaTypeList.get(position);
                item.setUnder(under);
                toolbar.setTitle( gameTypeStr + ">"+under);
                type = 2;
                maxSelectSize = position+3;
                showSelect();
                break;
        }

    }

    private void showSelect() {
        switch (type) {
            case 0:
                llGameType.setVisibility(View.VISIBLE);
                llGameNap.setVisibility(View.GONE);
//                llGameOption.setVisibility(View.GONE);
//                llGameOddType.setVisibility(View.GONE);
                break;
            case 1:

                if (gameTypeStr.equals("NAP")){
                    llGameType.setVisibility(View.GONE);
                    llGameNap.setVisibility(View.VISIBLE);
//                    llGameOption.setVisibility(View.GONE);
//                    llGameOddType.setVisibility(View.GONE);
                }else {
//
//                    llGameType.setVisibility(View.GONE);
//                    llGameNap.setVisibility(View.GONE);
//                    llGameOption.setVisibility(View.VISIBLE);
//                    llGameOddType.setVisibility(View.GONE);
                    //select games selection 8-9-16-23-25-27- etc
                    Intent intent = new Intent();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", item);
                    bundle.putInt("maxSelectSize", 49);
                    intent.putExtras(bundle);
                    intent.setClass(StepActivity.this, SelectActivity.class);
                    startActivity(intent);
                }

                break;
            case 2:
//                llGameType.setVisibility(View.GONE);
//                llGameNap.setVisibility(View.GONE);
//                llGameOption.setVisibility(View.GONE);
//                llGameOddType.setVisibility(View.VISIBLE);
                Intent intent = new Intent();

                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putInt("maxSelectSize", maxSelectSize);
                intent.putExtras(bundle);
                intent.setClass(StepActivity.this, SelectActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (type) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
                type = 0;
                showSelect();
                toolbar.setTitle(gameTypeStr + ">");
                break;
            case 2:
                type = 1;
                toolbar.setTitle( gameTypeStr + ">"+under);
                break;
        }
    }
}
