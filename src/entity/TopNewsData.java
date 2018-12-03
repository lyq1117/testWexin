package entity;

/**
 * 新闻头条中数据的类
 * @author Administrator
 *
 */
public class TopNewsData {
	
	private String uniquekey;
	private String title;
	private String date;
	private String category;
	private String author_name;
	private String url;
	private String thumbnail_pic_s;
//	private String thumbnail_pic_s02;
//	private String thumbnail_pic_s03;
	
	public TopNewsData() {
	}

	public TopNewsData(String uniquekey, String title, String date, String category, String author_name, String url,
			String thumbnail_pic_s) {
		super();
		this.uniquekey = uniquekey;
		this.title = title;
		this.date = date;
		this.category = category;
		this.author_name = author_name;
		this.url = url;
		this.thumbnail_pic_s = thumbnail_pic_s;
	}

	public String getUniquekey() {
		return uniquekey;
	}

	public void setUniquekey(String uniquekey) {
		this.uniquekey = uniquekey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail_pic_s() {
		return thumbnail_pic_s;
	}

	public void setThumbnail_pic_s(String thumbnail_pic_s) {
		this.thumbnail_pic_s = thumbnail_pic_s;
	}
}
