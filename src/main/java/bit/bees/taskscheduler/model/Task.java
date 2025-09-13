package bit.bees.taskscheduler.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class Task {

    private Long id;
    private String name;
    private String description;

    private TaskType type;
    private TaskStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime lastRun;
    private LocalDateTime nextRun;

    private boolean active;

    @Getter
    public enum TaskType {
        FIXED_RATE("Fixed Rate"),
        FIXED_DELAY("Fixed Delay"),
        CRON("Cron Expression");

        private final String displayName;

        TaskType(String displayName) {
            this.displayName = displayName;
        }
    }

    @Getter
    public enum TaskStatus {
        ACTIVE("Active"),
        PAUSED("Paused"),
        ERROR("Error");

        private final String displayName;

        TaskStatus(String displayName) {
            this.displayName = displayName;
        }
    }
}
