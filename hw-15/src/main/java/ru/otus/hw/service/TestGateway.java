package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Specification;
import ru.otus.hw.model.TestCase;

import java.util.Collection;

@MessagingGateway
public interface TestGateway {

    @Gateway(requestChannel = "specChannel", replyChannel = "testCaseChannel")
    Collection<TestCase> process(Specification specification);
}
