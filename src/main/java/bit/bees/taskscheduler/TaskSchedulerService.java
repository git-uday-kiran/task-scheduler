package bit.bees.taskscheduler;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Task Scheduler, schedules tasks every 10 seconds
 * Prints timestamp every 10 seconds
 * Shows execution counter
 * Auto shutdowns after 60 seconds
 */

@Service
public class TaskSchedulerService {

    private long executionCount = 0;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    private long startTime;

    private final Logger log = LoggerFactory.getLogger(TaskSchedulerService.class);

    @PostConstruct
    public void postConstruct() {
        resolveStartTime();
        log.info("✅ Task Scheduler initialized at {}", getCurrentTimeStamp());
        log.info("🗓 Will run for 60 seconds, executing every 10 seconds");
        log.info("⏰ Expected executions: 6\n");
    }

    @Scheduled(fixedRate = 10_000)
    public void executeHelloTask() {
        resolveStartTime();
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsedTime >= 60) {
            log.info("🔕 Elapsed 60 seconds, shutting down");
            System.exit(0);
        }

        incrementExecutionCount();
        var timeStamp = getCurrentTimeStamp();
        String message = "Hello from Task Scheduler | Execution #%d | Time: %s | Elapsed time: %s seconds".formatted(getExecutionCount(), timeStamp, elapsedTime);
        log.info(message);

        if (getExecutionCount() == 1) {
            log.info("↪ This is your first scheduled task");
        } else if (getExecutionCount() == 6) {
            log.info("↪ Final execution - application will shutdown soon!");
        }
    }

    public String getCurrentTimeStamp() {
        return LocalDateTime.now().format(formatter);
    }

    public long getExecutionCount() {
        return executionCount;
    }

    private synchronized void resolveStartTime() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }

    private synchronized void incrementExecutionCount() {
        executionCount++;
    }
}
