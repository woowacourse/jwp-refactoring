package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class ProductDaoTest {

    private ProductDao productDao;

    @Autowired
    public ProductDaoTest(ProductDao productDao) {
        this.productDao = productDao;
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
        int defaultSize = 6;
        assertThat(products).hasSize(defaultSize + 2);
    }
}
