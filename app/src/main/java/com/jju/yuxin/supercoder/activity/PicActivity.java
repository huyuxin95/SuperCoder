package com.jju.yuxin.supercoder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.CatePicAdapter;
import com.jju.yuxin.supercoder.bean.ResponseBean;
import com.jju.yuxin.supercoder.http.HttpPicUtil;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.view.YRecycleview;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE;
import static android.util.Log.i;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname viewpager.lingdian.com.viewpagerdouble.activity
 * Created by yuxin.
 * Created time 2016/12/8 0008 下午 9:40.
 * Version   1.0;
 * Describe :壁纸activity
 * History:
 * ==============================================================================
 */
public class PicActivity extends Activity {

    private static final String TAG = PicActivity.class.getSimpleName();

    private CatePicAdapter picAdapter;

    private YRecycleview lv_pic;

    private Constant constant = new Constant();

    private DownloadManager downManager;

    //当数据获取完毕设置适配器
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case Constant.FINISHED:
                    //第一次加载或者刷新数据
                    List<ResponseBean> responseBeen = (List<ResponseBean>) msg.obj;
                    //设置适配器
                    //并设置广告出现在第三个位置
                    picAdapter.onReference(responseBeen, constant.getThridAdlist());
                    break;
                //加载更多
                case Constant.LOADMORE:
                    //加载的数据
                    List<ResponseBean> addlistBean = (List<ResponseBean>) msg.obj;
                    //加载的第几页
                    int page = msg.arg1;
                    i(TAG, "HM" + "page:" + page);
                    //初始化一个集合用来放置即将加入的广告位置
                    List<Integer> adlists = new ArrayList<>();

                    //根据广告位置获取到这个页面要插入的广告位置
                    for (int adpisition : constant.getThridAdlist()) {
                        //在原来的基础上增加
                        adlists.add(((page - 1) * (Constant.DEFAULT_COUNT * 2) + adpisition));
                        i(TAG, "HM" + "position" + ((page - 1) * Constant.DEFAULT_COUNT + adpisition));
                    }
                    //设置适配器
                    picAdapter.addOnReference(addlistBean, adlists);

                    break;
                //加载失败
                case Constant.ERROR:

                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        lv_pic = (YRecycleview) findViewById(R.id.yrecycle_view_pic);

        //设置为瀑布流视图
        lv_pic.setLayoutManager(new StaggeredGridLayoutManager(2
                , StaggeredGridLayoutManager.VERTICAL));

        lv_pic.setItemAnimator(new DefaultItemAnimator());

        //获取下载管理器
        downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        //内容的适配器
        picAdapter = new CatePicAdapter(this);
        //设置适配器
        lv_pic.setAdapter(picAdapter);

        //获取即将传入的参数
        HashMap<String, String> pic_params = new HashMap<>();
        pic_params.put("page", "1");
        //请求数据
        HttpPicUtil.doGet(mhandler, Constant.PIC_URL, pic_params);

        //Item点击事件
        picAdapter.setOnItemClickLitener(new CatePicAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(final ResponseBean responseBean, int position) {

                //显示一个AlertDialog来显示图片
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PicActivity.this);
                View inflate = LayoutInflater.from(PicActivity.this).inflate(R.layout.layout_alertdialog, null);
                //图片
                ImageView iv_pic_detail = (ImageView) inflate.findViewById(R.id.iv_pic_detail);
                //设置壁纸
                TextView tv_set_wallpaper = (TextView) inflate.findViewById(R.id.tv_set_wallpaper);
                //下载壁纸
                TextView tv_download = (TextView) inflate.findViewById(R.id.tv_download);
                //包含设置壁纸和下载是容器
                final LinearLayout ll_pic_setting = (LinearLayout) inflate.findViewById(R.id.ll_pic_setting);
                //默认消失
                ll_pic_setting.setVisibility(View.GONE);

                i(TAG, "onItemClick" + responseBean.getImage().getUrl());

                //即将加载的图片的配置信息
                ImageOptions option = new ImageOptions.Builder()
                        .setUseMemCache(true)  //是否缓存
                        .setImageScaleType(ImageView.ScaleType.FIT_CENTER) //填充方式
                        .setConfig(Bitmap.Config.ARGB_8888)  //加载高清图片
                        .setLoadingDrawableId(R.mipmap.image_loading)  //正在加载的等待图片
                        .setFailureDrawableId(R.mipmap.image_failed)   //记载失败的的图片
                        .build();
                //获取图片
                x.image().bind(iv_pic_detail, responseBean.getImage().getUrl(), option);
                //AlertDialog设置自定义视图
                alertDialogBuilder.setView(inflate);
                //显示AlertDialog
                alertDialogBuilder.show();

                //当壁纸被点击,则显示包含下载和设置鼻子的容器
                iv_pic_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //显示
                        ll_pic_setting.setVisibility(View.VISIBLE);
                    }
                });
                //设置壁纸点击监听
                tv_set_wallpaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i(TAG, "onClick" + "设置壁纸");
                        setWallpapers(responseBean);
                    }
                });
                //现在壁纸点击监听
                tv_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i(TAG, "onClick" + "下载");
                        downloadVedio(responseBean);
                    }
                });

            }
        });

        //recyleView的下拉刷新和加载更多的监听
        lv_pic.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                i(TAG, "onRefresh" + "下拉刷新");
                //设置刷新完成
                //重新请求第一页数据
                HashMap<String, String> pic_params = new HashMap<>();
                pic_params.put("page", "1");
                HttpPicUtil.doGet(mhandler, Constant.PIC_URL, pic_params);
            }

            @Override
            public void onLoadMore() {
                i(TAG, "onLoadMore" + "加载更多");
                //加载更多
                lv_pic.setloadMoreComplete();
                //获取到页数
                int page = (picAdapter.getItemCount()) / 20 + 1;

                i(TAG, "onLoadMore" + "page:" + page);
                //获取默认参数设置
                HashMap<String, String> pic_params = new HashMap<>();
                pic_params.put("page", page + "");
                //请求数据
                HttpPicUtil.doGet(mhandler, Constant.PIC_URL, pic_params);
            }
        });
    }

    /**
     * 设置壁纸.设置壁纸的前提是先要把壁纸下载下来
     * @param responseBean
     */
    private void setWallpapers(ResponseBean responseBean) {
        String path = "/Download/";
        //请求参数
        RequestParams requestParams = new RequestParams(responseBean.getImage().getUrl());
        i(TAG, "setWallpapers" + responseBean.getImage().getUrl());
        i(TAG, "setWallpapers" + Environment.getExternalStorageDirectory() + path + responseBean.getId() + ".jpg");
        //下载保存的目录
        requestParams.setSaveFilePath(Environment.getExternalStorageDirectory() + path + responseBean.getId() + ".jpg");
        //请求回调
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
            @Override
            public void onSuccess(File file) {
                //壁纸下载完成
                try {
                    InputStream inputstream = new FileInputStream(file);
                    //设置壁纸
                    setWallpaper(inputstream);
                    Toast.makeText(PicActivity.this, "壁纸设置成功!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e, boolean isOnCallback) {
                e.printStackTrace();
                Toast.makeText(PicActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });


    }

    /**
     * 下载图片到本地
     * @param responseBean
     */
    private void downloadVedio(ResponseBean responseBean) {
        //判断是否存在SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存的文件地址
            String path = "/Download/";
            File file = new File(path, responseBean.getId() + ".jpg");
            i(TAG, "downloadPic" + file.getAbsolutePath() + file.exists());
            //判断文件是否已经存在
            if (!file.exists()) {
                Toast.makeText(PicActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                //开始下载壁纸,用自带的下载器下载
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(responseBean.getImage().getUrl()));
                request.setNotificationVisibility(VISIBILITY_VISIBLE);//用于设置下载时时候在状态栏显示通知信
                request.allowScanningByMediaScanner();//用于设置是否允许本MediaScanner扫描。
                request.setTitle(responseBean.getId() + ".jpg");
                request.setDescription("壁纸正在下载中");
                request.setDestinationInExternalFilesDir(this, path, responseBean.getId() + ".jpg");
                long enqueue = downManager.enqueue(request);
            } else {
                Toast.makeText(this, "您已经有了这张壁纸...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请插入SD卡...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当按下返回键
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            //调用双击退出函数
            exitBy2Click();
            //当点击的是返回按键,那么返回true,拦截事件的传递
            return true;
        }
        //拦截按键事件,
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);
            // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }

}
