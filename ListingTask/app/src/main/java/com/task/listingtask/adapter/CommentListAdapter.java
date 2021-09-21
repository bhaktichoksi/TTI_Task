package com.task.listingtask.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.task.listingtask.R;
import com.task.listingtask.databinding.ItemCommentListBinding;
import com.task.listingtask.databinding.ItemFoodListBinding;
import com.task.listingtask.handler.FoodListHandler;
import com.task.listingtask.model.CommentListModel;
import com.task.listingtask.model.FoodListModel;
import com.task.listingtask.others.Constants;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> {

    ArrayList<CommentListModel> commentListModels;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentListBinding mBinding;

        public MyViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public CommentListAdapter(Context activity, ArrayList<CommentListModel> CommentListModels) {
        context = activity;
        commentListModels = CommentListModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_comment_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CommentListModel commentListModel = commentListModels.get(position);
        holder.mBinding.setCommentListModel(commentListModel);
        holder.mBinding.executePendingBindings();

    }


    @Override
    public int getItemCount() {
        return commentListModels.size();
    }
}
