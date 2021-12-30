package com.mime.minefront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	Properties properties = new Properties();

	public void saveConfiguration(String key, int value) {

		String path = "res/settings/config.xml";

		try {
			File file = new File(path);
			boolean exist = file.exists();
			if (!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			this.properties.setProperty(key, Integer.toString(value));
			this.properties.storeToXML(write, "Resolution");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			this.properties.loadFromXML(read);
			String width = this.properties.getProperty("width");
			String height = this.properties.getProperty("height");
			this.setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
		} catch (FileNotFoundException e) {
			this.saveConfiguration("width", 800);
			this.saveConfiguration("height", 600);
			this.loadConfiguration(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setResolution(int width, int height) {
		Display.width = width;
		Display.height = height;
	}
}
