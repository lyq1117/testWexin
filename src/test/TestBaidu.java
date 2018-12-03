package test;

import java.util.Random;

import org.junit.Test;

import service.BaiduIntfService;
import service.WexinService;

public class TestBaidu {
	
	/**
	 * 测试获取百度文字识别token
	 */
	@Test
	public void getToken() {
		String token = BaiduIntfService.getAccessTokenHolder();
		System.out.println("baidu token:" + token);
	}
	
	@Test
	public void test2() {
		int wid = 1440;
		int hei = 2029;
		double radio = 250/wid;
		System.out.println(radio);
	}

}
