package cocoelf.codeelfandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.json.MemoModel;
import cocoelf.codeelfandroid.json.SearchResultModel;

/**
 * Created by shea on 2018/2/27.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> implements View.OnClickListener {
    private List<SearchResultModel> searchResultModelList;
    private OnItemClickListener mOnItemClickListener = null;

    private static final String TAG = "SearchResultAdapter";

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public SearchResultAdapter(List<SearchResultModel> searchResultModelList) {
        this.searchResultModelList = searchResultModelList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_result_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    private String getTitle(String type,String name){
        String temp = "["+type+"]  "+name;
        if(temp.length()>44){
            return temp.substring(0,44);
        }else {
            return temp;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResultModel searchResultModel = searchResultModelList.get(position);
        SpannableString string = new SpannableString(getTitle(searchResultModel.getType(),searchResultModel.getName()));
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#F55D54")),0,searchResultModel.getType().length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.searchResultItemName.setText(string);
        String fDate = formatDate(searchResultModel.getDateLastCrawled());
        SpannableString das = new SpannableString(fDate+" - "+searchResultModel.getSnippet());
        das.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),0,fDate.length()+3,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.searchResultItemDateAndSnippet.setText(das);
        if(holder.searchResultItemKeywordPart.getChildCount()==0){
            Context context = holder.searchResultItemKeywordPart.getContext();
            Log.i(TAG, "onBindViewHolder: "+searchResultModel.getKeywords());
            for (String keyword:searchResultModel.getKeywords()) {
                TextView textView = new TextView(context);
                textView.setText(keyword);
                textView.setTextSize(12);
                textView.setTextColor(Color.parseColor("#FFBA69"));
                textView.setBackgroundColor(Color.parseColor("#eeeeee"));
                textView.setPadding(24,4,24,14);
                holder.searchResultItemKeywordPart.addView(textView);
                TextView margin = new TextView(context);
                margin.setText("    ");
                holder.searchResultItemKeywordPart.addView(margin);
            }
        }
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    private String formatDate(Date date){
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return searchResultModelList.size();
    }


    @Override
    public void onClick(View view) {
        if(mOnItemClickListener!=null){
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView searchResultItemName;
        TextView searchResultItemDateAndSnippet;
        LinearLayout searchResultItemKeywordPart;

        public ViewHolder(View itemView) {
            super(itemView);
            searchResultItemName = (TextView)itemView.findViewById(R.id.fragment_search_result_item_name);
            searchResultItemDateAndSnippet = (TextView)itemView.findViewById(R.id.fragment_search_result_item_date_and_snippet);
            searchResultItemKeywordPart = (LinearLayout)itemView.findViewById(R.id.fragment_search_result_item_keyword_part);
        }
    }

}
