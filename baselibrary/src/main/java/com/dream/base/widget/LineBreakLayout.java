package com.dream.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dream.base.common.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import dream.base.R;

/**
 * Created by wang on 2017/4/5.
 */

public class LineBreakLayout extends ViewGroup {

    private final static String TAG = "LineBreakLayout";
    /**
     * 所有标签
     */
    private List<String> labels;
    /**
     * 选中标签
     */
    private List<String> labelSelected = new ArrayList<>();

    //自定义属性
    private int LEFT_RIGHT_SPACE; //dip

    private int ROW_SPACE;

    private int BACK_GROUND;

    private int TEXT_COLOR_NORMAL;

    private int TEXT_COLOR_SELECTED;

    private int ITEM_RES;

    private OnLabelClickListener onLabelClickListener;

    public LineBreakLayout(Context context) {
        this(context, null);
    }

    public LineBreakLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineBreakLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineBreakLayout);
        LEFT_RIGHT_SPACE = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_leftAndRightSpace, 10);
        ROW_SPACE = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_rowSpace, 10);

        BACK_GROUND = ta.getResourceId(R.styleable.LineBreakLayout_label_background, -1);

        TEXT_COLOR_NORMAL = ta.getColor(R.styleable.LineBreakLayout_text_color_normal, -1);
        TEXT_COLOR_SELECTED = ta.getColor(R.styleable.LineBreakLayout_text_color_selected, -1);

        ITEM_RES = ta.getResourceId(R.styleable.LineBreakLayout_item_res, R.layout.widget_item_label);

        if (TEXT_COLOR_NORMAL == -1) {
            TEXT_COLOR_NORMAL = ContextCompat.getColor(context, R.color.tv_gray);
        }

        if (TEXT_COLOR_SELECTED == -1) {
            TEXT_COLOR_SELECTED = ContextCompat.getColor(context, R.color.tv_blue);
        }

        ta.recycle(); //回收

    }

    public void setLabels(List<String> labels) {
        setLabels(labels, false, false);
    }

    public void setLabels(List<String> labels, boolean add) {
        setLabels(labels, add, false);
    }

    public OnLabelClickListener getOnLabelClickListener() {
        return onLabelClickListener;
    }

    public void setOnLabelClickListener(OnLabelClickListener onLabelClickListener) {
        this.onLabelClickListener = onLabelClickListener;
    }

    public void setLabelsWithFinalWidth(List<String> labels, boolean add, boolean isCanSelected) {
        if (this.labels == null) {
            this.labels = new ArrayList<>();
        }
        if (add) {
            this.labels.addAll(labels);
        } else {
            this.labels.clear();
            this.labels = labels;
        }
        if (labels != null && labels.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            int position = 0;

            for (final String label : labels) {
                //获取标签布局
                final TextView tv = (TextView) inflater.inflate(ITEM_RES, null);

                LayoutParams params = new LayoutParams(ScreenUtils.dip2px(getContext(), 80), ScreenUtils.dip2px(getContext(), 35));
                tv.setLayoutParams(params);

                if (BACK_GROUND != -1) {
                    tv.setBackgroundResource(BACK_GROUND);
                }

                tv.setText(label);

                //设置选中效果
                if (labelSelected.contains(label)) {
                    //选中
                    tv.setSelected(true);
                    tv.setTextColor(TEXT_COLOR_SELECTED);
                } else {
                    //未选中
                    tv.setSelected(false);
                    tv.setTextColor(TEXT_COLOR_NORMAL);
                }

                if (isCanSelected) {
                    //点击标签后，重置选中效果
                    final int finalPosition = position;
                    
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (onLabelClickListener != null) {
                                onLabelClickListener.onClick(finalPosition);
                            }

                            tv.setSelected(tv.isSelected() ? false : true);
                            if (tv.isSelected()) {
                                tv.setTextColor(TEXT_COLOR_SELECTED);
                                //将选中的标签加入到labelSelected中
                                labelSelected.add(label);
                            } else {
                                tv.setTextColor(TEXT_COLOR_NORMAL);
                                labelSelected.remove(label);
                            }
                        }
                    });
                }
                //将标签添加到容器中
                addView(tv);
                position++;
            }
        }
    }

    /**
     * 添加标签
     *
     * @param labels 标签集合
     * @param add    是否追加
     */
    public void setLabels(List<String> labels, boolean add, boolean isCanSelected) {
        if (this.labels == null) {
            this.labels = new ArrayList<>();
        }
        if (add) {
            this.labels.addAll(labels);
        } else {
            this.labels.clear();
            this.labels = labels;
        }
        if (labels != null && labels.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (final String label : labels) {
                //获取标签布局
                final TextView tv = (TextView) inflater.inflate(ITEM_RES, null);
                if (BACK_GROUND != -1) {
                    tv.setBackgroundResource(BACK_GROUND);
                }

                tv.setText(label);

                //设置选中效果
                if (labelSelected.contains(label)) {
                    //选中
                    tv.setSelected(true);
                    tv.setTextColor(TEXT_COLOR_SELECTED);
                } else {
                    //未选中
                    tv.setSelected(false);
                    tv.setTextColor(TEXT_COLOR_NORMAL);
                }

                if (isCanSelected) {
                    //点击标签后，重置选中效果
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tv.setSelected(tv.isSelected() ? false : true);
                            if (tv.isSelected()) {
                                tv.setTextColor(TEXT_COLOR_SELECTED);
                                //将选中的标签加入到labelSelected中
                                labelSelected.add(label);
                            } else {
                                tv.setTextColor(TEXT_COLOR_NORMAL);
                                labelSelected.remove(label);
                            }
                        }
                    });
                }
                //将标签添加到容器中
                addView(tv);
            }
        }
    }

    /**
     * 获取选中标签
     */
    public List<String> getSelectedLabels() {
        return labelSelected;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //为所有的标签childView计算宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //获取高的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //建议的高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //布局的宽度采用建议宽度（match_parent或者size），如果设置wrap_content也是match_parent的效果
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果高度模式为EXACTLY（match_parent或者size），则使用建议高度
            height = heightSize;
        } else {
            //其他情况下（AT_MOST、UNSPECIFIED）需要计算计算高度
            int childCount = getChildCount();
            if (childCount <= 0) {
                height = 0;   //没有标签时，高度为0
            } else {
                int row = 1;  // 标签行数
                int widthSpace = width;// 当前行右侧剩余的宽度
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    //获取标签宽度
                    int childW = view.getMeasuredWidth();

                    if (widthSpace >= childW) {
                        //如果剩余的宽度大于此标签的宽度，那就将此标签放到本行
                        widthSpace -= childW;
                    } else {
                        row++;    //增加一行
                        //如果剩余的宽度不能摆放此标签，那就将此标签放入一行
                        widthSpace = width - childW;
                    }
                    //减去标签左右间距
                    widthSpace -= LEFT_RIGHT_SPACE;
                }
                //由于每个标签的高度是相同的，所以直接获取第一个标签的高度即可
                int childH = getChildAt(0).getMeasuredHeight();
                //最终布局的高度=标签高度*行数+行距*(行数-1)
                height = (childH * row) + ROW_SPACE * (row - 1);

            }
        }

        //设置测量宽度和测量高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row = 0;
        int right = 0;   // 标签相对于布局的右侧位置
        int bottom;       // 标签相对于布局的底部位置
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childW = childView.getMeasuredWidth();
            int childH = childView.getMeasuredHeight();
            //右侧位置=本行已经占有的位置+当前标签的宽度
            right += childW;
            //底部位置=已经摆放的行数*（标签高度+行距）+当前标签高度
            bottom = row * (childH + ROW_SPACE) + childH;
            // 如果右侧位置已经超出布局右边缘，跳到下一行
            // if it can't drawing on a same line , skip to next line
            if (right > (r - LEFT_RIGHT_SPACE)) {
                row++;
                right = childW;
                bottom = row * (childH + ROW_SPACE) + childH;
            }

            childView.layout(right - childW, bottom - childH, right, bottom);

            right += LEFT_RIGHT_SPACE;
        }
    }

    interface OnLabelClickListener {
        void onClick(int position);
    }
}
