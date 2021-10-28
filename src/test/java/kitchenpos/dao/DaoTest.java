package kitchenpos.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@JdbcTest
@Transactional
@ActiveProfiles("test")
public class DaoTest {
    @Autowired
    protected DataSource dataSource;
}
