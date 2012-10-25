package model.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public class SimpleListConfiguration {

	private String path;
	private Collection<String> values;

	public SimpleListConfiguration(String path) {
		this.path = path;
		values = new LinkedList<String>();
		update();
	}

	public Collection<String> getValues() {
		return values;
	}

	public void update() {
		try {
			values.clear();
			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNextLine()) {
				values.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {

		}
	}

}
