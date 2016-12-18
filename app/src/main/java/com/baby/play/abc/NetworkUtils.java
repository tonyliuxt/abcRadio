package com.baby.play.abc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络相关工具类
 * 
 * @author dzt
 * @<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *                   权限
 * @time 2014.04.25
 * @version V1.0
 * 
 * @see DetectNetwork 判断网络是否可用
 * @see HttpGet 通过Get获取网页内容
 * @see getHtml 得到网页的内容，以字符串形式
 * @see readInputStream 把数据流转成byte数组
 * @see OpenWebpage 打开指定网页
 * @see getURLResponse 获取指定URI的字符串
 * @see getNetWorkBitmap 获取网络图片
 */
public class NetworkUtils {

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 *            上下文对象
	 * @return true网络可用，否则不可用
	 */
	public static Boolean DetectNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * 通过Get获取网页内容
	 * 
	 * @param url
	 *            地址只能传入.xml后缀
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String HttpGet(String url) throws ClientProtocolException,
			IOException {
		// 新建一个默认的连接
		DefaultHttpClient client = new DefaultHttpClient();
		// 新建一个Get方法
		HttpGet get = new HttpGet(url);
		// 得到网络的回应
		HttpResponse response = client.execute(get);
		// 获得的网页源代码（xml）
		String content = null;

		// 如果服务器响应的是OK的话！
		if (response.getStatusLine().getStatusCode() == 200) {
			// 以下是把网络数据分段读取下来的过程
			InputStream in = response.getEntity().getContent();
			byte[] data = new byte[1024];
			int length = 0;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			while ((length = in.read(data)) != -1) {
				bout.write(data, 0, length);
			}
			// 最后把字节流转为字符串 转换的编码为utf-8.
			content = new String(bout.toByteArray(), "utf-8");
		}
		// 返回得到的字符串 也就是网页源代码
		return content;
	}

	/**
	 * 得到网页的内容，以字符串形式
	 * 
	 * @param path
	 *            传入html路径
	 * @return
	 * @throws Exception
	 * @time 2014.04.25
	 */
	public static String getHtml(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);

		InputStream inStream = conn.getInputStream();// 通过输入流获取html数据
		byte[] data = readInputStream(inStream);// 得到html的二进制数据
		String html = new String(data, "utf-8");
		return html;
	}

	/**
	 * 把数据流转成byte数组
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 * @time 2014.04.25
	 */
	private static byte[] readInputStream(InputStream inStream)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 打开指定网页
	 * 
	 * @param context
	 *            上下文
	 * @param uri
	 *            指定的uri,如：http://www.baidu.com
	 * @time 2014.04.25
	 */
	public static void OpenWebpage(Context context, String uri) {
		Uri uriUrl = Uri.parse(uri);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		launchBrowser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(launchBrowser);
	}

	/**
	 * 获取指定URI的字符串
	 * 
	 * @param urlString
	 *            如：http://www.baidu.com/
	 * @return
	 * @date 2014.05.05
	 */
	public static String getURLResponse(String urlString) {
		HttpURLConnection conn = null; // 连接对象
		InputStream is = null;
		String resultData = "";
		try {
			URL url = new URL(urlString); // URL对象
			conn = (HttpURLConnection) url.openConnection(); // 使用URL打开一个链接
			conn.setDoInput(true); // 允许输入流，即允许下载
			conn.setDoOutput(true); // 允许输出流，即允许上传
			conn.setUseCaches(false); // 不使用缓冲
			conn.setRequestMethod("GET"); // 使用get请求
			is = conn.getInputStream(); // 获取输入流，此时才真正建立链接
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferReader = new BufferedReader(isr);
			String inputLine = "";
			while ((inputLine = bufferReader.readLine()) != null) {
				resultData += inputLine + "\n";
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return resultData;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param urlString
	 *            如：http://f.hiphotos.baidu.com/image/w%3D2048/sign=3
	 *            b06d28fc91349547e1eef6462769358
	 *            /d000baa1cd11728b22c9e62ccafcc3cec2fd2cd3.jpg
	 * @return
	 * @date 2014.05.05
	 */
	public Bitmap getNetWorkBitmap(String urlString) {
		URL imgUrl = null;
		Bitmap bitmap = null;
		try {
			imgUrl = new URL(urlString);
			// 使用HttpURLConnection打开连接
			HttpURLConnection urlConn = (HttpURLConnection) imgUrl
					.openConnection();
			urlConn.setDoInput(true);
			urlConn.connect();
			// 将得到的数据转化成InputStream
			InputStream is = urlConn.getInputStream();
			// 将InputStream转换成Bitmap
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
