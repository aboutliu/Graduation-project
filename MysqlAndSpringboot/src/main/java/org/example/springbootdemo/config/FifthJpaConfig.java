package org.example.springbootdemo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.example.springbootdemo.virtual.repository",
        entityManagerFactoryRef = "fifthEntityManagerFactory",
        transactionManagerRef = "fifthTransactionManager"
)
public class FifthJpaConfig {

    @Bean(name = "fifthEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fifthEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fifthDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("org.example.springbootdemo.virtual.model")
                .persistenceUnit("fifth")
                .build();
    }

    @Bean(name = "fifthTransactionManager")
    public PlatformTransactionManager fifthTransactionManager(
            @Qualifier("fifthEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
