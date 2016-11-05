package com.zdc.qq.bean;

public class IndexBean implements Comparable<IndexBean> {

	private String name;
	private char firstLetter;

	public IndexBean(String name, char firstLetter) {
		this.name = name;
		this.firstLetter = firstLetter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(char firstLetter) {
		this.firstLetter = firstLetter;
	}

	@Override
	public String toString() {
		return "IndexBean [name=" + name + ", firstLetter=" + firstLetter + "]";
	}

	@Override
	public int compareTo(IndexBean another) {
		return firstLetter - another.firstLetter;
	}
}
