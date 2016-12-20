package com.jju.yuxin.supercoder.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jju.yuxin.supercoder.bean.NewslistBean;
import com.jju.yuxin.supercoder.bean.ResponseBean;
import com.jju.yuxin.supercoder.utils.Constant;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Map;

import static android.util.Log.e;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.supercoder.http
 * Created by yuxin.
 * Created time 2016/12/14 0014 下午 3:20.
 * Version   1.0;
 * Describe : Http网络请求
 * History:
 * ==============================================================================
 */

public class HttpPicUtil {

    private static final String TAG = HttpPicUtil.class.getSimpleName();

    /**
     *
     * https://api.desktoppr.co/1/wallpapers?page=1
     * Http网络请求,第一次加载或者刷新数据
     * @param url
     * @param paramsMap
     */
    public static void doGet(final Handler mhandler, String url, final Map<String, String> paramsMap) {
        final RequestParams params = new RequestParams(url  + "/");
        //传入的参数遍历
        for (Map.Entry<String, String> params_entry : paramsMap.entrySet()) {
            params.addQueryStringParameter(params_entry.getKey(), params_entry.getValue());
        }
        //默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        //     params.setCacheMaxAge(1000 * 60);
        params.setCacheMaxAge(1000 * 60);

        Callback.Cancelable cancelable
                // 使用CacheCallback, xUtils将为该请求缓存数据.
                = x.http().get(params, new Callback.CacheCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) {
                // 得到缓存数据, 缓存过期后不会进入这个方法.
                // 如果服务端没有返回过期时间, 参考params.setCacheMaxAge(maxAge)方法.
                //
                // * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
                //   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
                //   逻辑, 那么xUtils将请求新数据, 来覆盖它.
                //
                // * 如果信任该缓存返回 true, 将不再请求网络;
                //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
                //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
                //
                this.result = result;
                return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                }
                mhandler.sendEmptyMessage(Constant.SUCCESS);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;

                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    e(TAG, "onError" + "responseCode:" + responseCode + "responseMsg:" + responseMsg + "errorResult:" + errorResult);
                    // ...
                } else { // 其他错误
                    // ...
                    e(TAG, "onError" + "ex.getMessage():" + ex.getMessage());
                }
                mhandler.sendEmptyMessage(Constant.ERROR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                e(TAG, "onCancelled" + "cancelled!!");
                mhandler.sendEmptyMessage(Constant.CANCELLED);
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    if ("1".equals(paramsMap.get("page"))){
                        // 成功获取数据
                        e(TAG, "onFinished" + "result:" + result);
                        List<ResponseBean> responseBeen = JsonParserUtils.parserPicResponse(result);
                        Log.e(TAG, "onFinished!!!s" + responseBeen.toString());
                        Message msg=Message.obtain();
                        msg.what= Constant.FINISHED;
                        msg.obj=responseBeen;
                        mhandler.sendMessage(msg);
                    }else{
                        e(TAG, "onSucesss" + "result:" + result);
                        List<ResponseBean> responseBeen = JsonParserUtils.parserPicResponse(result);
                        Log.e(TAG, "onFinished!!!" + responseBeen.toString());
                        Message msg=Message.obtain();
                        msg.what= Constant.LOADMORE;
                        msg.arg1= Integer.parseInt(paramsMap.get("page"));
                        msg.obj=responseBeen;
                        mhandler.sendMessage(msg);

                    }

                }
            }
        });
    }
}
