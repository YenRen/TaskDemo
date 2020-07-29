package com.dyhx.kdtask;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by YeRen on 2020/6/10.
 * Describe:
 */
public class DetailsDialog extends Dialog {

    /**
     * 显示的标题
     */
    private TextView titleTv ;

    private TextView tvStime,tvEtime,tvOthers;
    private String s ,e;
    private String name ,sOthers;

    /**
     * 按钮之间的分割线
     */
    private View columnLineView ;
    public DetailsDialog(Context context,String s,String e,String name,String sOthers) {
        super(context, R.style.CustomDialog);
        this.s = s;
        this.e = e;
        this.name = name;
        this.sOthers = sOthers;
    }

    /**
     * 都是内容数据
     */
    private String message;
    private String positive,negtive ;
    private int imageResId = -1 ;

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_dialog_view);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
    }


    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
        tvStime.setText(s);
        tvEtime.setText(e);
        tvOthers.setText(sOthers);

        //如果用户自定了title和message
        if (!TextUtils.isEmpty(name)) {
            titleTv.setText(name);
            titleTv.setVisibility(View.VISIBLE);
        }else {
            titleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    public void setContent(String s,String e){
        tvStime.setText(s);
        tvEtime.setText(e);
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        titleTv = (TextView) findViewById(R.id.title);
        tvStime = (TextView) findViewById(R.id.tv_start_time);
        tvEtime = (TextView) findViewById(R.id.tv_end_time);
        tvOthers = (TextView) findViewById(R.id.tv_others);

        columnLineView = findViewById(R.id.column_line);
    }

    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;
    public DetailsDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener{
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick(String tName, String tMsg);
        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
    }

    public String getMessage() {
        return message;
    }

    public DetailsDialog setMessage(String message) {
        this.message = message;
        return this ;
    }


    public String getPositive() {
        return positive;
    }

    public DetailsDialog setPositive(String positive) {
        this.positive = positive;
        return this ;
    }

    public String getNegtive() {
        return negtive;
    }

    public DetailsDialog setNegtive(String negtive) {
        this.negtive = negtive;
        return this ;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public DetailsDialog setSingle(boolean single) {
        isSingle = single;
        return this ;
    }

    public DetailsDialog setImageResId(int imageResId) {
        this.imageResId = imageResId;
        return this ;
    }


}
