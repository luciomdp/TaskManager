package com.advenio.medere.emr.objects.audit;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class AuditConfiguration {

	@Bean(destroyMethod = "destroy")
	public ThreadLocalTargetSource threadLocalTenantStore() {
		ThreadLocalTargetSource result = new ThreadLocalTargetSource();
		result.setTargetBeanName("auditStore");
		return result;
	}

	@Primary
	@Bean(name = "proxiedThreadLocalTargetSource")
	public ProxyFactoryBean proxiedThreadLocalTargetSource(ThreadLocalTargetSource threadLocalTargetSource) {
		ProxyFactoryBean result = new ProxyFactoryBean();
		result.setTargetSource(threadLocalTargetSource);
		return result;
	}

	@Bean(name = "auditStore")
	@Scope(scopeName = "prototype")
	public AuditStore auditStore() {
		return new AuditStore();
	}
}
