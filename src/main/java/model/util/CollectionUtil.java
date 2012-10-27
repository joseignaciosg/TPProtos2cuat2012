package model.util;

import java.util.Collection;

public class CollectionUtil {

	public static boolean empty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static String join(String[] array, int startIndex) {
		String result = "";
		for (int i = startIndex; i < array.length; i++) {
			String s = array[i];
			result += s;
		}
		return result;
	}

	public static String join(String[] array, String joint) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			result += array[i];
			if (i < array.length - 1) {
				result += joint;
			}
		}
		return result;
	}

	public static String join(Collection<?> collection, String joint) {
		String result = "";
		int i = 0;
		int size = collection.size();
		for (Object each : collection) {
			result += each.toString();
			if (i < size - 1) {
				result += joint;
			}
		}
		return result;
	}

	public static String[] trimAll(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
		return array;
	}
}
