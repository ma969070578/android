package com.emotte.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class HttpUtil {
	
	
	private static final int TIMEOUT = 15000; //15000
	private static final int TIMEOUT_SOCKET = 20000; //20000
	
	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	/** @Fields TYPE_NET_WORK_DISABLED : 网络不可用 */
	public static final int TYPE_NET_WORK_DISABLED = 0;
	/** @Fields TYPE_CM_CU_WAP : 移动联通wap10.0.0.172 */
	public static final int TYPE_CM_CU_WAP = 4;
	/** @Fields TYPE_CT_WAP : 电信wap 10.0.0.200 */
	public static final int TYPE_CT_WAP = 5;
	/** @Fields TYPE_OTHER_NET : 电信,移动,联通,wifi 等net网络 */
	public static final int TYPE_OTHER_NET = 6;
	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");
	
    private static HashMap<String,
                           HttpClient> sessionMap = new HashMap<String,
            HttpClient>();

    public HttpUtil() {
    }
    
    /*
     * 将形如“\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002”的字符串解码
     * */
	public static String decode(String s) {
		StringReader s1 = new StringReader(s);
		try {
			char[] chars = new char[s.length()];
			s1.read(chars);
			return new String(chars);
		} catch (Exception ex) {
		}
		return null;
	}
	
	/**
     * get  httpGet
     *
     * @param url String
     * @param name String     连接名称，用以维护session,不需要维持连接时请置null，操作结束后请调用destroy方法
     * @param charset String
     * @return String
	 * @throws UnsupportedEncodingException 
     */
    public static String get(String url, String name, String charset)   {
        if (charset == null)
            charset = HTTP.UTF_8;
        HttpClient httpclient = null;
        try {
      
           
	            if (name == null) { //不需要维持连接
	                httpclient = createHttpClient();
	            } else if (sessionMap.containsKey(name)) {
	                httpclient = sessionMap.get(name);
	            } else {
	                httpclient = createHttpClient();
	                sessionMap.put(name, httpclient);
	            }
            

            HttpGet httpget1 = new HttpGet(url); 
            HttpEntity entity1;
            String result=null;
//            synchronized(httpclient){
	            HttpResponse response1 = httpclient.execute(httpget1);
	            entity1 = response1.getEntity();
//            }
	            result =  EntityUtils.toString(entity1, charset);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	destroy("hhh");
        }

        return null;
    }

    /**
     * post
     *
     * @param url String
     * @param map HashMap     提交表单的键值对
     * @param name String     连接名称，用以维护session
     * @param charset String
     * @return String
     */
    public static String post(String url, HashMap<String, String> map,
            String name, String charset) {
        if (charset == null)
            charset = HTTP.UTF_8;

        try {
            HttpClient httpclient;

            if (name == null) { //不需要维持连接
                httpclient = createHttpClient();
            } else if (sessionMap.containsKey(name)) {
                httpclient = sessionMap.get(name);
            } else {
                httpclient = createHttpClient();
                sessionMap.put(name, httpclient);
            }

            HttpPost httpost = new HttpPost(url);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            if (map != null) {
                Iterator it = map.keySet().iterator();

                while (it.hasNext()) {
                    String key = (String) it.next();
                    nvps.add(new BasicNameValuePair(key, map.get(key)));
                }
            }

            httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));

//            System.out.println("!!!!!!!!!!!!!!!");
//            long s=System.currentTimeMillis();
            String requstStr;
//            synchronized(httpclient){
	            HttpResponse response = httpclient.execute(httpost);
	//            System.out.println((System.currentTimeMillis()-s)/1000);
	            HttpEntity entity1 = response.getEntity();
	             requstStr = EntityUtils.toString(entity1, charset);
	            entity1.consumeContent();
//            }
            return requstStr;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	destroy("hhh");
        }

        return null;
    }


    public static String post(String url, String content,
                              String name, String charset) {
        if (charset == null)
            charset = HTTP.UTF_8;

        try {
            HttpClient httpclient;

            if (name == null) { //不需要维持连接
                httpclient = createHttpClient();
            } else if (sessionMap.containsKey(name)) {
                httpclient = sessionMap.get(name);
            } else {
                httpclient = createHttpClient();
                sessionMap.put(name, httpclient);
            }

            HttpPost httpost = new HttpPost(url);

            if (content != null) {
                httpost.setEntity(new StringEntity(content,charset));
            }
            String requstStr;
//            synchronized(httpclient){
	            HttpResponse response = httpclient.execute(httpost);
	
	            HttpEntity entity1 = response.getEntity();
	            requstStr = EntityUtils.toString(entity1, charset);
	            entity1.consumeContent();
//            }
            return requstStr;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	destroy("hhh");
        }

        return null;
    }


    public static void destroy(String name) {
        HttpClient httpclient = sessionMap.get(name);
        if(httpclient!=null)
        httpclient.getConnectionManager().shutdown();
        sessionMap.remove(name);
    }

/*    private static HttpClient createHttpClient() {
    	
    	
        DefaultHttpClient httpclient = new DefaultHttpClient();
        System.getProperties().setProperty("httpclient.useragent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

        try {

            httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException,
                        IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }

            });
            httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30000);
            httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT,60000);
            httpclient.addResponseInterceptor(new HttpResponseInterceptor() {

                public void process(
                        final HttpResponse response,
                        final HttpContext context) throws HttpException,
                        IOException {
                    HttpEntity entity = response.getEntity();
                    Header ceheader = entity.getContentEncoding();
                    if (ceheader != null) {
                        HeaderElement[] codecs = ceheader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(
                                        new GzipDecompressingEntity(response.
                                        getEntity()));
                                return;
                            }
                        }
                    }
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpclient;

    }*/

    
	public static synchronized HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		ConnManagerParams.setTimeout(params, 5000);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);
	
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		DefaultHttpClient customHttpClient = new DefaultHttpClient(conMgr,
				params);
		
		customHttpClient.addRequestInterceptor(new HttpRequestInterceptor() {

             public void process(
                     final HttpRequest request,
                     final HttpContext context) throws HttpException,
                     IOException {
                 if (!request.containsHeader("Accept-Encoding")) {
                     request.addHeader("Accept-Encoding", "gzip");
                 }
             }

         });
         
		customHttpClient.addResponseInterceptor(new HttpResponseInterceptor() {

             public void process(
                     final HttpResponse response,
                     final HttpContext context) throws HttpException,
                     IOException {
                 HttpEntity entity = response.getEntity();
                 Header ceheader = entity.getContentEncoding();
                 if (ceheader != null) {
                     HeaderElement[] codecs = ceheader.getElements();
                     for (int i = 0; i < codecs.length; i++) {
                         if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                             response.setEntity(
                                     new GzipDecompressingEntity(response.
                                     getEntity()));
                             return;
                         }
                     }
                 }
             }

         });
		
/*		switch (checkNetworkType(EdjApp.getInstance())) {
		case TYPE_CT_WAP: {
			// 通过代理解决中国移动联通GPRS中wap无法访问的问题
			HttpHost proxy = new HttpHost("10.0.0.200", 80, "http");
			customHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			Log.v("tag","当前网络类型为cm_cu_wap,设置代理10.0.0.200访问www");
		}
			break;
		case TYPE_CM_CU_WAP: {
			// 通过代理解决中国移动联通GPRS中wap无法访问的问题
			HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
			customHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			Log.v("tag","当前网络类型为cm_cu_wap,设置代理10.0.0.172访问www");
		}
			break;
		}*/
		
		return customHttpClient;
	}
    static class GzipDecompressingEntity extends HttpEntityWrapper {

        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent() throws IOException,
                IllegalStateException {

            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();

            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }

    }
    
    
    
    /***
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 * 
	 * */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isAvailable()) {
				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				Log.i("", "=====================>无网络");
				return TYPE_NET_WORK_DISABLED;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = networkInfo.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					Log.i("", "=====================>wifi网络");
					return TYPE_OTHER_NET;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！
					final Cursor c = mContext.getContentResolver().query(
							PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c
								.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							Log.i(
									"",
									"=====================>代理："
											+ c.getString(c
													.getColumnIndex("proxy")));
							if (user.startsWith(CTWAP)) {
								Log.i("", "=====================>电信wap网络");
								return TYPE_CT_WAP;
							}
						}
					}
					c.close();

					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断
					String netMode = networkInfo.getExtraInfo();
					Log.i("", "netMode ================== " + netMode);
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
								|| netMode.equals(UNIWAP)) {
							Log.i("", "=====================>移动联通wap网络");
							return TYPE_CM_CU_WAP;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return TYPE_OTHER_NET;
		}
		return TYPE_OTHER_NET;
	}
    }

