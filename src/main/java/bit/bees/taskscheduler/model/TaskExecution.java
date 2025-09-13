package bit.bees.taskscheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskExecution {
    private String taskName;
    private String executionType;
    private String status;
    private LocalDateTime startTime;
    private Duration duration;
}
