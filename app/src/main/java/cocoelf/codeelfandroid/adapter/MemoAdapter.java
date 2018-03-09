package cocoelf.codeelfandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.component.PaddingBackgroundColorSpan;
import cocoelf.codeelfandroid.json.MemoModel;

/**
 * Created by shea on 2018/3/1.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> implements View.OnClickListener {
    private List<MemoModel> memoModelList;
    private MemoAdapter.OnItemClickListener mOnItemClickListener = null;

    private static final String TAG = "MemoAdapter";

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public MemoAdapter(List<MemoModel> memoModelList) {
        this.memoModelList = memoModelList;
    }

    public void setOnItemClickListener(MemoAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_memo_item,parent,false);
        MemoAdapter.ViewHolder viewHolder = new MemoAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    private String geneKeywords(List<String> keywords){
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        for(i =0;i<keywords.size()-1;i++){
            stringBuilder.append(keywords.get(i)+" | ");
        }
        stringBuilder.append(keywords.get(i));
        if(stringBuilder.length()>40){
            return stringBuilder.substring(0,40);
        }else {
            return stringBuilder.toString();
        }
    }

    private String formatDate(Date date){
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    @Override
    public void onBindViewHolder(MemoAdapter.ViewHolder holder, int position) {
        MemoModel memoModel = memoModelList.get(position);
        holder.memoItemName.setText(memoModel.getName());
        holder.memoItemSnippet.setText(memoModel.getSnippet());
        holder.memoItemDate.setText(formatDate(memoModel.getDate()));
        holder.memoItemType.setText("["+memoModel.getType()+"]");
        holder.memoItemKeywords.setText(geneKeywords(memoModel.getKeywords()));
//        if(holder.memoItemKeywordPart.getChildCount()==0){
//            Context context = holder.memoItemKeywordPart.getContext();
//            Log.i(TAG, "onBindViewHolder: "+memoModel.getKeywords().size()+"--"+holder.memoItemKeywordPart.getChildCount());
//            for (String keyword:memoModel.getKeywords()) {
//                TextView textView = new TextView(context);
//                textView.setText(keyword);
//                textView.setTextSize(12);
//                textView.setTextColor(Color.parseColor("#FFBA69"));
//                textView.setBackgroundColor(Color.parseColor("#eeeeee"));
//                textView.setPadding(24,4,24,14);
//                holder.memoItemKeywordPart.addView(textView);
//                TextView margin = new TextView(context);
//                margin.setText("    ");
//                holder.memoItemKeywordPart.addView(margin);
//            }
//        }
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return memoModelList.size();
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
        TextView memoItemName;
        TextView memoItemType;
        TextView memoItemSnippet;
        TextView memoItemDate;
        TextView memoItemKeywords;

        public ViewHolder(View itemView) {
            super(itemView);
            memoItemName = (TextView)itemView.findViewById(R.id.fragment_memo_item_name);
            memoItemType = (TextView)itemView.findViewById(R.id.fragment_memo_item_type);
            memoItemSnippet = (TextView)itemView.findViewById(R.id.fragment_memo_item_snippet);
            memoItemDate = (TextView)itemView.findViewById(R.id.fragment_memo_item_date);
            memoItemKeywords = (TextView)itemView.findViewById(R.id.fragment_memo_item_keywords);
        }
    }
}
