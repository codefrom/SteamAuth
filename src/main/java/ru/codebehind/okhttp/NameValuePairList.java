package ru.codebehind.okhttp;

import java.util.ArrayList;

public class NameValuePairList extends ArrayList<NameValuePairList.NameValuePair> {

	private static final long serialVersionUID = -4916750140074030549L;

	public static class NameValuePair {
		public String name;
		public String value;
		
		public NameValuePair(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	public void add(String name, String value) {
		this.add(new NameValuePairList.NameValuePair(name, value));
	}
}
