package com.zhu.mygame;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhu.mygame.bean.ScoreBean;
import com.zhu.mygame.db.ScoreDao;

import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private ListView listview;
    private TextView tvEmptyRecord;
    private Context mContext;

    private List<ScoreBean> mData;
    private ScoreDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mContext = this;

        listview = findViewById(R.id.listview);
        tvEmptyRecord = findViewById(R.id.tv_empty_record);
        listview.setEmptyView(tvEmptyRecord);

        mDao = new ScoreDao();
        initData();
        initAdapter();
    }

    private void initData() {
        mData = mDao.selectAll();
    }

    private void initAdapter() {

        listview.setAdapter(new BaseAdapter() {

            LayoutInflater mInflater = LayoutInflater.from(mContext);

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Object getItem(int position) {
                return mData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ScoreBean bean = mData.get(position);

                convertView = mInflater.inflate(R.layout.adapter_record, null);
                TextView tvScore = convertView.findViewById(R.id.tv_score);
                TextView tvDuration = convertView.findViewById(R.id.tv_duration);
                TextView tvGameTime = convertView.findViewById(R.id.tv_game_time);

                tvScore.setText(bean.getScore() + "");
                tvDuration.setText(bean.getSpendTime());
                tvGameTime.setText(bean.getGameTime());

                return convertView;
            }
        });

    }

}
