package bit.bees.taskscheduler.init.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitTasks implements ApplicationListener<ApplicationStartedEvent> {

    private final Tasks tasks;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        tasks.autoShutdown(60);
    }
}
