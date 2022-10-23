package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductDaoTest extends DaoTest {

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
