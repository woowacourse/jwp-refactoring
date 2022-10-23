package kitchenpos.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
public abstract class DaoTest {

    @Autowired
    protected DataSource dataSource;
}
