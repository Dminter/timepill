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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.DraftData;
import com.zncm.timepill.modules.adapter.DraftAdapter;
import com.zncm.timepill.modules.ui.NoteBookAc;
import com.zncm.timepill.modules.ui.NoteNewAc;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftListFt extends BaseListFt {
    protected Activity ctx;
    protected List<DraftData> datas = null;
    protected DraftAdapter mAdapter;
    private int type = EnumData.DataTypeEnum.DRAFT.getValue();
    private int curPostion = 0;
    private DraftData data = null;

    public void getData() {
        try {
            datas = DbUtils.initDraftData(type);
            mAdapter.setItems(datas);
            loadComplete();
        } catch (Exception e) {
        }
    }

    public void initData() {
        getData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = getActivity();
        lvBase.setCanLoadMore(false);

        if (ctx.getIntent().getExtras() != null) {
            type = ctx.getIntent().getExtras().getInt("type");
        }

        datas = new ArrayList<DraftData>();
        mAdapter = new DraftAdapter(ctx) {
            @Override
            public void setData(final int position, DraftViewHolder holder) {
                final DraftData data = (DraftData) datas.get(position);
                if (data == null) {
                    return;
                }
                if (data.getTime() != null) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    String content = TimeUtils.getTimeMDHM(new Date(data.getTime()));
                    holder.tvTime.setText(content);
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                }
                if (StrUtil.notEmptyOrNull(data.getContent())) {
                    holder.tvText.setVisibility(View.VISIBLE);
                    holder.tvText.setText(data.getContent());
                } else {
                    holder.tvText.setVisibility(View.GONE);
                }

            }
        };
        mAdapter.setItems(datas);
        XUtil.listViewRandomAnimation(lvBase, mAdapter);
        lvBase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPostion = position - lvBase.getHeaderViewsCount();
                if (curPostion >= 0 && StrUtil.listNotNull(datas)) {
                    data = (DraftData) datas.get(position);
                }
                if (data == null) {
                    return;
                }

                if (type == EnumData.DataTypeEnum.CUSTOM_FACE.getValue()) {
                    return;
                }
                Intent newIntent = new Intent(ctx, NoteNewAc.class);
                newIntent.putExtra("note_content", data.getContent());
                startActivity(newIntent);
            }
        });
        lvBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                curPostion = position - lvBase.getHeaderViewsCount();
                if (curPostion >= 0 && StrUtil.listNotNull(datas)) {
                    data = (DraftData) datas.get(position);
                }

                if (data == null) {
                    return true;
                }

                new MaterialDialog.Builder(ctx)
                        .items(new String[]{"删除", "复制"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        try {
                                            DbUtils.deleteDraftData(data.getId());
                                            datas.remove(curPostion);
                                            mAdapter.setItems(datas);
                                        } catch (Exception e) {
                                        }
                                        break;
                                    case 1:
                                        StrUtil.copyText(ctx, data.getContent());
                                        break;
                                }
                            }
                        })
                        .show();
                return true;
            }
        });
        XUtil.initViewTheme(ctx, lvBase);
        initData();


        return view;
    }


    void initDlg() {
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setLines(5);
        editText.setTextColor(TpSp.getThemeColor());
        view.addView(editText);
        XUtil.autoKeyBoardShow(editText);
        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText("保存继续")
                .negativeText("添加")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(materialDialog, false);
                                return;
                            }
                            DbUtils.saveDraftData(content, type);
                            editText.setText("");
                            XUtil.dismissShowDialog(materialDialog, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {
                        XUtil.dismissShowDialog(materialDialog, true);
                        getData();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            DbUtils.saveDraftData(content, type);
                            getData();
                            XUtil.dismissShowDialog(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        getData();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("md_add").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_add)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("md_add")) {
            initDlg();
        }
        return false;
    }

}
