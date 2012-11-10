package service.command.impl.stats;

import org.joda.time.LocalDate;

public class UserHistogram {

	private String user;
	private long transferedBytes;
	private int numberOfReadMail;
	private int numberOfDeletedMail;
	private int numberOfAccesses;
	private AccesByDate accesByDate;

	public UserHistogram(String user) {
		this.user = user;
		accesByDate = new AccesByDate();
	}

	public long getTransferedBytes() {
		return transferedBytes;
	}

	public void incrementTransferedBytes(long transferedBytes) {
		this.transferedBytes += transferedBytes;
	}
	
	public int getNumberOfAccesses() {
		return numberOfAccesses;
	}

	public int incrementNumberOfAccesses() {
		numberOfAccesses++;
		accesByDate.incrementAccess();
		return numberOfAccesses;
	}

	public int getNumberOfReadMail() {
		return numberOfReadMail;
	}

	public void incrementNumberOfReadMail() {
		numberOfReadMail++;
	}

	public int getNumberOfDeletedMail() {
		return numberOfDeletedMail;
	}

	public void incrementNumberOfDeletedMail() {
		numberOfDeletedMail++;
	}

	public int getAccesForToday() {
		return accesByDate.todayAccesses();
	}

	public String getPrettyFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("User: " + user + "\r\n");
		sb.append("Total accesess: " + numberOfAccesses + "\r\n");
		sb.append("Acces today: " + accesByDate.todayAccesses() + "\r\n");
		sb.append("Tranfered bytes: " + getTransferedBytes() + "\r\n");
		sb.append("Read mails: " + getNumberOfReadMail() + "\r\n");
		sb.append("Deleted mails: " + getNumberOfDeletedMail() + "\r\n");
		return sb.toString();
	}
	
	private static class AccesByDate {
		LocalDate lastDayOfAccess;
		int amount;

		void incrementAccess() {
			LocalDate today = new LocalDate();
			if (!today.equals(lastDayOfAccess)) {
				amount = 0;
				lastDayOfAccess = today;
			}
			amount++;
		}

		int todayAccesses() {
			return (new LocalDate().equals(lastDayOfAccess)) ? amount : 0;
		}
	}

}
