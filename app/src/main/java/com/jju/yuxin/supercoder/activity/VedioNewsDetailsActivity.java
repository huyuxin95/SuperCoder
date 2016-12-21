package com.jju.yuxin.supercoder.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.supercoder.R;
import com.jju.yuxin.supercoder.adapter.VedioList_Adapter;
import com.jju.yuxin.supercoder.bean.VedioInfoBean;
import com.jju.yuxin.supercoder.utils.Constant;
import com.jju.yuxin.supercoder.utils.JsoupUtils;
import com.jju.yuxin.supercoder.view.YListView;
import com.jju.yuxin.supercoder.view.YVideoView;

import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE;
import static android.util.Log.e;


/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName VedioNewsDetailsActivity
 * Created by yuxin.
 * Created time 20-12-2016 23:31.
 * Describe :视频详细内容页面
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class VedioNewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = VedioNewsDetailsActivity.class.getSimpleName();
    private VedioInfoBean vedioInfoBean;

    //加载完成的新闻集合
    private List<VedioInfoBean> vedioinfos;

    //视频新闻爬取地址
    private static final String path = "http://www.jxntv.cn/";

    //当加载数据完毕需要更新界面数据
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case Constant.SUCCESS:
                    vedioinfos = (List<VedioInfoBean>) msg.obj;

                    //将内容填充到ListView
                    lv_more_vedio.setAdapter(new VedioList_Adapter(VedioNewsDetailsActivity.this, vedioinfos));
                    break;
                //视频新闻加载成功
                case Constant.SUCCESS_LOAD_DETAIL:
                    vedioInfoBean = (VedioInfoBean) msg.obj;

                    //设置新闻内容
                    reader_count.setText(vedioInfoBean.getPlay_count());
                    //设置新闻标题
                    collapsing_toolbar.setTitle(vedioInfoBean.getNews_title());

                    playVedio(vedioInfoBean.getPlay_src());
                    break;
                //视频新闻加载失败
                case Constant.ERROR:

                    break;
                default:
                    break;
            }

        }
    };

    private TextView reader_count;
    private YVideoView vedio_paly;
    private ImageView iv_shard;
    private ImageView ivImage;
    private int per_position = 0;
    private YListView lv_more_vedio;
    private ImageView iv_download;
    private DownloadManager downManager;
    private RelativeLayout rl_vv;
    private getPositionBroadcast positionBroadcast;
    private CollapsingToolbarLayout collapsing_toolbar;
    private MediaController mediaController;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        e(TAG, "onCreate" + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_news_details);
        //分享点击监听
        SharedClickListener sharedClickListener = new SharedClickListener();

        downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //视频图片
        ivImage = (ImageView) findViewById(R.id.ivImage);
        //标题
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //分享按钮
        iv_shard = (ImageView) findViewById(R.id.iv_shard);
        //点击事件
        iv_shard.setOnClickListener(sharedClickListener);
        //新闻阅读次数,或者视频播放次数
        reader_count = (TextView) findViewById(R.id.reader_count);
        //视屏播放器
        vedio_paly = (YVideoView) findViewById(R.id.vedio_paly);

        //视屏播放器上面嵌套的布局
        rl_vv = (RelativeLayout) findViewById(R.id.rl_vv);

        //更多视频列表
        lv_more_vedio = (YListView) findViewById(R.id.lv_more_vedio);
        //下载
        iv_download = (ImageView) findViewById(R.id.iv_download);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestscrollview);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //动态注册一个广播用来接收播放位置
        positionBroadcast = new getPositionBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("sendposition");
        registerReceiver(positionBroadcast, filter);

        //下载视频到本地
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadVedio(vedioInfoBean);

            }
        });

        //加载视频
        JsoupUtils.getNewPaper(path, mhandler);

        //<更多视频>列表点击事件
        lv_more_vedio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前点击的item的对象
                VedioInfoBean vedioInfoBean = vedioinfos.get(position);
                //跳转至新闻详情页面
                Intent intent = new Intent(VedioNewsDetailsActivity.this, VedioNewsDetailsActivity.class);
                intent.putExtra("vedio_news", vedioInfoBean);
                startActivity(intent);
                //关闭当前页面
                finish();
            }
        });

        //设置返回监听--返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回上一级
                onBackPressed();
            }
        });

        //当页面发生滚动,将正在现实的播放器的控制器隐藏
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //设置视频控制器
                        vedio_paly.setMediaController(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        vedio_paly.setMediaController(getMediaController(VedioNewsDetailsActivity.this));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


    }

    /**
     * onResume中防止从onpause跳转
     */
    @Override
    protected void onResume() {
        e(TAG, "onResume" + "");
        super.onResume();
        getInfo();

        //根据当前进度是否为零判断之前是否播放过
        if (per_position == 0) {
            //如果没有播放过那么加载视频资源
            if (vedioInfoBean != null) {
                //获取视频详情
                JsoupUtils.getNewsDetails(vedioInfoBean, mhandler);
            }
            //如果已经播放过那么跳转到上次的播放进度之后
        } else {
            if (!vedio_paly.isPlaying()) {
                vedio_paly.seekTo(per_position);
                vedio_paly.start();
            }
        }

        //给最外层设置长按事件
        rl_vv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = vedio_paly.getCurrentPosition();
                Intent fullintent = new Intent(VedioNewsDetailsActivity.this, LandScapeVedio.class);
                fullintent.putExtra("currentPosition", currentPosition);
                fullintent.putExtra("play_src", vedioInfoBean.getPlay_src());
                startActivity(fullintent);
                return false;
            }
        });
    }

    //接收新闻对象
    private void getInfo() {
        //传过来的视频新闻对象
        vedioInfoBean = getIntent().getParcelableExtra("vedio_news");
        x.image().bind(ivImage, vedioInfoBean.getImg_src());

    }

    /**
     * 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        e(TAG, "onPause" + "");
        //当切换到分享界面，需要暂停当前的的视频播放
        //记录下当前播放的进度
        if (vedio_paly.isPlaying()) {
            vedio_paly.pause();
            per_position = vedio_paly.getCurrentPosition();
        }
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vedio_paly != null) {
            vedio_paly.pause();
            vedio_paly.stopPlayback(); //将VideoView所占用的资源释放掉
        }

        unregisterReceiver(positionBroadcast);

        e(TAG, "onDestroy" + "");
    }

    /**
     * 分享方法的实现，需要注意的是，对应每一个平台，他能分享的信息都不尽相同
     * 可以参考  http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/
     */
    private void ShareMessage() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 播放视频的操作
     *
     * @param play_src
     */
    private void playVedio(String play_src) {


        Uri uri = Uri.parse(play_src);

        //设置视频控制器
        vedio_paly.setMediaController(getMediaController(this));

        //播放完成回调
        vedio_paly.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //播放错误监听
        vedio_paly.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                e(TAG, "onError" + "what" + what + "extra" + extra);
                Toast.makeText(VedioNewsDetailsActivity.this, "视频好像出现了一点问题!", Toast.LENGTH_SHORT).show();
                vedio_paly.seekTo(0);
                return true;
            }
        });

        //设置视频路径
        vedio_paly.setVideoURI(uri);

        //准备完成
        vedio_paly.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //开始播放视频
                vedio_paly.start();
            }
        });

    }

    //分享按钮点击事件
    private class SharedClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //分享按钮
                case R.id.iv_shard:
                    ShareMessage();
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 播放器播放完成监听
     */
    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VedioNewsDetailsActivity.this, "播放完成.", Toast.LENGTH_SHORT).show();
            vedio_paly.seekTo(0);
            vedio_paly.start();
        }
    }

    /**
     * 分享方法的实现，需要注意的是，对应每一个平台，他能分享的信息都不尽相同
     * 可以参考  http://wiki.mob.com/%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E/
     */
    private class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

        @Override
        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

            e(TAG, "onShare" + platform.getName());
            //微信分享
            if (Wechat.NAME.equals(platform.getName())) {

                // text是分享文本，所有平台都需要这个字段
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                paramsToShare.setImageUrl(vedioInfoBean.getImg_src() + "");
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                paramsToShare.setUrl(vedioInfoBean.getPlay_src() + "");

                //QQ分享
            } else if (QQ.NAME.equals(platform.getName())) {
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                paramsToShare.setTitle(vedioInfoBean.getNews_title());
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                paramsToShare.setTitleUrl(vedioInfoBean.getPlay_src() + "");
                // text是分享文本，所有平台都需要这个字段
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                paramsToShare.setImageUrl(vedioInfoBean.getImg_src() + "");

                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                paramsToShare.setComment(vedioInfoBean.getPlay_count());
                // site是分享此内容的网站名称，仅在QQ空间使用
                paramsToShare.setSite("CINews");
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                paramsToShare.setSiteUrl(vedioInfoBean.getPlay_src() + "");

                //新浪微博分享
            } else if (SinaWeibo.NAME.equals(platform.getName())) {

                // text是分享文本，所有平台都需要这个字段
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                paramsToShare.setImageUrl(vedioInfoBean.getImg_src() + "");

                //微信朋友圈分享
            } else if (WechatMoments.NAME.equals(platform.getName())) {

                paramsToShare.setTitle(vedioInfoBean.getNews_title());
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                paramsToShare.setUrl(vedioInfoBean.getVideo_src() + "");
                paramsToShare.setImagePath(vedioInfoBean.getImg_src() + "");

                //微信收藏
            } else if (WechatFavorite.NAME.equals(platform.getName())) {
                paramsToShare.setTitle(vedioInfoBean.getNews_title());
                paramsToShare.setText(vedioInfoBean.getNews_info() + "" + vedioInfoBean.getPlay_src());
                paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                paramsToShare.setUrl(vedioInfoBean.getVideo_src() + "");
                paramsToShare.setImagePath(vedioInfoBean.getImg_src() + "");
            }
        }
    }

    /**
     * 下载视频到本地
     *
     * @param play_info
     */
    private void downloadVedio(VedioInfoBean play_info) {
        //判断是否存在SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.DIRECTORY_DOWNLOADS, play_info.getNews_title().replace(" ", "") + ".mp4");
            e(TAG, "downloadVedio" + file.getAbsolutePath() + file.exists());
            if (!file.exists()) {
                Toast.makeText(VedioNewsDetailsActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                //创建一个下载管理器的请求
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(play_info.getPlay_src()));
                request.setNotificationVisibility(VISIBILITY_VISIBLE);//用于设置下载时时候在状态栏显示通知信
                request.allowScanningByMediaScanner();//用于设置是否允许本MediaScanner扫描。
                request.setTitle("下载中");
                request.setDescription("视频正在下载中");
                request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, play_info.getNews_title().replace(" ", "") + ".mp4");
                //加入下载队列
                long enqueue = downManager.enqueue(request);
            } else {
                Toast.makeText(this, "您已经下载过了这个视频...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请插入SD卡...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取播放器的控制器
     *
     * @param context
     * @return
     */
    private MediaController getMediaController(Context context) {
        //单例
        if (mediaController == null) {
            mediaController = new MediaController(context);
        }
        return mediaController;
    }

    /**
     * 获取播放进度,广播接受者
     */
    public class getPositionBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int position = intent.getIntExtra("position", 0);
            e(TAG, "onReceive" + "position" + position);
            //调到指定位置
            vedio_paly.seekTo(position);
            if (position == 0) {
                per_position = 0;
                //播放完成
                vedio_paly.seekTo(vedio_paly.getDuration() - 1);
            } else {
                vedio_paly.start();
            }
        }
    }

}
