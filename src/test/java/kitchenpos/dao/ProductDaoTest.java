package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductDaoTest {

    private ProductDao productDao;

    @Autowired
    public ProductDaoTest(DataSource dataSource) {
        this.productDao = BeanAssembler.createProductDao(dataSource);
    }

    @Test
    void save() {
        // given
        Product product = new Product("상품", BigDecimal.valueOf(1000));

        // when
        Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Product savedProduct = productDao.save(new Product("상품", BigDecimal.valueOf(1000)));

        // when
        Optional<Product> foundProduct = productDao.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).isPresent();
    }

    @Test
    void findAll() {
        // given
        productDao.save(new Product("상품A", BigDecimal.valueOf(1000)));
        productDao.save(new Product("상품B", BigDecimal.valueOf(2000)));

        // when
        List<Product> products = productDao.findAll();

        // then
        assertThat(products).hasSize(6 + 2);
    }
}
