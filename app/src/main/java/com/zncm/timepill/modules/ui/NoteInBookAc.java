package com.zncm.timepill.modules.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.materialicons.Iconify;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.Pager;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.NoteScanAdapter;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.NotebooksService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.view.LoadMoreListView;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.xip.errorview.ErrorView;

public class NoteInBookAc extends BaseAc implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, AbsListView.OnScrollListener {
    private Activity mActivity;
    private List<NoteData> datas = null;
    private NoteScanAdapter mAdapter;
    private int curPosition;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView lvBase;
    private int pageIndex = 1;
    private Set<Integer> IDs;
    private NoteBookData noteBookData;
    ErrorView errorView;
    private Set<String> timeSet;
    private Set<Integer> IDSet;
    boolean bSearch = false;
    String key;

    //head
    ObjectAnimator fade;
    View mContainerHeader;
    TextView title, content;
    ImageView icon;
    View headerView = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(TpSp.getThemeColor());
        lvBase = (LoadMoreListView) findViewById(R.id.listView);
        lvBase.setOnLoadMoreListener(this);
        initHeadView();

        mActivity = this;
        timeSet = new HashSet<String>();
        IDSet = new HashSet<Integer>();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            bSearch = intent.getExtras().getBoolean("bSearch");
            if (bSearch) {
                key = intent.getExtras().getString("key");
                getSupportActionBar().setTitle("搜索 " + key);
            } else {
                Serializable dataParam = intent.getSerializableExtra(TpConstants.KEY_PARAM_DATA);
                noteBookData = (NoteBookData) dataParam;
                getSupportActionBar().setTitle(noteBookData.getSubject());
            }
        }


        if (noteBookData != null && !bSearch) {
            lvBase.addHeaderView(headerView);
            if (StrUtil.notEmptyOrNull(noteBookData.getCoverUrl()) && !TpConstants.BOOK_COVER_DEFAULT.equals(noteBookData.getCoverUrl()) && !TpSp.getNoPic()) {
                XUtil.getImageLoader().displayImage(noteBookData.getCoverUrl(), icon, XUtil.getOptions());
            } else {
                icon.setImageResource(R.drawable.default_notebook);
            }
            title.setText(noteBookData.getSubject());
            content.setText(noteBookData.getCreated() + "-" + noteBookData.getExpired());
        }


        errorView = (ErrorView) findViewById(R.id.error_view);
        ErrorView.Config errorViewConfig = ErrorView.Config.create()
                .image(XUtil.initIconTheme(Iconify.IconValue.md_error, 50))
                .title("无日记~")
                .retryVisible(true)
                .retryText("刷新")
                .build();

        errorView.setConfig(errorViewConfig);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                onRefresh();
            }
        });


        XUtil.initViewTheme(ctx, swipeLayout);
        datas = new ArrayList<NoteData>();
        mAdapter = new NoteScanAdapter(mActivity) {
            @Override
            public void setData(int position, NoteViewHolder holder) {
                final NoteData data = (NoteData) datas.get(position);

                if (data == null) {
                    return;
                }


                if (StrUtil.notEmptyOrNull(data.getContent())) {
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setText(StrUtil.replaceBlank(data.getContent()));
                } else {
                    holder.tvContent.setVisibility(View.GONE);
                }
                if (StrUtil.notEmptyOrNull(data.getPhotoUrl()) && !TpSp.getNoPic()) {
                    holder.ivPhoto.setVisibility(View.VISIBLE);
                    XUtil.getImageLoader().displayImage(data.getPhotoUrl(), holder.ivPhoto, XUtil.getOptions());
                    holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, PhotoShowAc.class);
                            intent.putExtra(TpConstants.KEY_STRING, data.getPhotoUrl());
                            startActivity(intent);
                        }
                    });
                } else {
                    holder.ivPhoto.setVisibility(View.GONE);
                }
                if (StrUtil.notEmptyOrNull(data.getCreated())) {
                    String month = TimeUtils.getMonth(TimeUtils.timeStrToLong(data.getCreated()));
                    String day = TimeUtils.getDay(TimeUtils.timeStrToLong(data.getCreated()));

                    boolean flag = timeSet.add(month + day);
                    if (flag || IDSet.contains(data.getId())) {
                        if (flag) {
                            IDSet.add(data.getId());
                        }
                        holder.tvTime.setText(day);
                        String monthStr = month;
                        if (monthStr.startsWith("0")) {
                            monthStr = monthStr.substring(1);
                        }
                        holder.tvTimeMonmth.setText(monthStr + "月");
                        holder.llTime.setVisibility(View.VISIBLE);
                        holder.viewDiv.setVisibility(View.VISIBLE);
                    } else {
                        holder.viewDiv.setVisibility(View.GONE);
                        holder.llTime.setVisibility(View.INVISIBLE);
                    }
//                    String tmp1 = "";
//
//                    String tmp2 = month + day;
//                    if (position == 0) {
//                        tmp1 = "";
//                    } else {
//                        String preTime = datas.get(position - 1).getCreated();
//                        if (preTime != null && StrUtil.notEmptyOrNull(preTime)) {
//                            month = TimeUtils.getMonth(TimeUtils.timeStrToLong(preTime));
//                            day = TimeUtils.getDay(TimeUtils.timeStrToLong(preTime));
//                            tmp1 = month + day;
//                        }
//                    }
//                    if (!tmp1.equals(tmp2)) {
//                        holder.llTime.setVisibility(View.VISIBLE);
//                        holder.viewDiv.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.llTime.setVisibility(View.INVISIBLE);
//                        holder.viewDiv.setVisibility(View.GONE);
//                    }
//                    holder.tvTime.setText(day);
//                    holder.tvTimeMonmth.setText(month + "月");
                } else {
                    holder.viewDiv.setVisibility(View.VISIBLE);
                    holder.llTime.setVisibility(View.INVISIBLE);
                }
            }
        };
        mAdapter.setItems(datas);
        XUtil.listViewRandomAnimation(lvBase, mAdapter);
        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition >= 0) {
                    Intent intent = new Intent(mActivity, NoteDetailsAc.class);
                    intent.putExtra(TpConstants.KEY_ID, datas.get(curPosition).getId());
                    intent.putExtra(TpConstants.KEY_STRING, "details_noOp");
                    intent.putExtra(TpConstants.KEY_PARAM_DATA, datas.get(curPosition));
                    startActivity(intent);
                }

            }
        });
        initData();
    }

    private void initHeadView() {
        headerView = LayoutInflater.from(this)
                .inflate(R.layout.listview_header, lvBase, false);
        mContainerHeader = headerView.findViewById(R.id.container);
        title = (TextView) headerView.findViewById(R.id.title);
        content = (TextView) headerView.findViewById(R.id.content);
        icon = (ImageView) headerView.findViewById(R.id.icon);

        // prepare the fade in/out animator
        fade = ObjectAnimator.ofFloat(mContainerHeader, "alpha", 0f, 1f);
        fade.setInterpolator(new DecelerateInterpolator());
        fade.setDuration(600);
        lvBase.setOnScrollListener(this);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.ac_noteinbook;
    }


    public void loadComplete() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                lvBase.onLoadMoreComplete();
            }
        });
    }


    public void initData() {

        getData(true);
    }


    public void getData(final boolean bFirst) {

        if (bFirst && !bSearch) {
            if (noteBookData != null && noteBookData.getIsExpired().equals("true")) {
                datas = DbUtils.getNoteListByClass(EnumData.NoteClassEnum.INBOOK.getValue(), noteBookData.getId(), true);
                mAdapter.setItems(datas);
            }
        }
        if (bSearch) {
            ServiceFactory.getService(NoteService.class).search(key, pageIndex, new Callback<Pager<NoteData>>() {
                @Override
                public void success(Pager<NoteData> pagers, Response response) {
                    loadComplete();
                    if (pagers == null) {
                        if (!StrUtil.listNotNull(datas)) {
                            errorView.setVisibility(View.VISIBLE);
                        }
                        return;
                    } else if (pagers != null && !StrUtil.listNotNull(pagers.items)) {
                        if (!StrUtil.listNotNull(datas)) {
                            errorView.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    errorView.setVisibility(View.GONE);
                    List<NoteData> list = pagers.items;
                    if (bFirst) {
                        IDs = new HashSet<Integer>();
                        datas = new ArrayList<NoteData>();
                    }
                    if (StrUtil.listNotNull(list)) {
                        DbUtils.addNoteList(list, EnumData.NoteClassEnum.INBOOK.getValue());
                        pageIndex++;
                        for (NoteData data : list) {
                            if (IDs.add(data.getId())) {
                                datas.add(data);
                            }
                        }
                    }
                    mAdapter.setItems(datas);
                }

                @Override
                public void failure(RetrofitError error) {
                    XUtil.onFailure(error);
                }
            });
        } else {
            ServiceFactory.getService(NotebooksService.class).getNotebooksDiaries(noteBookData.getId(), pageIndex, new Callback<Pager<NoteData>>() {
                @Override
                public void success(Pager<NoteData> pagers, Response response) {
                    loadComplete();
                    if (pagers == null) {
                        if (!StrUtil.listNotNull(datas)) {
                            errorView.setVisibility(View.VISIBLE);
                        }
                        return;
                    } else if (pagers != null && !StrUtil.listNotNull(pagers.items)) {
                        if (!StrUtil.listNotNull(datas)) {
                            errorView.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    errorView.setVisibility(View.GONE);
                    List<NoteData> list = pagers.items;
                    if (bFirst) {
                        IDs = new HashSet<Integer>();
                        datas = new ArrayList<NoteData>();
                    }
                    if (StrUtil.listNotNull(list)) {
                        DbUtils.addNoteList(list, EnumData.NoteClassEnum.INBOOK.getValue());
                        pageIndex++;
                        for (NoteData data : list) {
                            if (data != null) {
                                if (IDs.add(data.getId())) {
                                    datas.add(data);
                                }
                            }
                        }
                    }
                    mAdapter.setItems(datas);

                }

                @Override
                public void failure(RetrofitError error) {
                    XUtil.onFailure(error);
                }
            });
        }


    }


    @Override
    public void onRefresh() {
        pageIndex = 1;
        getData(true);
    }

    @Override
    public void onLoadMore() {
        getData(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (bSearch) {
//        }
//        menu.add("search").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_search)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
//        if (item.getTitle().equals("search")) {
//            Intent intent = new Intent(ctx, SearchPopActivity.class);
//            startActivity(intent);
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * Listen to the scroll events of the listView
     *
     * @param view             the listView
     * @param firstVisibleItem the first visible item
     * @param visibleItemCount the number of visible items
     * @param totalItemCount   the amount of items
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onScroll(AbsListView view,
                         int firstVisibleItem,
                         int visibleItemCount,
                         int totalItemCount) {
        // we make sure the list is not null and empty, and the header is visible
        if (view != null && view.getChildCount() > 0 && firstVisibleItem == 0) {

            // we calculate the FAB's Y position
            int translation = view.getChildAt(0).getHeight() + view.getChildAt(0).getTop();
//            mFab.setTranslationY(translation>0  ? translation : 0);

            // if we scrolled more than 16dps, we hide the content and display the title
            if (view.getChildAt(0).getTop() < -dpToPx(16)) {
                toggleHeader(false, false);
            } else {
                toggleHeader(true, true);
            }
        } else {
            toggleHeader(false, false);
        }

        // if the device uses Lollipop or above, we update the ToolBar's elevation
        // according to the scroll position.
        if (isLollipop()) {
            if (firstVisibleItem == 0) {
                toolbar.setElevation(0);
            } else {
                toolbar.setElevation(dpToPx(4));
            }
        }
    }

    /**
     * Start the animation to fade in or out the header's content
     *
     * @param visible true if the header's content should appear
     * @param force   true if we don't wait for the animation to be completed
     *                but force the change.
     */
    private void toggleHeader(boolean visible, boolean force) {
        if ((force && visible) || (visible && mContainerHeader.getAlpha() == 0f)) {
            fade.setFloatValues(mContainerHeader.getAlpha(), 1f);
            fade.start();
        } else if (force || (!visible && mContainerHeader.getAlpha() == 1f)) {
            fade.setFloatValues(mContainerHeader.getAlpha(), 0f);
            fade.start();
        }
        // Toggle the visibility of the title.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(!visible);
        }
    }


    /**
     * Convert Dps into Pxs
     *
     * @param dp a number of dp to convert
     * @return the value in pixels
     */
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (dp * (displayMetrics.densityDpi / 160f));
    }

    /**
     * Check if the device rocks, and runs Lollipop
     *
     * @return true if Lollipop or above
     */
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


}
