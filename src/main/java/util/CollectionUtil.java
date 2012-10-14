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

	public static String[] splitAndTrim(String string, String separator) {
		String[] ans = string.trim().split(separator);
		for (int i = 0; i < ans.length; i++) {
			ans[i] = ans[i].trim();
		}
		return ans;
	}
}
