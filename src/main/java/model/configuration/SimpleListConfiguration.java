package model.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import model.util.StringUtil;

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
				String line = scanner.nextLine();
				if (!StringUtil.empty(line) && !StringUtil.empty(line.trim())) {
					String trimmed = line.trim();
					if (!trimmed.startsWith("#")) {
						values.add(trimmed);
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {

		}
	}

}
