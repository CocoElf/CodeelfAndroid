package cocoelf.codeelfandroid.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cocoelf.codeelfandroid.R;

/**
 * Created by green-cherry on 2018/3/6.
 */

public class ExpandablelistAdapter extends BaseExpandableListAdapter {

    private List<String>groupData;
    private List<String> childData;
    private List<ImageView> mIndicators;


    public ExpandablelistAdapter(List<String> groupData, List<String> childData) {
        this.groupData = groupData;
        this.childData = childData;
        this.mIndicators = new ArrayList<>();

    }
    //      根据分组的展开闭合状态设置指示器
    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        Log.e("iin",String.valueOf(mIndicators.size()));
        if (!isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.drawable.ic_expand_less);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.drawable.ic_expand_more);
        }
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1 ;
    }

    @Override
    public Object getGroup(int i) {
        return groupData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childData.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_timer_parent, viewGroup, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.typeTitle = (TextView) view.findViewById(R.id.code_type);
            groupViewHolder.time=(TextView) view.findViewById(R.id.code_time);
            groupViewHolder.ivIndicator = (ImageView) view.findViewById(R.id.iv_indicator);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.typeTitle.setText(groupData.get(i));
        groupViewHolder.time.setText("\t"+childData.get(i));
        //      把位置和图标添加到Map
        mIndicators.add(groupViewHolder.ivIndicator);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_timer_child, viewGroup, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) view.findViewById(R.id.timerView);
            childViewHolder.begin=(Button)view.findViewById(R.id.beginTimer);
            childViewHolder.end=(Button)view.findViewById(R.id.endTimer);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.tvTitle.setText(childData.get(i));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public static class GroupViewHolder {
        TextView typeTitle;
        TextView time;
        ImageView ivIndicator;
    }

    public static class ChildViewHolder {
        TextView tvTitle;
        public Button begin;
        public Button end;
    }

}
