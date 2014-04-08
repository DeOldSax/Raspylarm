package utils;

import javafx.util.StringConverter;

public class CheckBoxStringConverter extends StringConverter<Boolean> {
	
	private final String ACTIVE_COLOR = "-fx-background-color: yellow"; 
	private final String UNACTIVE_COLOR = null; 

	@Override
	public Boolean fromString(String string) {
		if (ACTIVE_COLOR.equals(string)) {
			return true; 
		} else {
			return false; 
		}
	}

	@Override
	public String toString(Boolean value) {
		if (value) {
			return ACTIVE_COLOR;
		} else {
			return UNACTIVE_COLOR; 
		}
	}
}
