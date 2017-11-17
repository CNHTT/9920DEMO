package com.szfp.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.szfp.pos.adapter.ListStringAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_game_type)
    ListView lvGameType;
    @BindView(R.id.ll_game_type)
    LinearLayout llGameType;
    @BindView(R.id.lv_game_type_option)
    ListView lvGameTypeOption;
    @BindView(R.id.ll_game_option)
    LinearLayout llGameOption;
    @BindView(R.id.lv_game_odd_type)
    ListView lvGameOddType;
    @BindView(R.id.ll_game_odd_type)
    LinearLayout llGameOddType;
    @BindView(R.id.tv_option)
    TextView tvOption;
    @BindView(R.id.tv_odd)
    TextView tvOdd;


    private ListStringAdapter gameTypeAdapter;
    private ListStringAdapter gameTypeOptionAdapter;
    private ListStringAdapter gameOddTypeAdapter;

    private List<String> gameTypeList;
    private List<String> gameTypeOptionList;
    private List<String> gameOddTypeList;

    private int type = 0;


    private String gameTypeStr;
    private String gameOptionStr;
    private String gameOddStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        gameTypeList = Arrays.asList(getResources().getStringArray(R.array.array_game_type));
        gameTypeOptionList = Arrays.asList(getResources().getStringArray(R.array.array_game_option));
        gameOddTypeList = Arrays.asList(getResources().getStringArray(R.array.array_under));

        gameTypeAdapter = new ListStringAdapter(this, gameTypeList);
        gameTypeOptionAdapter = new ListStringAdapter(this, gameTypeOptionList);
        gameOddTypeAdapter = new ListStringAdapter(this, gameOddTypeList);

        lvGameOddType.setAdapter(gameOddTypeAdapter);
        lvGameType.setAdapter(gameTypeAdapter);
        lvGameTypeOption.setAdapter(gameTypeOptionAdapter);


        lvGameType.setOnItemClickListener(this);
        lvGameTypeOption.setOnItemClickListener(this);

        lvGameOddType.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_game_type: //select game type NAP/PERM OR GROUPING
                gameTypeStr = gameTypeList.get(position);
                tvTitle.setText(gameTypeStr+">");

                type = 1;
                showSelect();
                break;
            case R.id.lv_game_type_option://select game option 100-1 or 80-1 or 40-1
                gameOptionStr = gameTypeOptionList.get(position);
                tvOption.setText(gameOptionStr);
                type = 2;
                showSelect();
                break;
            case R.id.lv_game_odd_type://select odd type u3,u4,u5,u6
                gameOddStr = gameOddTypeList.get(position);
                tvOdd.setText("("+gameOddStr+")");




                //select games selection 8-9-16-23-25-27- etc
                Intent intent = new Intent();
                intent.setClass(StepActivity.this,SelectActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void showSelect() {
        switch (type){
            case 0:
                llGameType.setVisibility(View.VISIBLE);
                llGameOption.setVisibility(View.GONE);
                llGameOddType.setVisibility(View.GONE);
                break;
            case 1:
                llGameType.setVisibility(View.GONE);
                llGameOption.setVisibility(View.VISIBLE);
                llGameOddType.setVisibility(View.GONE);
                break;
            case 2:
                llGameType.setVisibility(View.GONE);
                llGameOption.setVisibility(View.GONE);
                llGameOddType.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (type){
            case 0:
                super.onBackPressed();
                break;
            case 1:
                type =0;
                showSelect();
                tvOption.setText("");
                break;
            case 2:
                type =1;
                showSelect();
                tvOdd.setText("");
                break;
        }
    }
}
