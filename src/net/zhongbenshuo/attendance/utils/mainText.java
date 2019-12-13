package net.zhongbenshuo.attendance.utils;

import java.util.Random;

public class mainText {
	public static void main(String[] args) {
		Random random = new Random();
		for(int i = 0;i<20;i++){
			int sizes = random.nextInt(10);
			System.out.println(sizes);
		}
	
	}
}
