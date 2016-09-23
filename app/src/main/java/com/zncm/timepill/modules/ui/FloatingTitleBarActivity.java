package com.zncm.timepill.modules.ui;

import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zncm.timepill.R;
import com.zncm.timepill.modules.view.ObservableScrollView;
import com.zncm.timepill.modules.view.ProgressDrawable;

public class FloatingTitleBarActivity extends ActionBarActivity {

    protected ObservableScrollView scrollView;
    protected ImageView imageView;
    protected FrameLayout titleLayout;
    protected View titleBackground;
    protected TextView titleView;
    protected LinearLayout bodyLayout;
    protected TextView bodyTextView;
    protected ImageButton quasiFab;
    protected Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        final View root = getLayoutInflater().inflate(R.layout.activity_floating_title_bar, null);
        setContentView(root);



        imageView = (ImageView) root.findViewById(R.id.floating_img);

        bodyLayout = (LinearLayout) root.findViewById(R.id.body_layout);
        bodyTextView = (TextView) root.findViewById(R.id.test_text);

        quasiFab = (ImageButton) root.findViewById(R.id.join_group_button);
        quasiFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "I do nothing", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleLayout = (FrameLayout) findViewById(R.id.title_layout);
        titleView = (TextView) findViewById(R.id.floating_title_bar_text);
        titleBackground = findViewById(R.id.title_background_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}