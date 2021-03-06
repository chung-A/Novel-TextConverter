package com.pier.tc.excel;

import java.io.Serializable;

public class ScenarioVO implements Serializable{
	
	private String command;
	private String arg1;
	private String arg2;
	private String arg3;
	private String arg4;
	private String arg5;
	private String arg6;
	private StringBuilder text;
	private String pageCtrl;
	
	public ScenarioVO() {
		text=new StringBuilder();
		text.setLength(0);
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getArg1() {
		return arg1;
	}
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
	public String getArg2() {
		return arg2;
	}
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
	public String getArg3() {
		return arg3;
	}
	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}
	public String getText() {
		return text.toString();
	}
	public void addText(String text) {
		this.text.append(text);
	}

	public String getArg4() {
		return arg4;
	}

	public void setArg4(String arg4) {
		this.arg4 = arg4;
	}

	public String getArg5() {
		return arg5;
	}

	public void setArg5(String arg5) {
		this.arg5 = arg5;
	}

	public String getArg6() {
		return arg6;
	}

	public void setArg6(String arg6) {
		this.arg6 = arg6;
	}

	public String getPageCtrl() {
		return pageCtrl;
	}

	public void setPageCtrl(String pageCtrl) {
		this.pageCtrl = pageCtrl;
	}
	
}
