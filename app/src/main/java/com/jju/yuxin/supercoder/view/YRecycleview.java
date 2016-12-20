package com.jju.yuxin.supercoder.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.i;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName YRecycleview
 * Created by yuxin.
 * Created time 12-12-2016 14:12.
 * Describe : 这是一个带下拉刷新和上拉加载的自定义的recycleview
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class YRecycleview extends RecyclerView {

    private static final String TAG = YRecycleview.class.getSimpleName();
    //加载更多是否开启
    private boolean loadMoreEnabled = true;

    //下拉刷新是否开启
    private boolean pullRefreshEnabled = true;

    //下拉刷新的头布局集合
    private List<View> mHeaderViews = new ArrayList<>();

    //上拉加载的脚布局集合
    private ArrayList<View> mFootViews = new ArrayList<>();

    //当前的头布局
    private YRecycleviewRefreshHeadView mHeadView;

    //刷新和加载更多的监听器
    private OnRefreshAndLoadMoreListener refreshAndLoadMoreListener;

    //是否正在加载数据
    private boolean isLoadingData = false;

    //当adapter没有数据时,显示这个视图
    private View mEmptyView;

    //没有数据的状态
    private boolean isNoMore = false;

    //下拉的阻率,值越大越难下拉
    private static final float DRAG_RATE = 3;

    //数据观察者
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    //最后的y坐标
    private float mLastY = -1;

    private boolean onTop;
    private WrapAdapter mWrapAdapter;
    //头部刷新的视图
    private static final int TYPE_REFRESH_HEADER = -5;
    //正常视图
    private static final int TYPE_NORMAL = 0;
    //底部刷新的视图
    private static final int TYPE_FOOTER = -3;

    private static List<Integer> sHeaderTypes = new ArrayList<>();
    private static final int HEADER_INIT_INDEX = 10000;

    //构造函数
    public YRecycleview(Context context) {
        this(context, null);
    }

    public YRecycleview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //在构造中初始化
        init();
    }

    /**
     * 这是一个初始化的方法
     */
    private void init() {
        //判断是否可以下拉刷新
        if (pullRefreshEnabled) {
            //获取头部的布局
            YRecycleviewRefreshHeadView refreshHeader = new YRecycleviewRefreshHeadView(getContext());
            //在顶部加入刷新布局
            mHeaderViews.add(0, refreshHeader);
            mHeadView = refreshHeader;
        }
        YRecycleviewRefreshFootView footView = new YRecycleviewRefreshFootView(getContext());
        addFootView(footView);

        //设置底栏隐藏
        mFootViews.get(0).setVisibility(GONE);
    }

    /**
     * 当滚动状态改变的时候调用
     *
     * @param state 滚动状态, {@link #SCROLL_STATE_IDLE},
     *              {@link #SCROLL_STATE_DRAGGING} {@link #SCROLL_STATE_SETTLING}
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && refreshAndLoadMoreListener != null && !isLoadingData && loadMoreEnabled) {
            //获取布局管理器
            LayoutManager layoutManager = getLayoutManager();
            //当页面切换时最后看到的位置
            int lastVisibleItemPosition;

            //如果是GridLayoutManager
            if (layoutManager instanceof GridLayoutManager) {
                //获取到布局的最后位置
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();

                //如果是StaggeredGridLayoutManager
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //返回跨越的行数
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];

                //返回最后的位置
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);

                lastVisibleItemPosition = findMax(into);
            } else {

                //获取到布局的最后位置
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            //判断是否需要添加底部刷新
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore && mHeadView.getStatus() < YRecycleviewRefreshHeadView.STATE_REFRESHING) {

                //获取底部视图
                View footView = mFootViews.get(0);
                //正在加载数据
                isLoadingData = true;
                if (footView instanceof YRecycleviewRefreshFootView) {
                    //将状态改为加载状态
                    ((YRecycleviewRefreshFootView) footView).setState(YRecycleviewRefreshFootView.STATE_LOADING);
                } else {
                    footView.setVisibility(View.VISIBLE);
                }

                //执行加载更多
                refreshAndLoadMoreListener.onLoadMore();
            }
        }
    }


    /**
     * 控件的触摸事件的监听
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mLastY == -1) {
            //获取屏幕Y轴坐标
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取按下的位置
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取移动的偏移量
                final float deltaY = e.getRawY() - mLastY;
                //更新Y轴位置
                mLastY = e.getRawY();

                if (isOnTop() && pullRefreshEnabled) {
                    //设置mHeadView向下移动的距离
                    //DRAG_RATE是阻尼大小值越大mHeadView位移越小感觉越难拉动
                    mHeadView.onMove(deltaY / DRAG_RATE);

                    if (mHeadView.getVisibleHeight() > 0 && mHeadView.getStatus() < mHeadView.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                //复位
                mLastY = -1; //重置
                if (isOnTop() && pullRefreshEnabled) {
                    if (mHeadView.releaseAction()) {
                        if (refreshAndLoadMoreListener != null) {
                            //刷新数据
                            refreshAndLoadMoreListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }


    /**
     * 设置适配器
     */
    @Override
    public void setAdapter(Adapter adapter) {

        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        //为适配器注册数据变化的观察者
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    /**
     * 适配器的包装类
     * 将recycle内容的adapter和具有头部和底部的adapter进行包装
     */
    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;
        private int mCurrentPosition;

        //头部位置
        private int headerPosition = 1;

        //构造方法
        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        /**
         * 根据位置获取View的类型
         *
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            //是否位是头部
            if (isRefreshHeader(position)) {
                i(TAG, "isRefreshHeader" + "position:" + position);
                return TYPE_REFRESH_HEADER;
            }


            if (isHeader(position)) {
                position = position - 1;
                i(TAG, "isHeader" + "position:" + position);
                return sHeaderTypes.get(position);
            }

            //是否底栏
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            int objposition = position - getHeadersCount();
            int adapterCount;

            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (objposition < adapterCount) {
                    //返回adapter的视图类型
                    return adapter.getItemViewType(objposition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {

                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                //设置adapter中每一项的跨度大小
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        //判断当前位置是否是头部或者底栏,如果是则返回gridManager.getSpanCount(),否返回1
                        return (isHeader(position) || isFooter(position))
                                ? gridManager.getSpanCount() : 1;

                    }
                });
            }
        }

        //视图添加到窗口
        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            //获取item的LayoutParams
            ViewGroup.LayoutParams ly_params = holder.itemView.getLayoutParams();
            if (ly_params != null
                    && ly_params instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams sglm_params = (StaggeredGridLayoutManager.LayoutParams) ly_params;
                //设置填充属于控件的所有空间
                sglm_params.setFullSpan(true);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //如果是顶部刷新视图
            if (viewType == TYPE_REFRESH_HEADER) {
                mCurrentPosition++;
                return new SimpleViewHolder(mHeaderViews.get(0));

            } else if (isContentHeader(mCurrentPosition)) {
                if (viewType == sHeaderTypes.get(mCurrentPosition - 1)) {
                    mCurrentPosition++;
                    return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
                }
                //如果是底部视图
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootViews.get(0));
            }
            //内容视图
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //如果是头部
            if (isHeader(position)) {
                return;
            }
            //内容位置
            int objposition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (objposition < adapterCount) {
                    //viewholder和位置相绑定
                    adapter.onBindViewHolder(holder, objposition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                //加头部,加底部,加内容
                return getHeadersCount() + getFootersCount() + adapter.getItemCount();
            } else {
                //头部加底部
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public long getItemId(int position) {
            //如果内容不为空
            if (adapter != null && position >= getHeadersCount()) {
                int objposition = position - getHeadersCount();
                int adapterCount = adapter.getItemCount();
                if (objposition < adapterCount) {
                    //返回被包装的adapter 的id
                    return adapter.getItemId(objposition);
                }
            }
            return -1;
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                //注册内容观察者
                adapter.registerAdapterDataObserver(observer);
            }
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                //取消内容观察者
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }

        //是否是头部内容区域
        public boolean isContentHeader(int position) {
            return position >= 1 && position < mHeaderViews.size();
        }

        //是否位于底部
        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        //判断头部刷新内容区域
        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        //是否头部刷新区域
        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        //获取头部刷新区域大小
        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        //获取底部刷新区域大小
        public int getFootersCount() {
            return mFootViews.size();
        }


    }

    /**
     * 最大值
     */
    private int findMax(int[] into) {
        int max = into[0];
        for (int value : into) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 是否是顶部
     * 就是判断是否已经设置mHeaderViews和mHeaderViews的第一个View是否已经添加在布局中(可见)
     *
     * @return
     */
    public boolean isOnTop() {

        return !(mHeaderViews == null || mHeaderViews.isEmpty()) && mHeaderViews.get(0).getParent() != null;
    }

    /**
     * 这是一个数据观察者
     */
    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (pullRefreshEnabled) {
                    emptyCount++;
                }
                if (loadMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    YRecycleview.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    YRecycleview.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }

    }

    /**
     * 设置没有更多数据了
     *
     * @param noMore
     */
    public void setNoMoreData(boolean noMore) {
        this.isNoMore = noMore;
        View footView = mFootViews.get(0);
        ((YRecycleviewRefreshFootView) footView).setState(isNoMore ? YRecycleviewRefreshFootView.STATE_NOMORE : YRecycleviewRefreshFootView.STATE_COMPLETE);
    }

    /**
     * 获取一个空布局
     *
     * @return
     */
    public View getmEmptyView() {
        return mEmptyView;
    }

    /**
     * 设置空布局
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        //数据改变
        mDataObserver.onChanged();
    }

    /**
     * 还原所有的状态
     */
    public void reSetStatus() {
        setloadMoreComplete();
        refreshComplete();
    }

    /**
     * 设置刷新完成
     */
    private void refreshComplete() {
        mHeadView.refreshComplete();
    }

    /**
     * 设置加载更多完成
     */
    public void setloadMoreComplete() {
        //设置加载数据为false
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (footView instanceof YRecycleviewRefreshFootView) {
            ((YRecycleviewRefreshFootView) footView).setState(YRecycleviewRefreshFootView.STATE_COMPLETE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置刷新和加载更多的监听器
     *
     * @param refreshAndLoadMoreListener 监听器
     */
    public void setRefreshAndLoadMoreListener(OnRefreshAndLoadMoreListener refreshAndLoadMoreListener) {
        this.refreshAndLoadMoreListener = refreshAndLoadMoreListener;
    }

    /**
     * 刷新和加载更多的监听器
     */
    public interface OnRefreshAndLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 设置是否启用下拉刷新功能
     *
     * @param isEnabled
     */
    public void setReFreshEnabled(boolean isEnabled) {
        pullRefreshEnabled = isEnabled;
    }

    /**
     * 设置刷新完成
     */
    public void setReFreshComplete() {
        mHeadView.refreshComplete();
    }

    /**
     * 设置是否启用上拉加载功能
     *
     * @param isEnabled
     */
    public void setLoadMoreEnabled(boolean isEnabled) {
        loadMoreEnabled = isEnabled;
        //如果不启用加载更多功能,就隐藏脚布局
        if (!isEnabled && mFootViews.size() > 0) {
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    /**
     * 设置是否刷新ing状态
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && refreshAndLoadMoreListener != null) {
            mHeadView.setState(YRecycleviewRefreshHeadView.STATE_REFRESHING);
            mHeadView.onMove(mHeadView.getMeasuredHeight());
            refreshAndLoadMoreListener.onRefresh();
        }
    }

    /**
     * 添加头布局
     *
     * @param headView 刷新头布局
     */
    public void addHeadView(View headView) {
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof YRecycleviewRefreshHeadView)) {
            YRecycleviewRefreshHeadView refreshHeader = new YRecycleviewRefreshHeadView(getContext());
            mHeaderViews.add(0, refreshHeader);
            mHeadView = refreshHeader;
        }
        mHeaderViews.add(headView);
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
    }

    /**
     * 添加底部布局
     * @param footView
     */
    public void addFootView(YRecycleviewRefreshFootView footView) {
        mFootViews.clear();
        mFootViews.add(footView);
    }

}
