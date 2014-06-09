package com.aline.activity.wxapi;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.aline.activity.R;
import com.aline.util.Constants;
import com.aline.util.SharedPreTools;
import com.aline.util.EdjTools;

public class TestShare extends Activity {

	private RelativeLayout sina;
	private RelativeLayout sms;
	private RelativeLayout qq;
	private RelativeLayout chat;
	private TextView invite_code;

	private Context context;
	private String[] items = new String[] { "分享给好友", "分享到朋友圈" };
	private String share_content;
	private String	user_id;
	private TextView share_name;
	
	private static final int SHARE_SUCCESS = 30;
	private static final int SHARE_FAIL = 31; 

	private LinearLayout ll_head;
	private RelativeLayout rl_yqcode;
	private String  flag_share;
	private String  yymenu_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/**
		 * 手册内容 分享
		 */
//		Intent intent = getIntent();
//		if(intent != null){
//			flag_share = intent.getStringExtra("yyb_share");
//			yymenu_content = intent.getStringExtra("content");
//		}
//		
//		
//		this.setContentView(R.layout.test_share);
//		context = TestShare.this;
//
//		ShareSDK.initSDK(this);
//
//		sina = (RelativeLayout) this.findViewById(R.id.share_sina);
//		qq = (RelativeLayout) this.findViewById(R.id.share_qq);
//		chat = (RelativeLayout) this.findViewById(R.id.share_chat);
//		sms = (RelativeLayout) this.findViewById(R.id.share_sms);
//		invite_code = (TextView) this.findViewById(R.id.invite_code);
//		user_id = SharedPreTools.readShare(Constants.SHB_LOGIN_USER_PER_NAME, Constants.SHB_LOGIN_USER_ID);
//	       if(!EdjTools.isNull(user_id)){
//	    	   invite_code.setText(user_id);  
//	       }
//       
//	     if(EdjTools.isNull(app.share_content)){  
//          share_content  = this.getResources().getString(R.string.share_content);
//	     }else{
//	      share_content =app.share_content;
//	     }
//        share_content = share_content.replace("##", user_id);
//        
//        //分享说明
//        share_name  = (TextView) this.findViewById(R.id.share_name);
//        String sharename =SharedPreTools.readShare(Constants.APP_CONFIG, Constants.SHB_SHARE_CONTENT);
//        //默认显示 string
//        if(!EdjTools.isNull(sharename)){
//        	share_name.setText(sharename);
//        }
//		sina.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// 分享到新浪微博
//				// showShare(true, SinaWeibo.NAME,TestShare.this);
//				if("yyb_share".equals(flag_share)){
//					share(yymenu_content, null, SinaWeibo.NAME);
//				}else{
//					share(share_content, null, SinaWeibo.NAME);
//				}
//				
//			}
//		});
//
//		qq.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// 分享到腾讯微博
//				// showShare(true, TencentWeibo.NAME, TestShare.this);
//				if("yyb_share".equals(flag_share)){
//					share(yymenu_content, null, TencentWeibo.NAME);
//				}else{
//					share(share_content, null, TencentWeibo.NAME);	
//				}
//				
//			}
//		});
//
//		chat.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if("yyb_share".equals(flag_share)){ //分享内容
//					showChatDialogContent();
//				}else{
//					showChatDialog();
//				}
//				
//
//			}
//		});
//
//		sms.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if("yyb_share".equals(flag_share)){ //分享内容
//					EdjTools.sendSms(TestShare.this, yymenu_content, "");
//				}else{
//					EdjTools.sendSms(TestShare.this, share_content, "");
//				}
//				
//
//			}
//		});
//		
//		ll_head = (LinearLayout) findViewById(R.id.ll_head);
//		rl_yqcode = (RelativeLayout) findViewById(R.id.rl_yqcode);
//		if("yyb_share".equals(flag_share)){
//			ll_head.setVisibility(View.GONE);
//			rl_yqcode.setVisibility(View.GONE);
//		}
	}

	// 新浪微博分享 腾讯微博等只需修改 NAME
	public void share(String text, String photopath, String sharename) {

		Platform.ShareParams sp = new SinaWeibo.ShareParams();
		sp.text = text;

		if (!EdjTools.isNull(photopath)){
//			sp.imagePath = "/mnt/sdcard/测试分享的图片.jpg";
			sp.imagePath = photopath;
			
		}

		Platform weibo = ShareSDK.getPlatform(context, sharename);

		// 设置分享事件回调
		weibo.setPlatformActionListener(new PlatformActionListener() {

			public void onError(Platform platform, int action, Throwable t) {
				// 操作失败的处理代码
				Message m = handler.obtainMessage();
				m.what = SHARE_FAIL;
				TestShare.this.handler.sendMessage(m);
			}

			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				// 操作成功的处理代码
				Message m = handler.obtainMessage();
				m.what = SHARE_SUCCESS;
				TestShare.this.handler.sendMessage(m);
			}

			public void onCancel(Platform platform, int action) {
				// 操作取消的处理代码
			}

		});

		// 执行图文分享
		weibo.share(sp);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	/**
	 * 显示选择对话框
	 */
	private void showChatDialog() {

		new AlertDialog.Builder(this).setTitle("分享到")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							share(share_content, null, Wechat.NAME);
							break;
						case 1:

							share(share_content, null, WechatMoments.NAME);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
	/**
	 * 微信 内容分享
	 */
	private void showChatDialogContent() {

		new AlertDialog.Builder(this).setTitle("分享到")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							share(yymenu_content, null, Wechat.NAME);
							break;
						case 1:

							share(yymenu_content, null, WechatMoments.NAME);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
	
	
	
	private Handler handler = new Handler() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case SHARE_SUCCESS:

				Toast.makeText(TestShare.this, "分享成功", Toast.LENGTH_SHORT).show();
				break;
			case SHARE_FAIL:

				Toast.makeText(TestShare.this, "分享失败", Toast.LENGTH_SHORT).show();
				break;

			}
		}

	};
}
