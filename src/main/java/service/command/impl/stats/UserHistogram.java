package service.command.impl.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.LocalDate;


public class UserHistogram {
	
	private String user;
	private long transferedBytes;
	private Map<LocalDate, Integer> numberOfAccessesByDay; 
	private int numberOfReadMail;
	private int numberOfDeletedMail;
	
	public UserHistogram(String user) {
		this.user = user;
		numberOfAccessesByDay = new HashMap<LocalDate, Integer>();
	}
	
	public long getTransferedBytes() {
		return transferedBytes;
	}
	
	public void incrementTransferedBytes(long transferedBytes) {
		this.transferedBytes += transferedBytes;
	}
	
	public int getNumberOfAccesses(LocalDate date) {
		if(numberOfAccessesByDay.containsKey(date)){
			return numberOfAccessesByDay.get(date);
		}
		
		return 0;
			
	}
	
	public void incrementNumberOfAccesses(){
		LocalDate today = LocalDate.now();
		if(!numberOfAccessesByDay.containsKey(today)){
			numberOfAccessesByDay.put(today, 1);
		}else{
			int currentAccesses = numberOfAccessesByDay.get(today);
			currentAccesses++;
			numberOfAccessesByDay.put(today, currentAccesses);
		}
	}
	
	public int getNumberOfReadMail() {
		return numberOfReadMail;
	}

	public void incrementNumberOfReadMail(){
		numberOfReadMail++;
	}
	
	public int getNumberOfDeletedMail() {
		return numberOfDeletedMail;
	}

	public void incrementNumberOfDeletedMail(){
		numberOfDeletedMail++;
	}

	public String getPrettyFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("User: " + user + "\r\n");
		for(Entry<LocalDate, Integer> entry: getNumberOfAccessesMap().entrySet()){
			sb.append("Accesses ["+ entry.getKey() +"] : " + entry.getValue() + "\r\n");
		}
		sb.append("Tranfered bytes: " + getTransferedBytes() + "\r\n");
		sb.append("Read mails: " + getNumberOfReadMail() + "\r\n");
		sb.append("Deleted mails: " + getNumberOfDeletedMail() + "\r\n");
		return sb.toString();
	}

	private Map<LocalDate, Integer> getNumberOfAccessesMap() {
		return numberOfAccessesByDay;
	}

	public int getNumberOfAccessesToday() {
		if(numberOfAccessesByDay.containsKey(LocalDate.now())){
			return numberOfAccessesByDay.get(LocalDate.now());
		}
		
		return -1;
	}

}
