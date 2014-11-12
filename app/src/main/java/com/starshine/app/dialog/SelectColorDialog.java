package com.starshine.app.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.starshine.app.R;
import com.starshine.app.constant.IntentConstant;

public class SelectColorDialog extends Activity {

	private LinearLayout llWhite;	// “白色”
	private LinearLayout llBlack;	// “黑色”

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_select_color);

        llBlack = (LinearLayout) findViewById(R.id.llBlack);
        llWhite = (LinearLayout) findViewById(R.id.llWhite);

        llBlack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取启动该Activity之间的Activity对应的Intent
                Intent intent = getIntent();
                intent.putExtra(
                        IntentConstant.SELECT_COLOR_VALUE,
                        Color.BLACK);
                //设置该SelectActivity的结果码，并设置结束之后退回的Activity
                SelectColorDialog.this.setResult(RESULT_OK, intent);
                //结束SelectCityActivity
                SelectColorDialog.this.finish();
            }
        });

        llWhite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//获取启动该Activity之间的Activity对应的Intent
                Intent intent = getIntent();
                intent.putExtra(
                        IntentConstant.SELECT_COLOR_VALUE,
                        Color.WHITE);
                //设置该SelectActivity的结果码，并设置结束之后退回的Activity
                SelectColorDialog.this.setResult(RESULT_OK, intent);
                //结束SelectCityActivity
                SelectColorDialog.this.finish();
			}
		});
	}
}
