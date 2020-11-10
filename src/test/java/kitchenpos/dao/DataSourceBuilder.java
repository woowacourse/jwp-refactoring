package kitchenpos.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class DataSourceBuilder {
    public static DataSource initializeDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:initialize.sql")
            .build();
    }

    public static DataSource deleteDataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:delete.sql")
            .build();
    }
}
