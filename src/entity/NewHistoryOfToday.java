package entity;

public class NewHistoryOfToday {

	private String day;   // 日
	private String des;   // 新闻内容
	private String id;    //时间id
	private String lunar; // 农历时间
	private String month; // 月
	private String pic;   // 图片
	private String title; // 标题
	private String year;  // 年份
	public NewHistoryOfToday(String day, String des, String id, String lunar, String month, String pic, String title,
			String year) {
		super();
		this.day = day;
		this.des = des;
		this.id = id;
		this.lunar = lunar;
		this.month = month;
		this.pic = pic;
		this.title = title;
		this.year = year;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLunar() {
		return lunar;
	}
	public void setLunar(String lunar) {
		this.lunar = lunar;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
