package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.base.SearchHistoryData;
import com.zncm.timepill.modules.ui.NoteInBookAc;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;


import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;


public class SearchHistoryFragment extends BaseFt {
    private Activity ctx;
    private EditText editText;
    View view;
    List<String> items;
    TagGroup mTagGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        view = inflater.inflate(R.layout.fragment_search_history, null);
        editText = (EditText) ctx.findViewById(R.id.editText);
        editText.setTextColor(TpSp.getThemeColor());
        mTagGroup = (TagGroup) view.findViewById(R.id.tag_group);
        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String key) {
                if (StrUtil.notEmptyOrNull(key)) {
                    toSearch(key);
                }
            }
        });
        getData();
        return view;
    }

    private void getData() {
        items = new ArrayList<>();
        ArrayList<SearchHistoryData> tmp = DbUtils.getSearchHistory(ctx);
        if (StrUtil.listNotNull(tmp)) {
            for (SearchHistoryData data : tmp) {
                items.add(data.getKey());
            }
        }
        mTagGroup.setTags(items);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("search").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_search)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("md_delete").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_delete)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item == null || item.getTitle() == null) {
            return false;
        }


        if (item.getTitle().equals("search")) {
            String key = editText.getText().toString().trim();
            if (!XUtil.notEmptyOrNull(key)) {
                XUtil.tShort("请输入关键字!");
                return false;
            }

            SearchHistoryData searchHistoryData = new SearchHistoryData(key);
            DbUtils.insertSearchHistory(searchHistoryData);
            toSearch(key);
        } else if (item.getTitle().equals("md_delete")) {
            //清空回收站
            new MaterialDialog.Builder(ctx)
                    .title("清空搜索历史")
                    .content("确认清空?")
                    .theme(Theme.LIGHT)
                    .positiveText("清空")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog materialDialog) {
                            DbUtils.delAllSearchHistory();
                            getData();
                        }
                    })
                    .show();

        }
        return false;
    }

    private void toSearch(String key) {
        Intent intent = new Intent(ctx, NoteInBookAc.class);
        intent.putExtra("key", key);
        intent.putExtra("bSearch", true);
        startActivity(intent);
    }


}
