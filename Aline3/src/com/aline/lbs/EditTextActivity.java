package com.aline.lbs;

import com.aline.activity.R;
import com.aline.util.EdjTools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTextActivity extends Activity {


private Button button_left;
private Button button_right;
private EditText edit;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.edit_text);

	button_left = (Button) findViewById(R.id.butt_left);
	button_left.setVisibility(View.VISIBLE);
	button_left.setText(getResources().getString(R.string.back));

	button_right = (Button) findViewById(R.id.butt_right);
	button_right.setVisibility(View.VISIBLE);
	button_right.setText(getResources().getString(R.string.ok));

	edit = (EditText) findViewById(R.id.edittext);

	button_left.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});

	button_right.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String content=edit.getText().toString();
//			Toast.makeText(EditTextActivity.this, content, Toast.LENGTH_SHORT).show();
			EdjTools.writeShare(EditTextActivity.this, "user_name", content);
			finish();
		}
	});

}
}