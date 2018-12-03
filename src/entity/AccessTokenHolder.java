package entity;

public class AccessTokenHolder {

	private String accessToken;
	// expireIn存储过期时间，注意：这个过期时间不是微信服务器返回的过期时间
	// 而是根据当前时间+服务器返回的过期时间得到的过期时间终点
	private long expireIn;
	
	public AccessTokenHolder(String accessToken, long expireIn) {
		this.accessToken = accessToken;
		// 计算过期时间终点
		this.expireIn = System.currentTimeMillis() + expireIn*1000;
	}
	
	/**
	 * 判断当前token是否过期
	 * @return
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() > expireIn;
	}

	public String getToken() {
		return this.accessToken;
	}
	
}
