package kitchenpos.common.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;

@JdbcTest
public abstract class RepositoryTest {

    @Autowired
    protected DataSource dataSource;
}
