package com.nguyen.i180.config;

import com.nguyen.i180.clazz.RoutingDataSource;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

@Configuration
@PropertySource("classpath:datasource-cfg.properties")
public class WebMvcConfig {

    private Environment env;

    public WebMvcConfig(@Autowired Environment environment) {
        this.env = environment;
    }

    @Autowired @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.nguyen.i180.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource dataSource(DataSource dataSourceEn, DataSource dataSourceVi) {
        RoutingDataSource routingDataSource = new RoutingDataSource(Locale.ENGLISH);

        HashMap<Locale, DataSource> map = new HashMap<>();
        map.put(new Locale("vi"), dataSourceVi);
        map.put(new Locale("en"), dataSourceEn);

        routingDataSource.initDataSources(map);
        return routingDataSource;
    }

    @Bean(name = "dataSourceEn")
    public DataSource dataSourceEn() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("en.spring.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("en.spring.datasource.url"));
        dataSource.setUsername(env.getProperty("en.spring.datasource.username"));
        dataSource.setPassword(env.getProperty("en.spring.datasource.password"));

        return dataSource;
    }

    @Bean(name = "dataSourceVi")
    public DataSource dataSourceVi() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(
                Objects.requireNonNull(env.getProperty("vi.spring.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("vi.spring.datasource.url"));
        dataSource.setUsername(env.getProperty("vi.spring.datasource.username"));
        dataSource.setPassword(env.getProperty("vi.spring.datasource.password"));

        return dataSource;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();

        txManager.setDataSource(dataSource);

        return txManager;
    }
}
