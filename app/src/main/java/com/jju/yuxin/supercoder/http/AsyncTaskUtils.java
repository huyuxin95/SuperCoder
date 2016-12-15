package com.jju.yuxin.supercoder.http;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.http
 * Created by yuxin.
 * Created time 2016/12/14 0014 下午 10:03.
 * Version   1.0;
 * Describe :
 * History:
 * ==============================================================================
 */

public class AsyncTaskUtils extends AsyncTask<String, Void, Drawable> {

    private ImageView mImageView;


    public AsyncTaskUtils(ImageView mImageView){

        this.mImageView = mImageView;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            if (conn.getResponseCode() == 200) {
                Drawable mDrawable = Drawable.createFromStream(conn.getInputStream(), null);
                return mDrawable;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Drawable result) {
        mImageView.setImageDrawable(result);
    }

}

