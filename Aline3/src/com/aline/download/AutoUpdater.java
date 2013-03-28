package com.aline.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aline.activity.R;
import com.aline.app.App;
import com.aline.util.Tools;

/**
 * @author Administrator
 * 
 */
public class AutoUpdater {
	private String TAG = "AutoUpdater";
	Context tcontext;
	private String updataStr;
	private Resources mResources = null;
	private boolean backstage;
	public ProgressDialog progress;
	// ProgressDialog pBar;
	private DownloadDialogHandler mDownloadHandler = null;

	private AutoUpdater(Context context, boolean backstage) {
		this.tcontext = context;
		this.backstage = backstage;
		this.mResources = context.getResources();
		mDownloadHandler = new DownloadDialogHandler(new DownloadDialog(
				context, "软件更新", "正在下载最新版本安装包..."));
	}

	private void checkForUpdate() {
		if (!backstage) {
			if (progress == null) {
				progress = new ProgressDialog(tcontext);
			}
			progress.setMessage("正在检查更新...");
			progress.show();
		}
		Tools.executorService.submit(new Runnable() {
			@Override
			public void run() {

				String url = "http://" + Tools.HOST + "/"
						+ tcontext.getText(R.string.update).toString();
				String revision = tcontext.getText(R.string.revision)
						.toString();

				String updateUrl = checkUpdate(url, revision);

				//检查到url  提示更新
				Message msg = mHandler.obtainMessage();
				if(updateUrl!=null&&!"".equals(updateUrl)){
				msg.what = 1;
				msg.obj = updateUrl;
				mHandler.sendMessage(msg);
			}else{
				msg.what = 2;
				msg.obj = updateUrl;
				mHandler.sendMessage(msg);	
			}
		}

		});
	}

	public String checkUpdate(String checkURL, String curVersion) {
		if (checkURL == null)
			return null;

		if (checkURL.indexOf("?") > 0) {
			checkURL = checkURL + "&v=" + curVersion;
		} else {
			checkURL = checkURL + "?v=" + curVersion;
		}
		if (Tools.isDebug)
			System.out.println("checkURL:" + checkURL);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(checkURL);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			if (is != null) {
				byte[] buf = new byte[1024];
				int ch = -1;
				StringBuffer buf1 = new StringBuffer();
				while ((ch = is.read(buf)) != -1) {
					buf1.append(new String(buf, 0, ch));
				}
				String revision = buf1.toString().trim();

				// revision=revision.replaceAll(" ","");

				String url = getUpdateUrl(revision);
				if (url.toLowerCase().startsWith("http")) {
					return url;
				}
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	public String getUpdateUrl(String html) {
		try {
			JSONObject obj = new JSONObject(html);
			String url = "";
			String sms = "";
			String desc = "";
			try {
				if (obj.getString("url") != null) {
					url = obj.getString("url");
				}
			} catch (Exception e) {
			}

			try {
				if (obj.getString("desc") != null) {
					desc = obj.getString("desc");
				}
			} catch (Exception e) {
			}

			if (!"".equals(desc)) {
				updataStr = desc;
			}

			if (!"".equals(url)) {
				return url;
			}

		} catch (Exception e) {
		}
		return "";
	}

	void downFile(final String url) {
		// pBar.show();

		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				Message msg = Message.obtain();
				try {
					String fileName = getFileName(url);
					File f1 = getFile(fileName);
					if (!f1.exists()) {
						f1.mkdirs();
					}

					if (f1.exists()) {
						f1.delete();
					}

					response = client.execute(get);

					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						InputStream is = entity.getContent();
						int fileSize = (int) entity.getContentLength();
						FileOutputStream fileOutputStream = null;
						int count = 0;
						if (is != null) {
							fileOutputStream = new FileOutputStream(f1);

							byte[] buf = new byte[1024];
							int ch = -1;
							int totalRead = 0;
							while ((ch = is.read(buf)) != -1) {
								msg = Message.obtain();
								msg.obj = DownloadStates.MESSAGE_DOWNLOAD_PROGRESS;
								totalRead += ch;
								msg.arg1 = totalRead;// / 1024;
								msg.arg2 = fileSize;// / 1024;
								float fileLen = (float) msg.arg2 / 1024;
								float readLen = (float) msg.arg1 / 1024;
								DecimalFormat df = new DecimalFormat("0.00");
								String fileLen_str = df.format(fileLen);
								String readLen_str = df.format(readLen);
								if (Tools.isDebug)
									Log.i("AutoUpdater", "msg.arg1" + msg.arg1
											+ "msg.arg2" + msg.arg2);
								// String
								// title="\n下载更新"+"\n"+readLen_str+"K"+"/"+fileLen_str+"K";
								String title = "下载更新中....";
								
								Bundle data = new Bundle();
								data.putString("msg", title);
								msg.setData(data);
								mDownloadHandler.sendMessage(msg);

								fileOutputStream.write(buf, 0, ch);
								count += ch;
							}

						} else {
							Toast.makeText(tcontext, "is null",
									Toast.LENGTH_SHORT).show();
						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
						if (is != null) {
							is.close();
						}

						down(url);

					} else {
						fail();
					}
				} catch (ClientProtocolException e) {
					Toast.makeText(tcontext, "ClientProtocolException!",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					fail();
				} catch (IOException e) {
					Toast.makeText(tcontext, "IOException!" + e.getMessage(),
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					fail();
				}

			}
		}.start();

	}

	void down(final String url) {
		mHandler.post(new Runnable() {
			public void run() {
				// pBar.cancel();
				Message msg = Message.obtain();
				msg.obj = DownloadStates.MESSAGE_DOWNLOAD_COMPLETE;
				mDownloadHandler.sendMessage(msg);
				update1(url);
			}
		});
	}

	File getFile(String filename) {
		File updateDir = tcontext.getFilesDir();
		File file = new File(updateDir.getPath(), filename);

		return file;
	}

	String getFileName(String path) {
		if (path != null && path.indexOf("/") > -1) {
			return path.substring(path.lastIndexOf("/") + 1);
		}
		return "95081DJT.apk";
	}

	void update1(String url) {
		String fileName = getFileName(url);
		File f = getFile(fileName);
		String cmd = "chmod 777 " + f.getPath();
		try {
			Runtime.getRuntime().exec(cmd);
			// Toast.makeText(Update.this, "cmd:" + cmd,
			// Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(tcontext, "cmd error:" + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f),
				"application/vnd.android.package-archive");
		tcontext.startActivity(intent);
	}

	public void fail() {
		// pBar.cancel();
		Toast.makeText(App.getInstance(), "更新失败！", Toast.LENGTH_LONG).show();
		/*
		 * Intent intent = new Intent(this, InitActivity.class);
		 * startActivity(intent); finish();
		 */
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int n = msg.what;
			switch (n) {
			case 1:
				if (progress != null && progress.isShowing()) {
					progress.cancel();
				}
				final String updateUrl = (String) msg.obj;
				if (updateUrl != null) {
					Activity activity = (Activity) tcontext;
					if (activity.isFinishing()) {
						return;
					}
					LayoutInflater inflater = LayoutInflater.from(App
							.getInstance());
					// LayoutInflater inflater = getLayoutInflater();;
					View view = inflater.inflate(R.layout.updata_dialog, null);
					Builder builder = new AlertDialog.Builder(tcontext);
					builder.setTitle(R.string.updatadialog_title)
							.setPositiveButton(
									Html.fromHtml("<font color=#288245>立即更新</font>"),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// App.getInstance().isAutoUpate
											// =true;
											// pBar = new
											// ProgressDialog(tcontext);
											// pBar.setTitle("正在下载");
											// pBar.setMessage("请稍候...");
											// pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
											// pBar.setCancelable(false);

											Message msg = Message.obtain();
											msg.obj = DownloadStates.MESSAGE_DOWNLOAD_STARTING;
											Bundle data = new Bundle();
											data.putString("msg",
													"开始下载最新版本安装包...");
											msg.setData(data);
											mDownloadHandler.sendMessage(msg);

											downFile(updateUrl);
											App.getInstance().isStartCheckUpdata = false;
											App.getInstance().isAutoUpate = true;
										}
									})
							.setNegativeButton(
									Html.fromHtml("<font color=#666666>暂不更新</font>"),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// 点击"取消"按钮之后退出程序
											dialog.cancel();
											App.getInstance().isStartCheckUpdata = false;
											App.getInstance().isAutoUpate = true;
											// Intent intent = new
											// Intent(BaseActivity.this,
											// InitActivity.class);
											// startActivity(intent);
											// finish();
										}
									}).setView(view)
							.setIcon(android.R.drawable.ic_dialog_info);
					AlertDialog dialog = builder.create();
					dialog.show();
					TextView dialogTitle = (TextView) dialog
							.findViewById(R.id.dialogtitle);
					dialogTitle.setText(updataStr);
				} else {
					if (!backstage) {
						Toast.makeText(tcontext, "当前已是最新版本", Toast.LENGTH_SHORT)
								.show();
					}
				}
				break;
			case 2:
				if (!backstage) {
					Toast.makeText(tcontext, "暂无最新版本", Toast.LENGTH_SHORT)
							.show();
				}
				if (progress != null && progress.isShowing()) {
					progress.cancel();
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	public static void CheckForUpdate(Context context, boolean backstage) {
		AutoUpdater autoUpdater = new AutoUpdater(context, backstage);
		autoUpdater.checkForUpdate();
	}

	// public interface CheckForUpdateFinish {
	// public static final int SUCESS = 1;
	// public static final int FAIL = 0;
	// public abstract void callBack(String updatastr);
	// }
}
