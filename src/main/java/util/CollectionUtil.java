package util;

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
}
