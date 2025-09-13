package bit.bees.taskscheduler;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Task Scheduler, schedules tasks every 10 seconds
 * Prints timestamp every 10 seconds
 * Shows execution counter
 * Auto shutdowns after 60 seconds
 */

@Service
public class TaskSchedulerService implements ApplicationContextAware {

    private long startTime;
    private long executionCount = 0;
    private ApplicationContext applicationContext;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
    private final Logger log = LoggerFactory.getLogger(TaskSchedulerService.class);

    @PostConstruct
    public void postConstruct() {
        resolveStartTime();
        log.info("✅ Task Scheduler initialized at {}", getCurrentTimestamp());
        log.info("🗓 Will run for 60 seconds, executing every 10 seconds");
        log.info("⏰ Expected executions: 6\n");
    }

    @Scheduled(fixedRate = 10_000)
    public void executeHelloTask() {
        incrementExecutionCount();
        var elapsedTime = getElapsedTime();
        var timestamp = getCurrentTimestamp();
        String message = "Hello from Task Scheduler | Execution #%d | Timestamp: %s | Elapsed time: %ss".formatted(getExecutionCount(), timestamp, elapsedTime);
        log.info(message);

        if (getExecutionCount() == 1) {
            log.info("↪ This is your first scheduled task");
        } else if (getExecutionCount() == 6) {
            log.info("↪ Final execution - application will shutdown soon!");
        }
    }

    @Scheduled(fixedRate = 100, timeUnit = TimeUnit.MILLISECONDS)
    private void monitorElapsedTime() {
        resolveStartTime();
        long elapsedTime = getElapsedTime();
        if (elapsedTime >= 60) {
            log.info("🔕 Elapsed 60 seconds, shutting down gracefully...");
            SpringApplication.exit(applicationContext);
        }
    }

    private long getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public String getCurrentTimestamp() {
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

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
