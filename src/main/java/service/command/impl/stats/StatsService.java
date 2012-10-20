package service.command.impl.stats;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import model.util.CollectionUtil;

public class StatsService {

	private static StatsService statsService;

	public static synchronized StatsService getInstace() {
		if (statsService == null) {
			statsService = new StatsService();
		}
		return statsService;
	}

	private AtomicInteger transferedBytes;
	private AtomicInteger numberOfAccesses;
	private AtomicInteger numberOfReadMail;
	private AtomicInteger numberOfDeletedMail;
	private ConcurrentMap<String, UserHistogram> statsByUserMap;

	public StatsService() {
		reset();
	}

	private void reset() {
		transferedBytes = new AtomicInteger();
		numberOfAccesses = new AtomicInteger();
		numberOfReadMail = new AtomicInteger();
		numberOfDeletedMail = new AtomicInteger();
		statsByUserMap = new ConcurrentHashMap<String, UserHistogram>();
	}

	public void incrementNumberOfAccesses() {
		numberOfAccesses.incrementAndGet();
	}

	public void incrementTransferedBytes(int bytes) {
		transferedBytes.addAndGet(bytes);
	}

	public int getTransferedBytes() {
		return transferedBytes.get();
	}

	public UserHistogram getStatsByUser(String user) {
		return statsByUserMap.get(user);
	}

	public void incrementUserTransferedBytes(String user, int bytes) {
		UserHistogram uh = statsByUserMap.get(user);
		if (uh != null) {
			uh.incrementTransferedBytes(bytes);
		}
	}

	public void incrementUserReadMail(String user) {
		UserHistogram uh = statsByUserMap.get(user);
		if (uh != null) {
			uh.incrementNumberOfReadMail();
		}
	}

	public void incrementNumberOfReadMail() {
		numberOfReadMail.incrementAndGet();
	}

	public int getNumberOfAccesses() {
		return numberOfAccesses.get();
	}

	public void incrementUserDeletedMail(String user) {
		UserHistogram uh = statsByUserMap.get(user);
		if (uh != null) {
			uh.incrementNumberOfDeletedMail();
		}
	}

	public int getNumberOfDeletedMail() {
		return numberOfDeletedMail.get();
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
