package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import entity.Article;
import entity.Message;
import entity.NewHistoryOfToday;
import entity.NewsMessage;
import entity.TextMessage;
import entity.TopNewsData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JuHeIntfService {
	
	//历史上的今天appkey
	private static String appKey = "f3a0001e078ca4bf0a0998c8d0817b26";
	private static String version = "1.0";
	
	//新闻头条appkey
	private static String appkey_TopNews = "fae545d73334dc3631e627254a028eca";
	
	//周公解梦appkey
	private static String appkey_SolveDream = "63425da486bd6859696d14d253f65716";
	
	//QQ号测吉凶appKey
	private static String appkey_qqGoodOrBad = "0cf2b4f71034a76d120cd89c6f4c0bc8";
	
	//成语词典appKey
	private static String appkey_DictionaryOfidioms = "19165fc1f63f46f0c9aea7626024e481";
	
	/**
	 * 发送get请求到指定url，不会乱码。WexinService里的get没有转码，可能出现乱码。
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		HttpURLConnection connection = null;
		BufferedReader br = null;
		StringBuilder result = new StringBuilder();
		InputStream is = null;
		try {
			URL urlObj = new URL(url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			is = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String str = null;
			while((str = br.readLine()) != null) {
				result.append(str);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				// 关闭资源
				br.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
	
	/**
	 * 历史上的今天的请求url
	 * @param month
	 * @param day
	 * @return
	 */
	public static String getUrlOfHistoryToday(String month, String day) {
		String url = "http://api.juheapi.com/japi/toh?";
		try {
			url += "key=" + URLEncoder.encode(appKey,"UTF-8") + "&month=" + month + "&day=" + day;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * 得到历史上的今天的所有事件
	 * @param month
	 * @param day
	 * @param method
	 * @return 返回一个所有事件组成的ArrayList
	 */
	public static ArrayList<NewHistoryOfToday> getResultOfHistoryToday(String month, String day, String method) {
		String url = getUrlOfHistoryToday(month, day);
		String result = get(url);
		String resultStr = result.toString();
		// 处理json，把所有事件封装到一个ArrayList中返回
		JSONObject jsonObj = JSONObject.fromObject(resultStr);
		JSONArray resultArray = jsonObj.getJSONArray("result");
		Iterator<JSONObject> iterator = resultArray.iterator();
		ArrayList<NewHistoryOfToday> list = new ArrayList<NewHistoryOfToday>();
		while(iterator.hasNext()) {
			JSONObject next = iterator.next();
			String des = next.getString("des");
			String id = next.getString("_id");
			String lunar = next.getString("lunar");
			String pic = next.getString("pic");
			String title = next.getString("title");
			String year = next.getString("year");
			NewHistoryOfToday historyEvent = new NewHistoryOfToday(day, des, id, lunar, month, pic, title, year);
			list.add(historyEvent);
		}
		return list;
	}
	
	/**
	 * 新闻头条的请求url
	 * @return
	 */
	public static String getUrlOfTopNews() {
		String url = "http://v.juhe.cn/toutiao/index?type=top&key=APPKEY";
		url = url.replace("APPKEY", appkey_TopNews);
		return url;
	}
	
	/**
	 * 得到新闻头条请求返回的结果
	 * @param map
	 * @return
	 */
	public static Message getResultOfTopNews(Map map) {
		String url = getUrlOfTopNews();
		String resultStr = get(url);
		// 处理resultStr乱码
		try {
			resultStr = new String(resultStr.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(resultStr);
		// 处理json
		JSONObject root = JSONObject.fromObject(resultStr);
		JSONObject result = root.getJSONObject("result");
		JSONArray dataArray = result.getJSONArray("data");
		// 新闻头条的列表
		ArrayList<TopNewsData> list = new ArrayList<TopNewsData>();
		Iterator<JSONObject> iterator = dataArray.iterator();
		while(iterator.hasNext()) {
			JSONObject item = iterator.next();
			String uniquekey = item.getString("uniquekey");
			String title = item.getString("title");
			String date = item.getString("date");
			String category = item.getString("category");
			String author_name = item.getString("author_name");
			String urll = item.getString("url");
			String thumbnail_pic_s = item.getString("thumbnail_pic_s");
//			String thumbnail_pic_s02 = item.getString("thumbnail_pic_s02");
//			String thumbnail_pic_s03 = item.getString("thumbnail_pic_s03");
			TopNewsData topNewsData = new TopNewsData(uniquekey, title, date, category, author_name, urll, thumbnail_pic_s);
			list.add(topNewsData);
		}
		// 随即一个数字，用户随机一条新闻头条，因为微信图文消息只能回复一条。
		Random random = new Random();
		int randomNum = random.nextInt(list.size());
		
		// 图文消息中的文章列表
		TopNewsData item = list.get(randomNum);
		ArrayList<Article> articles = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle(item.getTitle());
		article.setUrl(item.getUrl());
		article.setPicUrl(item.getThumbnail_pic_s());
		article.setDescription("");
		articles.add(article);
		NewsMessage newsMessage = new NewsMessage(map, articles);
		return newsMessage;
	}
	
	// http://v.juhe.cn/dream/query?key=63425da486bd6859696d14d253f65716&q=黄金&cid=&full=
	/**
	 * 得到周公解梦消息回复
	 * @param map
	 * @return
	 */
	public static TextMessage getResultOfSolveDream(Map map) {
		String url = "http://v.juhe.cn/dream/query?key=appKey&q=keyWord&cid=&full=";
		String kw = ((String)map.get("Content")).substring(2, ((String)map.get("Content")).length());
		// 梦境关键字需要utf-8 urlencode
		try {
			kw = URLEncoder.encode(kw, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url = url.replace("appKey", appkey_SolveDream).replace("keyWord", kw);
		String result = get(url);
		// 解析json
		JSONObject jsonObject = JSONObject.fromObject(result);
		if(jsonObject.getString("result") == null) {
			return new TextMessage(map, "没有相关梦境");
		}
		JSONArray resultArray = jsonObject.getJSONArray("result");
		Iterator<JSONObject> iterator = resultArray.iterator();
		StringBuilder finalResult = new StringBuilder();
		while(iterator.hasNext()){
			JSONObject item = iterator.next();
			String title = item.getString("title");
			String destination = item.getString("des");
			finalResult.append(title + "——");
			finalResult.append(destination + "\n");
		}
		
		return new TextMessage(map, finalResult.toString());
	}
	
	/**
	 * QQ号测吉凶
	 * @param map
	 * @return
	 */
	public static TextMessage getResultOfQqGoodOrBad(Map map) {
		String url = "http://japi.juhe.cn/qqevaluate/qq?key=appKey&qq=QQ";
		String content = (String) map.get("Content");
		String qqNumber = content.substring(2, content.length());
		url = url.replace("appKey", appkey_qqGoodOrBad).replace("QQ", qqNumber);
		String result = get(url);
		JSONObject jsonObject = JSONObject.fromObject(result);
		JSONObject resultJsonObj = jsonObject.getJSONObject("result");
		JSONObject dataJsonObj = resultJsonObj.getJSONObject("data");
		String conclusion = dataJsonObj.getString("conclusion");
		String analysis = dataJsonObj.getString("analysis");
		result = "    结论：" + conclusion + "\n" + "    分析：" + analysis;
		if(!"0".equals(jsonObject.getString("error_code"))){
			return new TextMessage(map, "QQ号格式不正确");
		}
		return new TextMessage(map, result);
	}
	
	/**
	 * 成语词典
	 * @param map
	 * @return
	 */
	public static TextMessage getResultOfDictionary(Map map) {
		String url = "http://v.juhe.cn/chengyu/query?key=appKey&word=Word";
		String content = (String) map.get("Content");
		String word = content.substring(2, content.length());
		try {
			word = URLEncoder.encode(word, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url = url.replace("appKey", appkey_DictionaryOfidioms).replace("Word", word);
		String result = get(url);
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject.isEmpty());
		JSONObject resultObj = jsonObject.getJSONObject("result");
		String bushou = resultObj.getString("bushou");
		String head = resultObj.getString("head");
		String pinyin = resultObj.getString("pinyin");
		String chengyujs = resultObj.getString("chengyujs");
		System.out.println("jieshi----" + chengyujs);
		String from_ = resultObj.getString("from_");
		String example = resultObj.getString("example");
		String yufa = resultObj.getString("yufa");
		String ciyujs = resultObj.getString("ciyujs");
		String yinzhengjs = resultObj.getString("yinzhengjs");
		StringBuilder yinzhengjsSb = new StringBuilder();
		JSONArray tongyiArray = resultObj.getJSONArray("tongyi");
		Iterator<String> iterator = tongyiArray.iterator();
		while(iterator.hasNext()) {
			String item = iterator.next();
			yinzhengjsSb.append(item + "\n");
		}
		String yinzhengStr = yinzhengjsSb.toString();
		StringBuilder finalStrSb = new StringBuilder();
		if(bushou != null && !"".equals(bushou))
			finalStrSb.append("部首：" + bushou + "\n");
		if(head != null && !"".equals(head))
			finalStrSb.append("首字：" + head + "\n");
		if(pinyin != null && !"".equals(pinyin))
			finalStrSb.append("拼音：" + pinyin + "\n");
		if(chengyujs != null && !"".equals(chengyujs))
			finalStrSb.append("成语解释：" + chengyujs + "\n");
		if(from_ != null && !"".equals(from_))
			finalStrSb.append("出自：" + from_ + "\n");
		if(example != null && !"".equals(example))
			finalStrSb.append("例子：" + example + "\n");
		if(yufa != null && !"".equals(yufa))
			finalStrSb.append("语法：" + yufa + "\n");
		if(ciyujs != null && !"".equals(ciyujs))
			finalStrSb.append("词语解释：" + ciyujs + "\n");
		if(yinzhengjs != null && !"".equals(yinzhengjs))
			finalStrSb.append("引证解释：" + yinzhengjs + "\n");
		return new TextMessage(map, finalStrSb.toString());
	}

}
