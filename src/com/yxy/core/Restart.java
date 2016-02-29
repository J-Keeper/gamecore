package com.yxy.core;

import java.io.File;
import java.io.IOException;

public class Restart extends Thread {
	String shell;

	public Restart(String shell) {
		this.shell = shell;
	}

	public void run() {
		try {
			String dir = System.getProperty("user.dir");
			ProcessBuilder pb = new ProcessBuilder(new String[] { this.shell });
			pb.directory(new File(dir));
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}