package com.yxy.core;

public class Halt extends Thread {
	public void run() {
		System.exit(0);
	}
}
