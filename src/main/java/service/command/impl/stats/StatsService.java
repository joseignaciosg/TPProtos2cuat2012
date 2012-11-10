package service.command.impl.stats;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import model.util.CollectionUtil;

public class StatsService {

	private static StatsService statsService = new StatsService(); 

	public static StatsService getInstace() {
		return statsService;
	}

	private AtomicInteger transferedBytes;
	private AtomicInteger totalNumberOfAccesses;
	private AtomicInteger numberOfReadMail;
	private AtomicInteger numberOfDeletedMail;
	private ConcurrentMap<String, UserHistogram> statsByUserMap;

	public StatsService() {
		reset();
	}

	private void reset() {
		transferedBytes = new AtomicInteger();
		totalNumberOfAccesses = new AtomicInteger();
		numberOfReadMail = new AtomicInteger();
		numberOfDeletedMail = new AtomicInteger();
		statsByUserMap = new ConcurrentHashMap<String, UserHistogram>();
	}

	public int incrementNumberOfAccesses(String userMail) {
		totalNumberOfAccesses.incrementAndGet();
		return incrementUserAccesses(userMail);
	}

	public void decrementNumberOfAccesses(String userMail) {
		totalNumberOfAccesses.decrementAndGet();
		decrementUserAccesses(userMail);
	}

	public void incrementTransferedBytes(long bytes, String userMail) {
		transferedBytes.addAndGet((int) bytes);
		incrementUserTransferedBytes(userMail, bytes);
	}

	public int getTransferedBytes() {
		return transferedBytes.get();
	}

	public UserHistogram getStatsByUser(String user) {
		return statsByUserMap.get(user);
	}

	private void incrementUserTransferedBytes(String user, long bytes) {
		UserHistogram uh = statsByUserMap.get(user);
		if (uh != null) {
			uh.incrementTransferedBytes(bytes);
		}
	}

	private void incrementUserReadMail(String user) {
		UserHistogram uh = statsByUserMap.get(user);
		if (uh != null) {
			uh.incrementNumberOfReadMail();
		}
	}

	public void incrementNumberOfReadMail(String userMail) {
		numberOfReadMail.incrementAndGet();
		incrementUserReadMail(userMail);
	}

	public int getNumberOfAccesses() {
		return totalNumberOfAccesses.get();
	}
	

	private synchronized int incrementUserAccesses(String user) {
		UserHistogram uh = statsByUserMap.get(user);
		if (uh == null) {
			uh = new UserHistogram(user);
			statsByUserMap.put(user, uh);
		}
		return uh.incrementNumberOfAccesses();
	}

	private void decrementUserAccesses(String user) {
		statsByUserMap.get(user).decrementNumberOfAccesses();
	}

	public int getNumberOfDeletedMail() {
		return numberOfDeletedMail.get();
	}
	
	public void incrementNumberOfDeletedMail(String userMail) {
		numberOfDeletedMail.incrementAndGet();
		UserHistogram uh = statsByUserMap.get(userMail);
		uh.incrementNumberOfDeletedMail();
	}

	public int getNumberOfReadMail() {
		return numberOfReadMail.get();
	}

	public String getPrettyFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("Total accesses: " + getNumberOfAccesses() + "\r\n");
		sb.append("Total tranfered bytes: " + getTransferedBytes() + "\r\n");
		sb.append("Total read mails: " + getNumberOfReadMail() + "\r\n");
		sb.append("Total deleted mails: " + getNumberOfDeletedMail() + "\r\n");
		return sb.toString();
	}

	public String getAllUserStats() {
		Collection<UserHistogram> userHistograms = statsByUserMap.values();
		Collection<String> prettyFormats = new LinkedList<String>();
		for (UserHistogram uh : userHistograms) {
			prettyFormats.add(uh.getPrettyFormat());
		}
		return CollectionUtil.join(prettyFormats, "\r\n");
	}

}
