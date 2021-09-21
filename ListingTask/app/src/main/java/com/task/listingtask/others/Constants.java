package com.task.listingtask.others;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Constants {


    public static final String PARAM_DATA = "data";
    public static final String BUNDLE_KEY_COMMENT = "comment";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_IMAGE = "image";

    public static void showSnackBar(View parentView, String stringResource, boolean setAction) {
        if (setAction) {
            final Snackbar snackbar = Snackbar.make(parentView
                    , stringResource, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            final Snackbar snackbar = Snackbar.make(parentView
                    , stringResource, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


}
