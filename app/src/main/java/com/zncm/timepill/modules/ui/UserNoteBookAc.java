package com.zncm.timepill.modules.ui;

import android.os.Bundle;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.ft.NoteBooksFt;


public class UserNoteBookAc extends BaseAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("笔记本");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new NoteBooksFt())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ft_base;
    }
}
