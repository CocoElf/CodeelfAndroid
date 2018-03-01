package cocoelf.codeelfandroid.adapter;

import android.graphics.Color;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.json.MemoModel;

/**
 * Created by shea on 2018/3/1.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> implements View.OnClickListener {
    private List<MemoModel> memoModelList;
    private MemoAdapter.OnItemClickListener mOnItemClickListener = null;

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

    @Override
    public void onBindViewHolder(MemoAdapter.ViewHolder holder, int position) {
        MemoModel memoModel = memoModelList.get(position);
        holder.memoItemName.setText(memoModel.getName());
        String dateAndSnippet = memoModel.getFormatDate()+"  "+memoModel.getSnippet();
        SpannableString spannableString = new SpannableString(dateAndSnippet);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#1AA8F2")),0,memoModel.getFormatDate().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.memoItemDateAndSnippet.setText(spannableString);
//        holder.memoItemDate.setText(new SimpleDateFormat("yyyy-MM-ss").format(memoModel.getDate()));
        holder.memoItemKeyword.setText(memoModel.getKeyWord());
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
        TextView memoItemDateAndSnippet;
//        TextView memoItemDate;
        TextView memoItemKeyword;

        public ViewHolder(View itemView) {
            super(itemView);
            memoItemName = (TextView)itemView.findViewById(R.id.fragment_memo_item_name);
            memoItemDateAndSnippet = (TextView)itemView.findViewById(R.id.fragment_memo_item_date_and_snippet);
//            memoItemDate = (TextView)itemView.findViewById(R.id.fragment_memo_item_date);
            memoItemKeyword = (TextView)itemView.findViewById(R.id.fragment_memo_item_keyword);
        }
    }
}
