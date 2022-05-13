// package ru.tyumentsev.remember_the_pills.configuration;

// import javax.sql.DataSource;

// import com.zaxxer.hikari.HikariDataSource;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// public class DefaultDatabaseConfig {
//     protected DataSource hikariDataSource(String tag, DbConfig.SpringDataJdbcProperties properties) {
//         log.info("[{}] настройки БД: [{}]", tag, properties.toString());

//         HikariDataSource ds = new HikariDataSource();
//         ds.setJdbcUrl(properties.getUrl());
//         ds.setDriverClassName(properties.getDriver());
//         ds.setUsername(properties.getUser());
//         ds.setPassword(properties.getPassword());
//         ds.setMaximumPoolSize(Integer.parseInt(properties.getPoolSize()));
//         return ds;
//     }
// }
