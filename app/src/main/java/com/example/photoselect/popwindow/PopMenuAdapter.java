package com.example.photoselect.popwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.photoselect.R;

import java.util.List;

public class PopMenuAdapter extends BaseAdapter {
    public final static String DEFAULT_STYLE = "default_style";
    public final static String LOGIN_CHANNEL_STYLE = "login_channel_style";

    private Context mContext;
    private List<PopMenuModel> popData;
    private String itemStyle;

    public PopMenuAdapter(Context mContext, List<PopMenuModel> popData) {
        this.mContext = mContext;
        this.popData = popData;
        this.itemStyle = DEFAULT_STYLE;
    }

    @Override
    public int getCount() {
        return popData!=null?popData.size():0;
    }

    @Override
    public PopMenuModel getItem(int position) {
        return popData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            TextView tv = new TextView(mContext);
            tv.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            tv.setHeight(SizeUtils.dp2px(mContext, 50));
            tv.setPadding(SizeUtils.dp2px(mContext, 5), 0, 0, SizeUtils.dp2px(mContext, 5));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            convertView = tv;
            convertView.setTag(position);
        } else {
            convertView.getTag();
        }
        ((TextView) convertView).setText(getItem(position).getName());
        ((TextView) convertView).setTextColor(mContext.getResources().getColor(
                getItem(position).getStyle() == MenuPopWindow.MenuStyle.DANGER ? R.color.pop_menu_red : R.color.pop_menu_black
        ));

        if (itemStyle.equals(LOGIN_CHANNEL_STYLE)) {
            ((TextView) convertView).setPadding(SizeUtils.dp2px(mContext, 16), 0, 0, SizeUtils.dp2px(mContext, 5));
            ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL);
            ((TextView) convertView).setTextColor(mContext.getResources().getColor(
                    R.color.pop_menu_gray_tex
            ));
        }
        return convertView;
    }

    public void setStyle(String valeStyle) {
        this.itemStyle = valeStyle;
    }

    public void clear() {
        if (popData!=null){
            popData.clear();
            notifyDataSetChanged();
        }
    }
}
