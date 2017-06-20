package com.jemhs.project.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyFortunes {

	public static String getDailyFortune(){
		
		List<String> myList = new ArrayList<String>();
		myList.add("Today, you will have a good day...");
		myList.add("Today, you will have a bad day...");
		myList.add("Today, you will have a worst day...");
		myList.add("Today, you will have a pretty good day...");
		myList.add("Today, you will have a pretty bad day...");
		myList.add("Today, you will have a pretty worst day...");
		myList.add("Today, you will have a fucking day...");
		Random randomizer = new Random();
		String random = myList.get(randomizer.nextInt(myList.size()));
		
		return random;
	}
}
