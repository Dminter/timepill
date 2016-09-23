package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.timepill.R;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.adapter.NoteAdapter;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.ui.NoteCommentAc;
import com.zncm.timepill.modules.ui.NoteDetailsAc;
import com.zncm.timepill.modules.ui.NoteNewAc;
import com.zncm.timepill.modules.ui.PhotoShowAc;
import com.zncm.timepill.modules.ui.ShowInfoActivity;
import com.zncm.timepill.modules.view.CellTopicView;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class NoteBaseFt extends BaseListFt {
    protected Activity mActivity;
    protected List<NoteData> datas = null;
    protected NoteAdapter mAdapter;
    protected int curPosition;
    protected CellTopicView topicView;
    protected ArrayList<Integer> maskUser;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivity = (Activity) getActivity();
        maskUser = TpApplication.getInstance().getMaskUser();
        datas = new ArrayList<NoteData>();
        mAdapter = new NoteAdapter(mActivity) {
            @Override
            public void setData(int position, NoteViewHolder holder) {
                final NoteData data = (NoteData) datas.get(position);
                if (data == null) {
                    return;
                }

                holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XUtil.viewUser(mActivity, new UserData(data.getUser()));
                    }
                });
                holder.tvAuthor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        XUtil.viewUser(mActivity, new UserData(data.getUser()));
                    }
                });

                if (StrUtil.notEmptyOrNull(data.getContent())) {
                    holder.tvContent.setVisibility(View.VISIBLE);
                    holder.tvContent.setMaxLines(5);
                    holder.tvContent.setText(StrUtil.replaceBlank(data.getContent()));

                    XUtil.doubleTextPre(mActivity, holder.tvContent, data.getContent());


                } else {
                    holder.tvContent.setVisibility(View.GONE);
                }
                boolean isUser = false;
                MiniUserData userData = data.getUser();

                if (userData != null) {
                    if (StrUtil.notEmptyOrNull(userData.getName())) {
                        holder.tvAuthor.setVisibility(View.VISIBLE);
                        holder.tvAuthor.setText(userData.getName());
                    } else {
                        holder.tvAuthor.setVisibility(View.GONE);
                    }
                    if (StrUtil.notEmptyOrNull(userData.getIconUrl()) && !TpSp.getNoPic()) {
                        holder.ivIcon.setVisibility(View.VISIBLE);
                        XUtil.getImageLoader().displayImage(userData.getIconUrl(), holder.ivIcon, XUtil.getRoundedOptions());
                    } else {
                        holder.ivIcon.setVisibility(View.GONE);
                    }
                    isUser = false;
                } else {
                    isUser = true;
                    if (StrUtil.notEmptyOrNull(data.getNotebook_subject())) {
                        holder.tvAuthor.setVisibility(View.VISIBLE);
                        holder.tvAuthor.setText(data.getNotebook_subject());
                        holder.tvAuthor.setTextColor(getResources().getColor(R.color.gray));
                    } else {
                        holder.tvAuthor.setVisibility(View.GONE);
                    }
                    holder.ivIcon.setVisibility(View.GONE);
                }

                if (StrUtil.notEmptyOrNull(data.getPhotoThumbUrl()) && !TpSp.getNoPic()) {
                    holder.ivPhoto.setVisibility(View.VISIBLE);
                    XUtil.getImageLoader().displayImage(data.getPhotoThumbUrl(), holder.ivPhoto, XUtil.getOptions());
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

                holder.replyIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, NoteCommentAc.class);
                        intent.putExtra(TpConstants.KEY_ID, data.getId());
                        intent.putExtra(TpConstants.KEY_DATA, data);
                        startActivity(intent);
                    }
                });

                if (StrUtil.notEmptyOrNull(data.getNotebook_subject()) && !isUser) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText("《" + StrUtil.subStrDot(data.getNotebook_subject(), 12) + "》");
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }

                if (StrUtil.notEmptyOrNull(data.getCreated())) {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setText(StrUtil.timeDay(data.getCreated()));
                } else {
                    holder.tvTime.setVisibility(View.GONE);
                }

                holder.tvReply.setVisibility(View.VISIBLE);
                if (data.getComment_count() > 0) {
                    holder.tvReply.setText(String.valueOf(data.getComment_count()));
                } else {
                    holder.tvReply.setText("");
                }
            }
        };
        mAdapter.setItems(datas);
        topicView = new CellTopicView(mActivity);
        lvBase.addHeaderView(topicView);
        XUtil.listViewRandomAnimation(lvBase, mAdapter);
        lvBase.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition >= 0) {
                    Intent intent = new Intent(mActivity, NoteDetailsAc.class);
                    intent.putExtra(TpConstants.KEY_ID, datas.get(curPosition).getId());
                    intent.putExtra(TpConstants.KEY_STRING, "details");
                    intent.putExtra(TpConstants.KEY_PARAM_DATA, datas.get(curPosition));
                    startActivity(intent);
                }

            }
        });

        lvBase.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position - lvBase.getHeaderViewsCount();
                if (curPosition >= 0) {
                    final String texts = datas.get(curPosition).getContent();
//                    if (StrUtil.notEmptyOrNull(texts)) {
//                        Intent newIntent = new Intent(mActivity, ShowInfoActivity.class);
//                        newIntent.putExtra("show", texts);
//                        startActivity(newIntent);
//                    }
                    XUtil.copyDlg(mActivity, texts);
                }
                return true;
            }
        });

        initData();
        return view;
    }


    public void toTop() {

    }

    public void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        maskUser = TpApplication.getInstance().getMaskUser();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void getData(final boolean bFirst) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mActivity.invalidateOptionsMenu();
    }


    public void loadComplete() {
        super.loadComplete();
    }


}
