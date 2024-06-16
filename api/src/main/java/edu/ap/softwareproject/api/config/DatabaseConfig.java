package edu.ap.softwareproject.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * A configuration class that uses an H2 database as a failsafe in case of no database.
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    String primaryDSUrl;

    @Value("${spring.datasource.username}")
    String primaryDSUsername;

    @Value("${spring.datasource.password}")
    String primaryDSPassword;

    @Value("${datasource.secondary.url}")
    String secondaryDSUrl;

    @Value("${datasource.secondary.platform}")
    String secondaryDSPlatform;

    @Value("${datasource.secondary.username}")
    String secondaryDSUsername;

    @Value("${datasource.secondary.password}")
    String secondaryDSPassword;

    @Primary
    @Bean
    public DataSource getDataSource(
            @Qualifier("mySQL") DataSourceProperties first,
            @Qualifier("h2") DataSourceProperties second) {
        second.setUrl(secondaryDSUrl);
        final DataSource firstDataSource = first.initializeDataSourceBuilder().build();
        final DataSource secondDataSource = second.initializeDataSourceBuilder().build();
        try {
            firstDataSource.getConnection();
            return firstDataSource;
        } catch (Exception e) {
            return secondDataSource;
        }
    }

    @Primary
    @Bean("mySQL")
    public DataSourceProperties primaryDataSource() {
        final DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(primaryDSUrl);
        dataSourceProperties.setUsername(primaryDSUsername);
        dataSourceProperties.setPassword(primaryDSPassword);
        return dataSourceProperties;
    }

    @Bean("h2")
    public DataSourceProperties secondaryDataSource() {
        final DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(secondaryDSUrl);
        dataSourceProperties.setDriverClassName(secondaryDSPlatform);
        dataSourceProperties.setUsername(secondaryDSUsername);
        dataSourceProperties.setPassword(secondaryDSPassword);
        return dataSourceProperties;
    }
}
