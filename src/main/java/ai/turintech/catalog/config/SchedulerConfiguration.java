package ai.turintech.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulerConfiguration {
    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.immediate();
    }
}