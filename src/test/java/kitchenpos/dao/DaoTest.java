package kitchenpos.dao;

import javax.sql.DataSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJdbcTest
public abstract class DaoTest {

    @Autowired
    protected DataSource dataSource;
}
