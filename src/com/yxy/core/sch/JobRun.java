package com.yxy.core.sch;

import java.util.Map;
import java.util.concurrent.DelayQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 延迟任务执行线程
 * 
 * @author YXY
 * @date 2015年9月10日 下午3:05:07
 */
public class JobRun implements Runnable {
	private Logger log = LoggerFactory.getLogger(getClass());

	private Trigger trigger;

	private Map<String, Trigger> runJobMap;
	private DelayQueue<Trigger> taskQueue;

	public JobRun(Trigger trigger, Map<String, Trigger> runJobMap,
			DelayQueue<Trigger> taskQueue) {
		this.trigger = trigger;
		this.runJobMap = runJobMap;
		this.taskQueue = taskQueue;
	}

	@Override
	public void run() {
		if (trigger.isComplete()) {
			runJobMap.remove(trigger.getName());
			return;
		}
		if (this.trigger.isCancel()) {
			return;
		}
		try {
			long start = System.currentTimeMillis();
			beforeFire(trigger, System.currentTimeMillis());
			fire();
			afterFire(trigger, start);
			long cost = System.currentTimeMillis() - start;
			String notice = String.format("任务:[%s]执行完成,耗时: %s ms",
					new Object[] { trigger.getName(), Long.valueOf(cost) });
			log.warn(notice);
			// 任务执行完成
			if (trigger.isComplete()) {
				runJobMap.remove(trigger.getName());
				return;
			}
			taskQueue.offer(trigger);
		} catch (Exception e) {
			log.error("Trigger Error, Trigger=" + trigger.getName(), e);
			if (!taskQueue.offer(trigger)) {
				log.warn("重新加入任务队列失败: Trigger=" + trigger.getName());
			}
		}
	}

	private void fire() {
		if (trigger.isCancel() || trigger.isComplete()) {
			return;
		}
		try {
			trigger.getTask().execute(trigger);
		} catch (Exception e) {
			log.error("Job.execute() error!", e);
		}

		// TODO 扩展 && (trigger.getListener() != null)
		// if (trigger.getTriggerCount() == 0) {
		// // trigger.getListener().startJob();
		// try {
		// trigger.getTask().execute(trigger);
		// } catch (Exception e) {
		// log.error("Job.execute() error!", e);
		// }
		// }
		// if ((trigger.getListener() != null) && (trigger.isComplete())) {
		// try {
		// trigger.getListener().endJob();
		// } catch (Exception e) {
		// log.error("Listener.endJob() error!", e);
		// }
		// }
	}

	private void beforeFire(Trigger trigger, long curr) {
		trigger.setPreTime(curr);
		trigger.updateNextTime(curr);
	}

	private void afterFire(Trigger trigger, long curr) {
		trigger.setTriggerCount(trigger.getTriggerCount() + 1);
		trigger.setSeqNum(Trigger.sequencer.getAndIncrement());
	}
}