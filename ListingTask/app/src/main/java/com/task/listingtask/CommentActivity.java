package com.task.listingtask;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.task.listingtask.adapter.CommentListAdapter;
import com.task.listingtask.adapter.CommentListAdapter;
import com.task.listingtask.adapter.FoodListDataAdapter;
import com.task.listingtask.databinding.ActivityCommentBinding;
import com.task.listingtask.model.CommentListModel;
import com.task.listingtask.model.CommentListModel;
import com.task.listingtask.model.FoodListModel;
import com.task.listingtask.others.Constants;
import com.task.listingtask.others.DetectConnection;
import com.task.listingtask.retrofit.ApiInterface;
import com.task.listingtask.retrofit.AppUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private boolean mLoading = false;
    private ArrayList<CommentListModel> commentListModels = new ArrayList<>();
    private CommentListAdapter commentListAdapter;
    ActivityCommentBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(ContextCompat.getColor(CommentActivity.this, R.color.white));// set status background white
        }

        init();
        callApiGetComment(getIntent().getStringExtra(Constants.BUNDLE_KEY_COMMENT));
    }

    public void init() {

        Glide.with(this).load(getIntent().getStringExtra(Constants.BUNDLE_KEY_IMAGE)).into(mBinding.ivImageuri);
        mBinding.txtTitle.setText(getIntent().getStringExtra(Constants.BUNDLE_KEY_TITLE));

        commentListAdapter = new CommentListAdapter(CommentActivity.this, commentListModels);
        mBinding.rvComment.setLayoutManager(new LinearLayoutManager(CommentActivity.this, RecyclerView.VERTICAL, false));
        mBinding.rvComment.setAdapter(commentListAdapter);
        commentListAdapter.notifyDataSetChanged();
    }

    public void isLoading(boolean isloading) {
        mLoading = isloading;
        supportInvalidateOptionsMenu();
    }

    private void callApiGetComment(String permalink) {
        if (DetectConnection.checkInternetConnection(this)) {
            isLoading(true);
            ApiInterface apiService = AppUrl.getClient().create(ApiInterface.class);
            try {
                Log.e("url", "" + permalink);
                Call<JsonArray> call = apiService.callApiGetComment("https://www.reddit.com" + permalink + ".json");
                call.enqueue(new retrofit2.Callback<JsonArray>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonArray> call, retrofit2.Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.code() == 200) {
                                    String strResponse = response.body().toString();
                                    JSONArray jsonArray = new JSONArray(strResponse);
                                    Log.e("print-0", "onResponse: " + jsonArray.length());
                                    for (int i1 = 1; i1 < jsonArray.length(); i1++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i1);
                                        Log.e("print-1", "onResponse: " + jsonObject);
                                        if (jsonObject != null) {
                                            JSONObject data = jsonObject.getJSONObject("data");

                                            if (data != null) {
                                                if (data.has("children")) {
                                                    JSONArray jsonArray1 = data.getJSONArray("children");

                                                    setArrayListData(jsonArray1);
                                                    Log.e("print-2", "onResponse: " + jsonArray1.length());

                                                }
                                            } else {
                                                isLoading(false);
                                            }
                                        }
                                    }
                                } else {
                                    isLoading(false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                isLoading(false);
                            }
                        } else {
                            isLoading(false);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<JsonArray> call, Throwable t) {
                        Log.e("Error>>", "" + call.toString());
                        isLoading(false);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                isLoading(false);
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isLoading(false);
                    Constants.showSnackBar(mBinding.llMain, getResources().getString(R.string.msg_internet_connection_error), false);
                }
            });
        }
    }

    private void setArrayListData(JSONArray jsonArray1) {
        try {
            for (int i12 = 0; i12 < jsonArray1.length(); i12++) {
                if (jsonArray1 != null) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray1.get(i12);
                    if (jsonObject1 != null) {
                        JSONObject data1 = jsonObject1.getJSONObject("data");
                        if (data1 != null) {

                            CommentListModel commentListModel = new CommentListModel();
                            commentListModel.setComment(data1.getString("body"));
                            commentListModel.setTitle(data1.getString("author"));
                            commentListModels.add(commentListModel);
                            commentListAdapter.notifyDataSetChanged();

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}