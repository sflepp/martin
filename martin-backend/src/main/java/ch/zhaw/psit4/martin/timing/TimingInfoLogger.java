package ch.zhaw.psit4.martin.timing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.pluginlib.PluginLibrary;

public class TimingInfoLogger {
	private List<TimingInfo> timingInfoLog = new ArrayList<>();
	private static final int MAX_AVAILABLE = 1;
	private static final Semaphore pool = new Semaphore(MAX_AVAILABLE, true);

	private final Log LOG = LogFactory.getLog(PluginLibrary.class);

	public void logStart(String label) {
		TimingInfo timingInfo = new TimingInfo();
		timingInfo.setLabel(label);
		timingInfo.setStartTime(new Date());
		timingInfoLog.add(timingInfo);
	}

	public List<TimingInfo> logEnd(String label) {
		Optional<TimingInfo> timingInfo = timingInfoLog.stream()
				.filter(p -> p.getLabel().equals(label) && p.getEndTime() == null).findFirst();

		if (timingInfo.isPresent()) {
			timingInfo.get().setEndTime(new Date());
		}

		return timingInfoLog;
	}

	public void startLogging() {
		try {
			this.logStart("Waiting in queue");
			pool.acquire();
			this.logEnd("Waiting in queue");
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public List<TimingInfo> stopLogging() {
		List<TimingInfo> timingInfoLogCopy = new ArrayList<>(this.timingInfoLog);
		this.timingInfoLog = new ArrayList<>();

		pool.release();

		return timingInfoLogCopy;
	}

}
