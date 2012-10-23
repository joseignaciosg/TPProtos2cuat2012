package model.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SimpleListConfiguration {

	private String path;
	
	public SimpleListConfiguration(String path) {
		this.path = path;
	}
	
	public Scanner createScanner() {
		try {
			return new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void update() {
		
	}
	
}
