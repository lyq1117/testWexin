package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletInputStream;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import entity.Message;
import entity.NewHistoryOfToday;
import entity.NewsMessage;
import entity.SubButton;
import entity.AccessTokenHolder;
import entity.Article;
import entity.Button;
import entity.ClickButton;
import entity.Image;
import entity.ImageMessage;
import entity.Menu;
import entity.TextMessage;
import entity.ViewButton;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WexinService {
	
	public static final String TOKEN = "lin";
	public static final String APPID = "wx1658fa4b5e60b33a";
	public static final String APPSECRET = "f38db41283dbfcbf7eb6b1a592e476fc";
	public static AccessTokenHolder accessTokenHolder;
	
	public static boolean check(String signature,String timestamp, String nonce) {
		String[] strs = {TOKEN,timestamp,nonce};
		Arrays.sort(strs);
		String newStr = strs[0] + strs[1] + strs[2];
		String mySig = sha1(newStr);
		System.out.println("mySig:" + mySig);
		System.out.println("signature:" + signature);
		return mySig.equals(signature);
	}

	private static String sha1(String newStr) {
		
		try {
			//��ȡһ�����ܶ���
			MessageDigest md = MessageDigest.getInstance("sha1");
			//����
			byte[] bytes = newStr.getBytes();
			byte[] digest = md.digest(bytes);
			String digestedStr = byteToHexStr(digest);
			return digestedStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String byteToHexStr(byte[] digest) {
		char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(chars[(b>>4)&15]);
			sb.append(chars[b&15]);
		}
		return sb.toString();
	}
	
	/**
	 * 将servlet输入流转成xml
	 * @param is
	 * @return
	 */
	public static Map<String, String> parseXml(ServletInputStream is) {
		Map map = new HashMap();
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(is);
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			for (Element element : list) {
				String name = element.getName();
				String value = element.getStringValue();
				map.put(name, value);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 处理服务器发过来的客户请求
	 * @param map
	 * @return
	 */
	public static Message getResp(Map map) {
		String type = (String) map.get("MsgType");
		switch (type) {
		case "text":
			return dealTexMessage(map);
		case "image":
			return dealImageMessage(map);
		case "voice":
			return dealVoiceMessage(map);
		case "video":

			break;
		case "shortvideo":

			break;
		case "location":

			break;
		case "link":

			break;
		case "event":
			return dealEvent(map);
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * 处理图片消息
	 * @param map
	 * @return
	 */
	private static Message dealImageMessage(Map map) {
		String picUrl = (String) map.get("PicUrl");
		String mediaId = (String) map.get("MediaId");
		String msgId = (String) map.get("MsgId");
		Image image = new Image(mediaId);
		ImageMessage imgMessage = new ImageMessage(map, image);
		String result = BaiduIntfService.recognize(picUrl);
		System.out.println("result image=----" + result);
		// 遍历结果
		JSONObject jsonObj = JSONObject.fromObject(result);
		JSONArray array = jsonObj.getJSONArray("words_result");
		Iterator<JSONObject> iterator = array.iterator();
		StringBuilder word = new StringBuilder();
		while(iterator.hasNext()) {
			word.append(iterator.next().get("words"));
		}
		return new TextMessage(map, "识别结果是：" + word.toString());
	}

	/**
	 * 处理事件消息
	 * @param map
	 * @return
	 */
	private static Message dealEvent(Map map) {
		
		switch ((String)map.get("Event")) {
		case "CLICK":
			return dealMenu(map);
		case "subscribe":
			return new TextMessage(map, "欢迎关注");
		default:
			break;
		}
		return null;
	}

	/**
	 * 处理菜单点击事件
	 * @param map
	 * @return
	 */
	private static Message dealMenu(Map map) {
		String str = null;
		switch ((String)map.get("EventKey")) {
		case "11":{// 新闻头条
			return JuHeIntfService.getResultOfTopNews(map);
		}
		case "12":{// 历史上的今天
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DATE);
			System.out.println("月：" + month + "日：" + day);
			ArrayList<NewHistoryOfToday> list = JuHeIntfService.getResultOfHistoryToday(String.valueOf(month), String.valueOf(day), "GET");
			return transferList2NewsMessage(map, list);
		}
		case "21":{
			return new TextMessage(map, "输入成语XXXX，如：\n    成语三顾茅庐");
		}
			
		case "22":{// QQ号测吉凶
			return new TextMessage(map, "输入QQXXXX，如：\n    QQ12345678");			
		}
		case "23":{// 周公解梦
			return new TextMessage(map, "输入解梦XXXX，如：\n    解梦黄金");
		}
		case "31": {
			str = "文字识别\n    你可以发送手写文字图片。";
			return new TextMessage(map, str);
		}
			
		default:
			break;
		}
		return null;
	}

	private static Message dealVoiceMessage(Map map) {
		return new TextMessage(map, "您说的是：" + map.get("Recognition"));
	}

	/**
	 * 若客户端发送"图文"、"解梦XXXX"、"QQXXXX"处理成图文消息对象
	 * 否则处理成文本对象：您输入的是XXX
	 * @param map
	 * @return
	 */
	private static Message dealTexMessage(Map map) {
		Message textMessage = null;
		String content = (String) map.get("Content");
		if("图文".equals(map.get("Content"))) {
			//返回一个图文消息对象
			return contentTuWen(map);
		}
		if("解梦".equals(content.substring(0, 2))) {
			textMessage = JuHeIntfService.getResultOfSolveDream(map);
			return textMessage;
		}
		if("QQ".equals(content.substring(0, 2))) {
			textMessage = JuHeIntfService.getResultOfQqGoodOrBad(map);
			return textMessage;
		}
		if("成语".equals(content.substring(0,2))) {
			textMessage = JuHeIntfService.getResultOfDictionary(map);
			return textMessage;
		}
		//返回一个文本消息对象
		textMessage = new TextMessage(map, "您发送的信息是：" + (String)map.get("Content"));
		return textMessage;
	}

	/**
	 * 转换事件list成图文消息
	 * @param list
	 * @return
	 */
	private static Message transferList2NewsMessage(Map map, ArrayList<NewHistoryOfToday> list) {
		ArrayList<Article> articles = new ArrayList<Article>();
		// 图文消息只能回复一条，所以在众多历史上的今天事件中，随机一条回复
		Random random = new Random();
		int randomNum = random.nextInt(list.size());
		// 创建图文消息中的一篇文章
		NewHistoryOfToday item = list.get(randomNum);
		Article article = new Article();
		article.setTitle(item.getTitle()); // 标题
		article.setUrl(""); // 跳转地址
		article.setPicUrl(item.getPic()); // 图片地址
		article.setDescription(item.getDes()); // 详细内容
		articles.add(article);
		// 回复一个图文消息
		NewsMessage newsMessage = new NewsMessage(map, articles);
		return newsMessage;
	}

	/**
	 * 若map中的content是“图文”，返回一个图文消息对象
	 * @param map
	 * @return
	 */
	private static Message contentTuWen(Map map) {
		List<Article> articles = new ArrayList<Article>();
		
		Article article = new Article();
		article.setTitle("蜡笔小新...");
		article.setDescription("描述...");
		article.setPicUrl(
				"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542794838599&di=cf888c318c6e18915438d744c807a3a9&imgtype=0&src=http%3A%2F%2Fwww.17qq.com%2Fimg_qqtouxiang%2F81277604.jpeg"
				);
		article.setUrl("http://www.baidu.com");
		
		articles.add(article);
		return new NewsMessage(map, articles);
	}

	/**
	 * 将消息对象通过XStream转成需要返回给微信服务器的Xml
	 * @param message
	 * @return
	 */
	public static String mesParse2Xml(Message message) {
		// 创建一个XStream对象
		XStream stream = new XStream();
		// 需要处理注解的类
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(Article.class);
		stream.processAnnotations(ImageMessage.class);
		// 返回XStream处理成的Xml
		return stream.toXML(message);
	}
	
	/**
	 * 向指定地址发送一个get请求，会返回一个json数据包
	 * @param url
	 */
	public static String get(String url) {
		String result = "";
		InputStream is = null;
		try {
			// 把url字符串转成URL对象
			URL urlObj = new URL(url);
			// 创建连接
			URLConnection connection = urlObj.openConnection();
            // 建立实际连接
			connection.connect();
			// 获取输入流
			is = connection.getInputStream();
			StringBuilder sb = new StringBuilder();
			int len;
			byte[] b = new byte[1024];
			while((len=is.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 从远程获取token
	 */
	public static void getAccessTokenObject() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		url = url.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		System.out.println("url" + url);
		String result = get(url);
		System.out.println("result" + result);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println("isEmpty:" + jsonObject.isEmpty());
		String asStr = jsonObject.getString("access_token");
		int ei = jsonObject.getInt("expires_in");
		accessTokenHolder = new AccessTokenHolder(asStr, ei);
	}
	
	/**
	 * 获取token
	 * @return
	 */
	public static String getAccessTokenHolder() {
		if(accessTokenHolder == null || accessTokenHolder.isExpired()) {
			getAccessTokenObject();
		}
		return accessTokenHolder.getToken();
	}
	
	/**
	 * 发送一个post请求到微信服务器
	 * @param url
	 * @param data
	 * @return
	 */
	public static String post(String url, String data) {
		
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		OutputStream os = null;
		try {
			// 把url字符串转换成URL对象
			URL urlObj = new URL(url);
			// 建立连接
			URLConnection connection = urlObj.openConnection();
			connection.setDoOutput(true);
			// 获取输出流
			os = connection.getOutputStream();
			os.write(data.getBytes());
			// 读取响应中的数据
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = br.readLine()) != null) {
				result.append(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关流，释放资源
				br.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
		
	}
	
	/**
	 * 将菜单封装成json，和url一起传入post方法发生post请求
	 * @param menu
	 */
	public static void createMenu(Menu menu) {
		String data = JSONObject.fromObject(menu).toString();
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + getAccessTokenHolder();
		String result = post(url, data);
		System.out.println("result" + result);
	}
	
	/**
	 * 删除自定义菜单
	 */
	public static void deleteMenu() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + getAccessTokenHolder();
		String result = get(url);
		System.out.println(result);
	}
	
	/**
	 * 获取用户信息
	 * @param openId
	 * @return
	 */
	public String getUserInfo(String openId) {
		String at = getAccessTokenHolder();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url.replace("ACCESS_TOKEN", at).replace("OPENID", openId);
		return get(url);
	}
	

}
