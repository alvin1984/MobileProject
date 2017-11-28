package com.mobile.base.widget.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mobile.base.R;

/**
 * Created by alvinzhang on 2017/7/24.
 */

public class FreshRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

    private final int HEADER_CONTAINER = 1;
    private final int FOOTER_CONTAINER = 2;
    private RecyclerViewType type = RecyclerViewType.Linear;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private WrapAdapter wrapAdapter;
    private ViewGroup headerContainer;
    private ViewGroup footerContainer;
    private View loadMoreView;
    private OnLoadListener onLoadListener;
    private boolean isLoading;

    public FreshRecyclerView(@NonNull Context context) {
        super(context, null);
    }

    public FreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.base_library_fresh_recycler_view, this, true);
        swipeRefreshLayout = findViewById(R.id.base_lib_swipe_refresh_layout);
        recyclerView = findViewById(R.id.base_lib_recycler_view);
        if (type == RecyclerViewType.Linear) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        } else if (type == RecyclerViewType.Grid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        headerContainer = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.base_library_fresh_recycler_view_header_container, this, false);
        footerContainer = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.base_library_fresh_recycler_view_footer_container, this, false);
        loadMoreView = LayoutInflater.from(getContext()).inflate(R.layout.base_library_fresh_recycler_view_footer, this, false);
        loadMoreView.setVisibility(GONE);
        footerContainer.addView(loadMoreView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItem = 0;
                int firstVisibleItem = 0;
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                }

                if (!isDownDrag && !isLoading && lastVisibleItem == wrapAdapter.getItemCount() - 1) {
                    //判断是否一屏就已经显示完所有的数据，如果是，则隐藏自动加载更多布局
                    if (firstVisibleItem == 0 && lastVisibleItem - firstVisibleItem <= wrapAdapter.getItemCount() - 1) {
                        Log.d("recyclerView","======onScrolled");
                        loadMoreView.setVisibility(GONE);
                    } else {
                        if (isLastPage){
                            loadMoreView.setVisibility(GONE);
                        }else{
                            loadMoreData();
                        }

                    }

                }
            }

        });

    }

    /**
     * 上一次划动的位置
     */
    private float last;

    /**
     * 是否是向下滑动，控制OnScrollListener监听时是否调用加载更多
     */
    private boolean isDownDrag;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float cur = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last = cur;
                break;
            case MotionEvent.ACTION_MOVE:
                if (cur - last < 0) {
                    if (!isLoading && !loadMoreView.isShown() && !swipeRefreshLayout.isRefreshing()) {
                        if (wrapAdapter.getItemCount() > 2) {
                            loadMoreView.setVisibility(VISIBLE);
                        }
                    }
                    isDownDrag = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isDownDrag = false;
                last = 0;
                if (loadMoreView.isShown()) {
                    loadMoreData();
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    private void loadMoreData() {
        if (onLoadListener != null) {
            isLoading = true;
            onLoadListener.onLoadMore();
            loadMoreView.setVisibility(VISIBLE);
            swipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 标记是否是最后一页，如果是最后一页，则关闭自动加载更多，需要手动上拉去加载
     */
    private boolean isLastPage;

    public void setManualLoad(boolean manualLoad) {
        isLastPage = manualLoad;
    }

    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        wrapAdapter = new WrapAdapter(adapter);
        recyclerView.setAdapter(wrapAdapter);
    }

    public void addHeaderView(View header) {
        headerContainer.addView(header);
    }

    public void addFooterView(View footer) {
        footerContainer.addView(footer);
    }

    public void removeHeader(View header) {
        headerContainer.removeView(header);
    }

    public void removeFooter(View footer) {
        footerContainer.removeView(footer);
    }

    private void setType(RecyclerViewType type) {
        this.type = type;
        if (type == RecyclerViewType.Linear) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        } else if (type == RecyclerViewType.Grid) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    public void setOnLoadListener(OnLoadListener listener) {
        onLoadListener = listener;
    }

    public void onComplete() {
        swipeRefreshLayout.setRefreshing(false);
        loadMoreView.setVisibility(GONE);
        isLoading = false;
        swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        if (onLoadListener != null) {
            onLoadListener.onRefresh();
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }


    private class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener{

        private RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter;


        public WrapAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter) {
            this.innerAdapter = innerAdapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER_CONTAINER:
                    return new HeaderViewHolder(headerContainer);
                case FOOTER_CONTAINER:

                    return new FooterViewHolder(footerContainer);
            }
            return innerAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position != 0 && position != getItemCount() - 1) {
                if (innerAdapter != null) {
                    holder.itemView.setTag(position - 1);
                    holder.itemView.setOnClickListener(this);
                    innerAdapter.onBindViewHolder(holder, position - 1);
                }
            } else if (position == getItemCount() - 1) {
                loadMoreView.setVisibility(VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return innerAdapter.getItemCount() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER_CONTAINER;
            } else if (position == getItemCount() - 1) {
                return FOOTER_CONTAINER;
            }
            return innerAdapter.getItemViewType(position - 1);
        }

        @Override
        public void onClick(View view) {

            if (onItemClickListener != null){
                int position = (int) view.getTag();
                onItemClickListener.onItemClick(position,view);
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View item);
    }


}
