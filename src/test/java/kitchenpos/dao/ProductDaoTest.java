package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;

    @BeforeEach
    void setup() {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    void 제품을_저장할_수_있다() {
        // given
        final Product product = new Product("제품1", new BigDecimal(10000));

        // when
        final Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }
}
