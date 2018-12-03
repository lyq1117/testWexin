package entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public abstract class Message {
	
	@XStreamAlias("ToUserName")
	private String toUserName;
	
	@XStreamAlias("FromUserName")
	private String fromUserName;
	
	@XStreamAlias("CreateTime")
	private String createTime;
	
	@XStreamAlias("MsgType")
	private String msgType;
	
	public Message() {}
	
	public Message(Map<String, String> map) {
		this.toUserName = map.get("FromUserName");
		this.fromUserName = map.get("ToUserName");
		this.createTime = System.currentTimeMillis()/100 + "";
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
