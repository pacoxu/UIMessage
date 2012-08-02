package com.emc.xplore;

import java.io.IOException;

import com.emc.xplore.liberary.StringLiberary;

public class Test {
	public static void main(String[] args) throws IOException {
		StringLiberary sl = new StringLiberary();
		sl.createStringLiberary();
		sl.createLuceneDictionary();
	}
}
