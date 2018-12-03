package entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class ImageMessage extends Message {

	@XStreamAlias("Image")
	private Image image;
	
	/**
	 * 无参构造
	 */
	public ImageMessage() {
	}
	
	/**
	 * 有参构造
	 * @param map
	 * @param picUrl
	 * @param mediaId
	 * @param msgId
	 */
	public ImageMessage(Map<String, String> map, Image image) {
		super(map);
		this.image = image;
		this.setMsgType("image");
	}

	
	
}
