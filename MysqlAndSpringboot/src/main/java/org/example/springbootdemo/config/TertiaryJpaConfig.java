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
        basePackages = "org.example.springbootdemo.resource.repository",
        entityManagerFactoryRef = "tertiaryEntityManagerFactory",
        transactionManagerRef = "tertiaryTransactionManager"
)
public class TertiaryJpaConfig {

    @Bean(name = "tertiaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tertiaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tertiaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("org.example.springbootdemo.resource.model")
                .persistenceUnit("tertiary")
                .build();
    }

    @Bean(name = "tertiaryTransactionManager")
    public PlatformTransactionManager tertiaryTransactionManager(
            @Qualifier("tertiaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
