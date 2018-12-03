package entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMessage extends Message{
	
	@XStreamAlias("Content")
	private String content;
	
	/**
	 * 无参构造
	 */
	public TextMessage() {}
	
	/**
	 * 有参构造 
	 * @param map 微信服务器发送来的xml映射成的map
	 * @param content 要发送给服务器的内容
	 */
	public TextMessage(Map<String, String> map, String content) {
		super(map);
		this.setMsgType("text");
		this.content = content; 
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	

}
