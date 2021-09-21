package com.task.listingtask.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.task.listingtask.CommentActivity;
import com.task.listingtask.R;
import com.task.listingtask.databinding.ItemFoodListBinding;
import com.task.listingtask.databinding.ItemLoadingBinding;
import com.task.listingtask.handler.FoodListHandler;
import com.task.listingtask.model.FoodListModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FoodListDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<FoodListModel> foodListArrayList;
    Context context;

    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public FoodListDataAdapter(Context context) {
        this.context = context;
        foodListArrayList = new LinkedList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_food_list, parent, false);
                viewHolder = new FoodViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        FoodListModel food = foodListArrayList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                FoodViewHolder foodViewHolder = (FoodViewHolder) holder;

                foodViewHolder.mBinding.setFoodListModel(food);
                foodViewHolder.mBinding.setHandler(new FoodListHandler());
                foodViewHolder.mBinding.executePendingBindings();

                Glide.with(context).load(food.getImage()).apply(RequestOptions.centerCropTransform()).into(foodViewHolder.mBinding.ivImage);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.mBinding.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void add(FoodListModel food) {
        foodListArrayList.add(food);
        notifyItemInserted(foodListArrayList.size() - 1);
    }

    public void addAll(List<FoodListModel> foodResults) {
        for (FoodListModel result : foodResults) {
            add(result);
        }
    }

    public FoodListModel getItem(int position) {
        return foodListArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return foodListArrayList == null ? 0 : foodListArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == foodListArrayList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new FoodListModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = foodListArrayList.size() - 1;
        FoodListModel result = getItem(position);

        if (result != null) {
            foodListArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private ItemFoodListBinding mBinding;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ItemLoadingBinding mBinding;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

        }
    }
}
