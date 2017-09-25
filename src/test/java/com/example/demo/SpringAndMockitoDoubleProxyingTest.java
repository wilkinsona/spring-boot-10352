package com.example.demo;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

public class SpringAndMockitoDoubleProxyingTest {

	@Test
	public void verifyNoMoreInteractionsSucceedsWithDoubleProxy() {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DoubleProxyConfiguration.class)) {
			TransactionalBean bean = context.getBean(TransactionalBean.class);
			bean.doSomething();
			verify(bean).doSomething();
			verifyNoMoreInteractions(bean);
		}
	}

	@Test
	public void verifyNoMoreInteractionsSucceedsWithSingleProxy() {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BaseConfiguration.class)) {
			TransactionalBean bean = context.getBean(TransactionalBean.class);
			bean.doSomething();
			verify(bean).doSomething();
			verifyNoMoreInteractions(bean);
		}
	}

	@Configuration
	@EnableTransactionManagement(proxyTargetClass=true)
	@Import(BaseConfiguration.class)
	static class DoubleProxyConfiguration {

		@Bean
		public TransactionalBean transactionalBean() {
			return spy(new TransactionalBean());
		}

		@Bean
		public DataSource dataSource() {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		}

		@Bean
		public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}

	}

	@Configuration
	static class BaseConfiguration {

		@Bean
		public TransactionalBean transactionalBean() {
			return spy(new TransactionalBean());
		}

	}

	static class TransactionalBean {

		@Transactional
		public void doSomething() {

		}

	}

}
