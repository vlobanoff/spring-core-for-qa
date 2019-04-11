package com.acme.banking.dbo.spring.service;

import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DemoIntegrationTestWithJavaSpringConfigTest.TestConfig.class)
@ActiveProfiles("test")
public class DemoIntegrationTestWithJavaSpringConfigTest {
    @Autowired private ReportingService reportingService;

    @Profile("test")
    @Configuration
    @ImportResource({"classpath:test-spring-context.xml", "classpath:spring-context.xml"})
    public static class TestConfig {
        @Bean
        public AccountRepository accountRepository() {
            Account accountStub = mock(Account.class);
            when(accountStub.getAmount()).thenReturn(10_000d);
            AccountRepository repoStub = mock(AccountRepository.class);
            when(repoStub.findById(anyLong())).thenReturn(Optional.of(accountStub));
            return repoStub;
        }
    }

    @Test
    public void shouldUseStubWithinSpringContext() {
        assertThat(reportingService.getUsdAmountFor(0L)).isEqualTo(300000d);
    }
}
