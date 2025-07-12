package com.suaistuds.monitoringeqiupment;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditAwareImpl")
public class AuditingConfig {}
