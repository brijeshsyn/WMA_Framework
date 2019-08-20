package com.wma.framework.common;

import org.openqa.selenium.By;
import static com.wma.framework.common.ByType.*;
import static com.wma.framework.common.Frame.*;

public enum WinAppElements implements PageElement{

	LAYOUT(NAME, "Layout", DEFAULT),
	CONTROLS(NAME, "Controls", DEFAULT),
	BUTTON(XPATH,"//*[@Name='Button' and @ClassName='TextBlock']",DEFAULT),
	CLICKME(NAME,"Click Me",DEFAULT),
	PASSWORDBOX(NAME,"PasswordBox",DEFAULT),
	PASSWORDBOXTEXTBOX(CLASS_NAME,"PasswordBox",DEFAULT),
	CHECKBOX(NAME,"CheckBox",DEFAULT),
	;
	
	
	private final String EXPRESSION;
	private final ByType BY_TYPE;
	private final Frame FRAME;
	
	private WinAppElements(ByType byType, String expr, Frame frame) {
		this.BY_TYPE = byType;
		this.EXPRESSION = expr;
		this.FRAME = frame;		
	}
	
	@Override
	public By getBy(String... placeholders) {		
		return ByFactory.getBy(BY_TYPE, EXPRESSION, placeholders);
	}

	@Override
	public Frame getFrame() {
		return FRAME;
	}

	@Override
	public ByType getType() {
		return BY_TYPE;
	}

	@Override
	public String getExpression() {
		return EXPRESSION;
	}

}
