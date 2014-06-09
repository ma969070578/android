package com.aline.lbs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aline.activity.R;
import com.aline.app.EdjApp;

public class InfosActivity extends Activity {

	private Button back_btn;

	private EdjApp app;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.infos);

		String version = getResources().getString(R.string.revision);

		TextView version1 = (TextView) this.findViewById(R.id.version);
		version1.setText("免费版本  V" + version);
		TextView web = (TextView) this.findViewById(R.id.m_web);
		TextView blog = (TextView) this.findViewById(R.id.m_blog);

		web.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String weburl = InfosActivity.this.getResources().getString(
						R.string.web);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(weburl));
				startActivity(intent);
			}
		});

		blog.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String bogurl = InfosActivity.this.getResources().getString(
						R.string.blog);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(bogurl));
				startActivity(intent);
			}
		});

	}
}
