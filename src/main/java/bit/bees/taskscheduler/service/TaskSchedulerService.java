package bit.bees.taskscheduler.service;

import bit.bees.taskscheduler.model.TaskExecution;
import bit.bees.taskscheduler.util.ThroatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Comparator.comparing;

/**
 * Task Scheduler, schedules tasks every 10 seconds
 * Prints timestamp every 10 seconds
 * Shows execution counter
 * Auto shutdowns after 60 seconds
 */

@Service
public class TaskSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(TaskSchedulerService.class);
    private final ConcurrentLinkedQueue<TaskExecution> executionHistory = new ConcurrentLinkedQueue<>();
    private final AtomicLong healthCheckCounter = new AtomicLong(0);
    private final AtomicLong cleanupCount = new AtomicLong(0);
    private final AtomicLong reportCount = new AtomicLong(0);


    @Scheduled(fixedRate = 5_000)
    public void systemHealthCheck() {
        long startTime = System.currentTimeMillis();
        try {
            ThroatUtils.smallThroat();
            long count = healthCheckCounter.incrementAndGet();
            long duration = System.currentTimeMillis() - startTime;

            var taskExecution = createTaskExecution("System Health Check", "FIXED_RATE", "SUCCESS", startTime, duration);
            addTaskExecution(taskExecution);
            log.info("✅ System Health Check #{} completed in {} ms - Status: Healthy", count, duration);

        } catch (Exception exception) {
            long duration = System.currentTimeMillis() - startTime;
            var taskExecution = createTaskExecution("System Health Check", "FIXED_RATE", "ERROR", startTime, duration);
            addTaskExecution(taskExecution);
            log.error("❌ Health Check failed", exception);
        }
    }

    @Scheduled(cron = "*/1 * * * * *") // Every minute for demo purposes
    public void generateDailReport() {
        long startTime = System.currentTimeMillis();
        try {
            ThroatUtils.mediumThroat();
            long count = reportCount.incrementAndGet();
            long duration = System.currentTimeMillis() - startTime;

            var taskExecution = createTaskExecution("Generate Daily Report", "CRON", "SUCCESS", startTime, duration);
            addTaskExecution(taskExecution);
            log.info("📈 Generate Daily Report #{} completed in {} ms - Analytics updated", count, duration);

        } catch (Exception exception) {
            long duration = System.currentTimeMillis() - startTime;
            var taskExecution = createTaskExecution("Generate Daily Report", "CRON", "ERROR", startTime, duration);
            addTaskExecution(taskExecution);
            log.error("❌ Generate Daily Report failed", exception);
        }
    }

    @Scheduled(fixedDelay = 15_000)
    public void performCleanup() {
        long startTime = System.currentTimeMillis();
        try {
            ThroatUtils.largeThroat();
            long count = cleanupCount.incrementAndGet();
            long duration = System.currentTimeMillis() - startTime;

            var taskExecution = createTaskExecution("System Cleanup", "FIXED_DELAY", "SUCCESS", startTime, duration);
            addTaskExecution(taskExecution);
            log.info("🧹 Cleanup task #{} completed in {} ms - Temporary files cleaned", count, duration);

        } catch (Exception exception) {
            long duration = System.currentTimeMillis() - startTime;
            var taskExecution = createTaskExecution("System Cleanup", "FIXED_DELAY", "ERROR", startTime, duration);
            addTaskExecution(taskExecution);
            log.error("❌ Cleanup task failed", exception);
        }
    }

    private static TaskExecution createTaskExecution(String taskName, String executionType, String status, long startTime, long duration) {
        return new TaskExecution(
                taskName,
                executionType,
                status,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneOffset.UTC),
                Duration.ofMillis(duration)
        );
    }

    private void addTaskExecution(TaskExecution taskExecution) {
        executionHistory.offer(taskExecution);
    }

    public Collection<TaskExecution> getExecutionHistory() {
        return Collections.unmodifiableCollection(executionHistory);
    }

    public long getHealthCheckCounter() {
        return healthCheckCounter.get();
    }

    public long getCleanupCount() {
        return cleanupCount.get();
    }

    public long getReportCount() {
        return reportCount.get();
    }

    public void printTaskExecutionHistory() {
        System.out.println("Task Executions: ");
        System.out.println("------------------------------------------------------------------------");
        getExecutionHistory().stream()
                .sorted(comparing(TaskExecution::getTaskName)
                        .thenComparing(TaskExecution::getExecutionType)
                        .thenComparing(TaskExecution::getStartTime))
                .forEach(System.out::println);
        System.out.println("------------------------------------------------------------------------");
    }
}
