package kitchenpos.dao;

import static kitchenpos.support.fixtures.ProductFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.ProductFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.ProductFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.ProductFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class ProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("Product를 저장한다.")
    void save() {
        Product actual = productDao.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));

        Product expected = productDao.findById(actual.getId()).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("모든 Product를 조회한다.")
    void findAll() {
        Product product1 = productDao.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productDao.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        List<Product> products = productDao.findAll();
        assertThat(products).contains(product1, product2);
    }
}
