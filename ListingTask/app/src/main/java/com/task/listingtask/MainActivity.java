package com.task.listingtask;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.task.listingtask.adapter.FoodListDataAdapter;
import com.task.listingtask.databinding.ActivityMainBinding;
import com.task.listingtask.model.FoodListModel;
import com.task.listingtask.others.Constants;
import com.task.listingtask.others.DetectConnection;
import com.task.listingtask.others.PaginationScrollListener;
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

public class MainActivity extends AppCompatActivity {

    private FoodListDataAdapter paginationAdapter;
    private ApiInterface apiService;

    private static final int PAGE_START = 1;
    private boolean isRefreshing = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    ActivityMainBinding mBinding;
    private boolean mLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.white));// set status background white
        }

        apiService = AppUrl.getClient().create(ApiInterface.class);
        init();

    }

    public void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        paginationAdapter = new FoodListDataAdapter(this);
        mBinding.rvFood.setLayoutManager(linearLayoutManager);
        mBinding.rvFood.setAdapter(paginationAdapter);

        mBinding.rvFood.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isRefreshing = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isRefreshing;
            }
        });

        loadFirstPage();
    }

    public void isLoading(boolean isloading) {
        mLoading = isloading;
        supportInvalidateOptionsMenu();
    }

    private void loadDatafromAPI(String Response) {

        try {
            JSONObject jsonObject = new JSONObject(Response);

            if (jsonObject != null) {
                JSONObject data = jsonObject.getJSONObject("data");

                if (data != null) {
                    if (data.has("children")) {
                        JSONArray jsonArray = data.getJSONArray("children");
                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                            if (jsonArray != null) {
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i1);
                                if (jsonObject1 != null) {
                                    JSONObject data1 = jsonObject1.getJSONObject("data");

                                    if (data1 != null) {
                                        List<FoodListModel> foodListModelArrayList = new ArrayList<>();
                                        FoodListModel foodListModel = new FoodListModel();
                                        foodListModel.setImage(data1.getString("thumbnail"));
                                        foodListModel.setTitle(data1.getString("title"));
                                        foodListModel.setPermalink(data1.getString("permalink"));
                                        foodListModelArrayList.add(foodListModel);
                                        paginationAdapter.addAll(foodListModelArrayList);

                                    }
                                }
                            }
                        }
                    }
                } else {
                    isLoading(false);
                }
            } else {
                isLoading(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            isLoading(false);
        }

    }

    private void loadNextPage() {
        if (DetectConnection.checkInternetConnection(this)) {
            isLoading(true);
            apiService.callApiGetFood().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.isSuccessful() && response != null) {
                        paginationAdapter.removeLoadingFooter();
                        isRefreshing = false;

                        loadDatafromAPI(response.body().toString());

                        if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });
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

    private void loadFirstPage() {

        if (DetectConnection.checkInternetConnection(this)) {
            isLoading(true);
            apiService.callApiGetFood().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.isSuccessful() && response != null) {

                        mBinding.progressbar.setVisibility(View.GONE);

                        loadDatafromAPI(response.body().toString());

                        if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
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
}
