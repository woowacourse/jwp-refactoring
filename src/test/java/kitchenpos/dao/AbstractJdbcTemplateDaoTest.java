package kitchenpos.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@JdbcTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
public abstract class AbstractJdbcTemplateDaoTest {

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void executeDDL() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("/db/migration/V1__Initialize_project_tables.sql"));
        databasePopulator.execute(dataSource);
    }

    protected void turnOffReferentialIntegrity() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("referential_integrity_off.sql"));
        databasePopulator.execute(dataSource);
    }

    protected void turnOnReferentialIntegrity() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("referential_integrity_on.sql"));
        databasePopulator.execute(dataSource);
    }
}
