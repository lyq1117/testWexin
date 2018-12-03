package entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import service.WexinService;

public class Menu {

	private List<Button> button = new ArrayList<Button>();

	public List<Button> getButton() {
		return button;
	}
	
	public void setButton(List<Button> button) {
		this.button = button;
	}
	
	/**
	 * main方法中构建菜单模型
	 * @param args
	 */
	@Test
	public void testCreateMenu() {
		Menu menu = new Menu();

		// 带二级按钮的一级菜单sb
		SubButton sb = new SubButton();
		sb.setName("生活");
		menu.getButton().add(sb);
		
		// 二级点击按钮11(新闻头条)
		ClickButton cb11 = new ClickButton();
		cb11.setName("新闻头条");
		cb11.setKey("11");
		sb.getSub_button().add(cb11);
		
		// 二级点击按钮12(历史上的今天)
		ClickButton cb12 = new ClickButton();
		cb12.setName("历史上的今天");
		cb12.setKey("12");
		sb.getSub_button().add(cb12);
		
		// 带二级按钮的一级菜单sb2
		SubButton sb2 = new SubButton();
		sb2.setName("八卦");
		menu.getButton().add(sb2);
		
		//二级点击按钮cb21(星座运势)
		ClickButton cb21 = new ClickButton();
		cb21.setName("成语解析");
		cb21.setKey("21");
		sb2.getSub_button().add(cb21);
		
		// 二级点击按钮cb22(QQ号测吉凶)
		ClickButton cb22 = new ClickButton();
		cb22.setName("QQ号测吉凶");
		cb22.setKey("22");
		sb2.getSub_button().add(cb22);
		
		// 二级点击按钮cb22(周公解梦)
		ClickButton cb23 = new ClickButton();
		cb23.setName("周公解梦");
		cb23.setKey("23");
		sb2.getSub_button().add(cb23);
		
		// 带二级按钮的一级菜单sb
		SubButton sb3 = new SubButton();
		sb3.setName("小工具");
		menu.getButton().add(sb3);
		
		// 二级点击按钮cb31(手写文字识别)
		ClickButton cb31 = new ClickButton();
		cb31.setName("文字识别");
		cb31.setKey("31");
		sb3.getSub_button().add(cb31);
		
		// 创建自定义菜单
		WexinService.createMenu(menu);
		
		// 删除自定义菜单
		// WexinService.deleteMenu();
		
	}
	
}
