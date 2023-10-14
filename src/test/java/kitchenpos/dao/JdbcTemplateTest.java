package kitchenpos.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;

@JdbcTest
public abstract class JdbcTemplateTest {

    @Autowired
    protected DataSource dataSource;

}
