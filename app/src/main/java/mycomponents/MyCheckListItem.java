package mycomponents;

public class MyCheckListItem{
	public String Text;
	public Object Value;
	public boolean isSelected;

	public MyCheckListItem(String text,Object value,boolean isselected) {
		Text = text;
		Value = value;
		isSelected=isselected;
	}
}