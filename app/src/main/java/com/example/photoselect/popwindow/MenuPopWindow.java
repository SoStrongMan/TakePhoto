package com.example.photoselect.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.photoselect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * popWindow
 */
public class MenuPopWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    public interface OnPopItemClickListener {
        void onItemClick(View view, int position);

        void onCancelClick(View view);
    }

    @Override
    public void onDismiss() {
        closePopWindow();
    }

    public enum MenuStyle {
        NORMAL, DANGER,
    }

    private Context mContext;
    private View contentView;
    private ListView popListView;
    private TextView CancelView;

    private OnPopItemClickListener itemClickListener;
    private List<PopMenuModel> datas;

    private MenuPopWindow(Builder builder) {
        this.mContext = builder.context;
        this.datas = builder.datas;
        this.itemClickListener = builder.listener;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_window_menu, null);
        initView(contentView);
        initData();
        initEvent();
    }

    private void initView(View contentView) {
        popListView = (ListView) contentView.findViewById(R.id.ll_ll_lv_item);
        CancelView = (TextView) contentView.findViewById(R.id.ll_ll_tv_cancel);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(0));
    }

    private void initData() {
        if (datas.size() > 0) {
            popListView.setVisibility(View.VISIBLE);
        } else {
            popListView.setVisibility(View.GONE);
        }
        popListView.setAdapter(new PopMenuAdapter(mContext, datas));
    }

    private void initEvent() {

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onCancelClick(v);
                }
                dismiss();
            }
        });

        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, position);
                }
                dismiss();
            }
        });
        CancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onCancelClick(v);
                }
                dismiss();
            }
        });
        setOnDismissListener(this);
    }

    public void showPopWindow(View parent, int gravity) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, gravity, 0, 0);
            openPopWindow();
        } else {
            this.dismiss();
        }
    }

    private void closePopWindow() {
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 1f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    private void openPopWindow() {
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    public static class Builder {
        private Context context;
        private List<PopMenuModel> datas;
        private OnPopItemClickListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onItemClickListener(OnPopItemClickListener itemClickListener) {
            this.listener = itemClickListener;
            return this;
        }

        public Builder addItem(PopMenuModel item) {
            if (datas == null) {
                datas = new ArrayList<>();
            }
            datas.add(item);
            return this;
        }

        public MenuPopWindow build() {
            return new MenuPopWindow(this);
        }
    }
}
