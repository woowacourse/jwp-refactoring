package kitchenpos.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@JdbcTest
@Transactional
public class DaoTest {
    @Autowired
    protected DataSource dataSource;
}
