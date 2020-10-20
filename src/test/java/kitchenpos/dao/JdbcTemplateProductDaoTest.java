package kitchenpos.dao;

import kitchenpos.domain.Product;
import kitchenpos.fixture.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql({"classpath:truncate.sql"})
class JdbcTemplateProductDaoTest extends TestFixture {

    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);

        jdbcTemplateProductDao.save(PRODUCT_1);
        jdbcTemplateProductDao.save(PRODUCT_2);
    }

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<Product> products = jdbcTemplateProductDao.findAll();

        assertAll(
            () -> assertThat(products).hasSize(2),
            () -> assertThat(products.get(0)).usingRecursiveComparison().isEqualTo(PRODUCT_1),
            () -> assertThat(products.get(1)).usingRecursiveComparison().isEqualTo(PRODUCT_2)
        );
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        Product product = jdbcTemplateProductDao.findById(PRODUCT_ID_1).get();

        assertThat(product).usingRecursiveComparison().isEqualTo(PRODUCT_1);
    }
}