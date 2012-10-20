package service.command.impl.stats;


public class UserHistogram {
	
	private String user;
	private int transferedBytes;
	private int numberOfAccesses;
	private int numberOfReadMail;
	private int numberOfDeletedMail;
	
	public UserHistogram(String user) {
		this.user = user;
	}
	
	public int getTransferedBytes() {
		return transferedBytes;
	}
	
	public void incrementTransferedBytes(int transferedBytes) {
		this.transferedBytes+= transferedBytes;
	}
	
	public int getNumberOfAccesses() {
		return numberOfAccesses;
	}
	
	public void incrementNumberOfAccesses(){
		numberOfAccesses++;
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
		sb.append("Accesses: " + getNumberOfAccesses() + "\r\n");
		sb.append("Tranfered bytes: " + getTransferedBytes() + "\r\n");
		sb.append("Read mails: " + getNumberOfReadMail() + "\r\n");
		sb.append("Deleted mails: " + getNumberOfDeletedMail() + "\r\n");
		return sb.toString();
	}

}
