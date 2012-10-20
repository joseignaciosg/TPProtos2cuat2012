package model.util;

public class IntegerUtil {

	public String getHumanReadableSize(int sizeInBytes) {
		boolean si = true;
	    int unit = si ? 1000 : 1024;
	    if (sizeInBytes < unit) { 
	    	return sizeInBytes + " B";
	    }
	    int exp = (int) (Math.log(sizeInBytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", sizeInBytes / Math.pow(unit, exp), pre);
	}
}
