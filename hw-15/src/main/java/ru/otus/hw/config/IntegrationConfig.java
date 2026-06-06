package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.model.TestCase;
import ru.otus.hw.service.AutoTestGeneratorService;
import ru.otus.hw.service.TestCaseService;

@Slf4j
@Configuration
public class IntegrationConfig {
    @Bean
    public MessageChannelSpec<?, ?> specChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> testCaseChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow testFlow(TestCaseService testCaseService, AutoTestGeneratorService autoTestGeneratorService) {
        return IntegrationFlow.from(specChannel())
            .handle(testCaseService, "createTestCases")
            .split()
            .route(TestCase.class, tc -> tc.getPriority() >= 4 ? "high" : "normal",
                mapping -> mapping
                    .subFlowMapping("high", flow -> flow
                        .transform(TestCase.class, tc -> {
                            if (tc.isNeedAutotest()) {
                                return autoTestGeneratorService.generateAutotestsWithHighPriority(tc);
                            }
                            return tc;
                        }))
                    .subFlowMapping("normal", flow -> flow
                        .transform(TestCase.class, tc -> {
                            if (tc.isNeedAutotest()) {
                                return autoTestGeneratorService.generateAutotests(tc);
                            }
                            return tc;
                        })))
            .aggregate()
            .channel(testCaseChannel())
            .get();
    }
}
