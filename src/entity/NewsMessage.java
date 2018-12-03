package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class NewsMessage extends Message {

	@XStreamAlias("ArticleCount")
	private String articleCount;
	
	@XStreamAlias("Articles")
	private List<Article> articles = new ArrayList<Article>();
	
	/**
	 * 无参构造
	 */
	public NewsMessage() {}
	
	/**
	 * 有参构造
	 * @param map 微信服务器得到的xml映射成的map
	 * @param articles 文章List
	 */
	public NewsMessage(Map<String, String> map, List articles) {
		super(map);
		this.setMsgType("news");
		this.articleCount = String.valueOf(articles.size());
		this.articles = articles;
	}

	public String getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(String articleCount) {
		this.articleCount = articleCount;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
}


