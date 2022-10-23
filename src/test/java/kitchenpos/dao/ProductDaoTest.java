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
        Product product = new Product();
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(1000));

        // when
        Product savedProduct = productDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Product product = new Product();
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(1000));
        Product savedProduct = productDao.save(product);

        // when
        Optional<Product> foundProduct = productDao.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).isPresent();
    }

    @Test
    void findAll() {
        // given
        Product productA = new Product();
        productA.setName("상품A");
        productA.setPrice(BigDecimal.valueOf(1000));
        productDao.save(productA);

        Product productB = new Product();
        productB.setName("상품B");
        productB.setPrice(BigDecimal.valueOf(2000));
        productDao.save(productB);

        // when
        List<Product> products = productDao.findAll();

        // then
        assertThat(products).hasSize(6 + 2);
    }
}
