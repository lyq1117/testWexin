package service;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.naming.spi.DirStateFactory.Result;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import Decoder.BASE64Encoder;
import entity.AccessTokenHolder;
import entity.Image;
import net.sf.json.JSONObject;
import util.Base64Util;
import util.FileUtil;
import util.HttpUtil;

/**
 * 百度接口service
 * @author Administrator
 *
 */
public class BaiduIntfService {
	
	private static AccessTokenHolder accessTokenHolder;
	private static final String apiKey = "MhqvITX075tYKv0l7EcnjGYc";
	private static final String secretKey = "ZIQqGgzGkT0fsVtX4S4ZvqiHbze8aeda";
	
	/**
	 * 返回AccessToken对象
	 */
	public static void getAccessTokenObject() {
		String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=ClientId&client_secret=ClientSecret";
		url = url.replace("ClientId", apiKey).replaceAll("ClientSecret", secretKey);
		String result = get(url);
		JSONObject jsonObj = JSONObject.fromObject(result);
		String access_token = jsonObj.getString("access_token");
		long expire_in = jsonObj.getLong("expires_in");
		accessTokenHolder = new AccessTokenHolder(access_token, expire_in);
	}
	
	/**
	 * 获取AccessToken
	 * @return
	 */
	public static String getAccessTokenHolder() {
		if(accessTokenHolder == null || accessTokenHolder.isExpired()) {
			getAccessTokenObject();
		}
		return accessTokenHolder.getToken();
	}
	
	/**
	 * 向服务器发送get请求
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		
		StringBuilder sb = null;
		BufferedReader br = null;
		try {
			URL urlObj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
			//connection.setRequestMethod("get");
			// 建立实际连接
			connection.connect();
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			sb = new StringBuilder();
			String str;
			while((str = br.readLine()) != null) {
				sb.append(str);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 文字识别
	 * @param url
	 * @return
	 */
	public static String recognize(String url) {
		// 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        try {
        	String imgStr = ImageToBase64ByOnline(url);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            String accessToken = getAccessTokenHolder();
            String result = HttpUtil.post(otherHost, accessToken, params);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	public static String ImageToBase64ByOnline(String imgURL) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			// 创建URL
			URL url = new URL(imgURL);
			byte[] by = new byte[10240];
			// 创建链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			InputStream is = conn.getInputStream();
			
			// 将内容读取内存中
			int len = -1;
			while ((len = is.read(by)) != -1) {
				data.write(by, 0, len);
			}
			// 关闭流
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data.toByteArray());
	}
	
}
