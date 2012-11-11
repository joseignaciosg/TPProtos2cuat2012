package model.configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import model.util.IOUtil;
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
		values.clear();
		Scanner scanner = new Scanner(IOUtil.getStream(path));
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
	}
}
