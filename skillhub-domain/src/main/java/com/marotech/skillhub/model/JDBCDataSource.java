package com.marotech.skillhub.model;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "jdbc_data_source")
public class JDBCDataSource extends BaseEntity {

    @Column(nullable = false,length = 32)
    private String name;
    @Column(nullable = false, length = 32)
    private String schemaName;
    @Column(nullable = false)
    private String jdbcUrl;
    @Column(nullable = false, length = 32)
    private String username;
    @Column(nullable = false, length = 128)
    private String password;
    @Column(nullable = false, length = 72)
    private String driverClassName;
    @Column(length = 2)
    private boolean cachePrepStatements = true;
    @Column(length = 8)
    private int prepStmtCacheSize = 250;
    @Column(length = (8))
    private int prepStmtCacheSqlLimit = 2048;

    public HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.addDataSourceProperty("cachePrepStmts", cachePrepStatements);
        config.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        HikariDataSource dataSource = new HikariDataSource(config);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setPassword(password);
        return dataSource;
    }
}