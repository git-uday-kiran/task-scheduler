package bit.bees.taskscheduler.init.tasks;

import bit.bees.taskscheduler.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class Tasks implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final TaskScheduler taskScheduler;
    private final TaskSchedulerService taskSchedulerService;

    private final Logger log = LoggerFactory.getLogger(Tasks.class);

    public void autoShutdown(long seconds) {
        long startTime = System.currentTimeMillis();

        var scheduleTime = Instant.ofEpochMilli(startTime + TimeUnit.SECONDS.toMillis(seconds));
        Runnable shutdownTask = () -> {
            log.info("🔕 Elapsed {} seconds, shutting down gracefully...", seconds);
            Runtime.getRuntime().addShutdownHook(new Thread(taskSchedulerService::printTaskExecutionHistory));
            SpringApplication.exit(applicationContext);
        };
        log.info("Auto shutdown in {} seconds", seconds);
        taskScheduler.schedule(shutdownTask, scheduleTime);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
