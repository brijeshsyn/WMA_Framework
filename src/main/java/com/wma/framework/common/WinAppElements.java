package com.wma.framework.common;

import static com.wma.framework.common.ByType.CLASS_NAME;
import static com.wma.framework.common.ByType.NAME;
import static com.wma.framework.common.ByType.XPATH;

public enum WinAppElements implements PageElement{

	LAYOUT(NAME, "Layout"),
	CONTROLS(NAME, "Controls"),
	BUTTON(XPATH,"//*[@Name='Button' and @ClassName='TextBlock']"),
	CLICKME(NAME,"Click Me"),
	PASSWORDBOX(NAME,"PasswordBox"),
	PASSWORDBOXTEXTBOX(CLASS_NAME,"PasswordBox"),
	CHECKBOX(NAME,"CheckBox"),
	;
		
	private final String EXPRESSION;
	private final ByType BY_TYPE;
	
	private WinAppElements(ByType byType, String expr) {
		this.BY_TYPE = byType;
		this.EXPRESSION = expr;
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
