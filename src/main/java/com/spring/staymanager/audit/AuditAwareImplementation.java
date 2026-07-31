package com.spring.staymanager.audit;


import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditAwareImplementation implements AuditorAware<String> {

    @Override
    public java.util.Optional<String> getCurrentAuditor() {
        return Optional.of("SYSTEM");
    }
}
