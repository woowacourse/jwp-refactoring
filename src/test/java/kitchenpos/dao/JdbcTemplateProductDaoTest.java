package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final Product product = jdbcTemplateProductDao.save(new Product("디노치킨", new BigDecimal(20000)));

        //then
        assertThat(jdbcTemplateProductDao.findById(product.getId())).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final List<Product> result = jdbcTemplateProductDao.findAll();

        //then
        assertThat(result).hasSize(6);
    }
}
