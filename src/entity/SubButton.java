package entity;

import java.util.ArrayList;
import java.util.List;

public class SubButton extends Button {

	private List<Button> sub_button = new ArrayList<Button>();

	public List<Button> getSub_button() {
		return sub_button;
	}
	
	public void setSub_button(List<Button> sub_button) {
		this.sub_button = sub_button;
	}
}
