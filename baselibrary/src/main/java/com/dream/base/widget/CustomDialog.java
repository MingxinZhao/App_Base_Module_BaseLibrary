package com.dream.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import dream.base.R;


/**
 * 弹窗统一
 * Created by j-like on 2016/12/15.
 */
public class CustomDialog extends Dialog {

    private Context context;
    public TextView mTitleTv;
    public TextView mContentTv;
    public TextView mRightTv;
    public TextView mLeftTv;
    private DismissListener mDismissListener = null;
    private String title;
    private String content;
    private String confirm;
    private String cancel;

    public CustomDialog(Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
        resetStyle();
    }

    public void resetStyle() {
        this.setContentView(R.layout.dialog_common);
        initViews();

    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        //对于设置外部不可触摸消失的，需要把layout_bg_bing控件的点击消失去掉
        super.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void show() {
//        resetStyle();
        super.show();
    }

    public void initViews() {
        mRightTv = (TextView) findViewById(R.id.tv_sure);
        mLeftTv = (TextView) findViewById(R.id.tv_cancel);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mContentTv = (TextView) findViewById(R.id.tv_content);

        mRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDismissListener != null) {
                    mDismissListener.onCLickOk();
                }
                dismiss();
            }
        });
        mLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDismissListener != null) {
                    mDismissListener.onClickCancel();
                }
                dismiss();
            }
        });

        setTitle(title);
        setContent(content);
        setSureText(confirm);
        setCancelText(cancel);
    }

    public void setTitle(String text) {
        this.title = text;
        if (mTitleTv != null) {
            mTitleTv.setText(text);
        }
        if (TextUtils.isEmpty(text)) {
            mTitleTv.setVisibility(View.GONE);
        } else {
            mTitleTv.setVisibility(View.VISIBLE);
        }
    }

    public void setContent(String text) {
        this.content = text;
        if (mContentTv != null) {
            mContentTv.setText(text);
        }
        if (TextUtils.isEmpty(text)) {
            mContentTv.setVisibility(View.GONE);
        } else {
            mContentTv.setVisibility(View.VISIBLE);
        }
    }

    public void setContentSize(float size) {
        if (mContentTv != null) {
            mContentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    public void setCancelText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.cancel = text;
            mLeftTv.setText(text);
        }
    }

    public void setSureText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.confirm = text;
            mRightTv.setText(text);
        }
    }

    public void setCancelBtnVisible(boolean show) {
        mLeftTv.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setDismissListener(DismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public interface DismissListener {
        void Trigger(Object o);

        void onCLickOk();

        void onClickCancel();
    }

    @Override
    public void dismiss() {
        if (mDismissListener != null) {
            mDismissListener.Trigger(null);
        }
        super.dismiss();
    }
}
