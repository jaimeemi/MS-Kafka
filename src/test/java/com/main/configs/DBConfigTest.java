package com.main.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConfigTest {

    @Test
    @DisplayName("h2DataSource - debe crear DataSource H2")
    void h2DataSource() {
        DBConfig config = new DBConfig();

        DataSource dataSource = config.h2DataSource();

        assertNotNull(dataSource);
    }

    @Test
    @DisplayName("h2DataSource - debe configurar base de datos correctamente")
    void h2DataSourceConfiguration() throws SQLException {
        DBConfig config = new DBConfig();

        DataSource dataSource = config.h2DataSource();

        assertNotNull(dataSource);
        assertNotNull(dataSource.getConnection());
    }
}
