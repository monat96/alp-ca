package com.kt.alpca.issue.application.listener;

import com.kt.alpca.issue.application.service.IssueService;
import com.kt.alpca.issue.domain.event.HealthCheckedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class IssueEventListener {
        private final IssueService issueService;

        @Bean
        public Consumer<HealthCheckedEvent> issueEvent() {
            return issueService::performHealthCheckedEvent;
        }


}
