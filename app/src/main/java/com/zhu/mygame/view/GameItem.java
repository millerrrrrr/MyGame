package com.zhu.mygame.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.zhu.mygame.R;

@SuppressLint("AppCompatCustomView")
public class GameItem extends TextView {

    private Context mContext;
    private int mNum = 0;
    private Resources mRes;

    public GameItem(Context context) {
        this(context, null);
    }

    public GameItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {
        mContext = getContext();
        mRes = getResources();

        setBackgroundColor(mRes.getColor(R.color.bg_item_normal));

        setTextSize(18);
        setGravity(Gravity.CENTER);
        getPaint().setFakeBoldText(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String content = mNum + "";
        Rect rect = new Rect();
        getPaint().getTextBounds(content, 0, content.length(), rect);
        setPadding(getPaddingLeft(),
                (getMeasuredHeight() - rect.height()) * 1 / 6,
                getPaddingRight(),
                getPaddingBottom());
    }

    public int getNum() {
        return this.mNum;
    }

    public void setNum(int num) {
        this.mNum = num;
        parseNumColor();
    }

    private void parseNumColor() {

        switch (mNum) {
            case 0:
                setBackgroundColor(mRes.getColor(R.color.bg_item_normal));
                break;
            case 1:
                setTextColor(mRes.getColor(R.color.text_2_color));
                setBackgroundColor(mRes.getColor(R.color.bg_item_2Back));
                break;
            case 2:
                setTextColor(mRes.getColor(R.color.text_2_color));
                setBackgroundColor(mRes.getColor(R.color.bg_item_2Back));
                break;
            case 4:
                setTextColor(mRes.getColor(R.color.text_4_color));
                setBackgroundColor(mRes.getColor(R.color.bg_item_4Back));
                break;
            case 8:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_8Back);
                break;
            case 16:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_16Back);
                break;
            case 32:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_32Back);
                break;
            case 64:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_64Back);
                break;
            case 128:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_128Back);
                break;
            case 256:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_256Back);
                break;
            case 512:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_512Back);
                break;
            case 1024:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_1024Back);
                break;
            case 2048:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_2048Back);
                break;
            case 4096:
                setTextColor(getResources().getColor(R.color.text_other_color));
                setBackgroundResource(R.color.bg_item_2048Back);
                break;
            default:
                setTextColor(getResources().getColor(R.color.text_2_color));
                setBackgroundResource(R.color.bg_item_2Back);
                break;
        }

        setText(mNum <= 0 ? "" : String.valueOf(mNum));
    }

    public boolean equals(GameItem item) {
        return getNum() == item.getNum();
    }

    public GameItem clone() {
        GameItem item = new GameItem(mContext);
        item.setNum(getNum());
        item.setLayoutParams(getLayoutParams());
        item.setGravity(Gravity.CENTER);

        String content = mNum + "";
        Rect rect = new Rect();
        item.getPaint().getTextBounds(content, 0, content.length(), rect);
        item.setPadding(getPaddingLeft(),
                (getMeasuredHeight() - rect.height()) * 2 / 5,
                getPaddingRight(),
                getPaddingBottom());

        return item;
    }

}
