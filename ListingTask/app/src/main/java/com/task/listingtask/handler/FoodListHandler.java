package com.task.listingtask.handler;

import android.content.Intent;
import android.view.View;

import com.task.listingtask.CommentActivity;
import com.task.listingtask.MainActivity;
import com.task.listingtask.R;
import com.task.listingtask.model.CommentListModel;
import com.task.listingtask.others.Constants;

public class FoodListHandler {

    public void onClickFoodList(View view, String permaLink, String title, String imageUri) {
        Intent intent = new Intent(view.getContext(), CommentActivity.class);
        intent.putExtra(Constants.BUNDLE_KEY_COMMENT, permaLink);
        intent.putExtra(Constants.BUNDLE_KEY_TITLE, title);
        intent.putExtra(Constants.BUNDLE_KEY_IMAGE, imageUri);
        view.getContext().startActivity(intent);
    }
}
