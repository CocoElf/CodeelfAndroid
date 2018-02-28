package cocoelf.codeelfandroid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cocoelf.codeelfandroid.R;
import cocoelf.codeelfandroid.json.SearchResultModel;

/**
 * Created by shea on 2018/2/27.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> implements View.OnClickListener {
    private List<SearchResultModel> searchResultModelList;
    private OnItemClickListener mOnItemClickListener = null;

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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResultModel searchResultModel = searchResultModelList.get(position);
        holder.searchResultItemName.setText(searchResultModel.getName());
        holder.searchResultItemSnippet.setText(searchResultModel.getSnippet());
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
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
        TextView searchResultItemSnippet;

        public ViewHolder(View itemView) {
            super(itemView);
            searchResultItemName = (TextView)itemView.findViewById(R.id.fragment_search_result_item_name);
            searchResultItemSnippet = (TextView)itemView.findViewById(R.id.fragment_search_result_item_snippet);
        }
    }

}
