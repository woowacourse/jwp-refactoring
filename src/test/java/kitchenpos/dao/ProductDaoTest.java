package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductDaoTest extends DaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void save() throws Exception {
        Product savedProduct = productDao.save(new Product("상품명", new BigDecimal(1000)));
        Product foundProduct = productDao.findById(savedProduct.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedProduct.getId()).isEqualTo(foundProduct.getId());
        assertThat(savedProduct.getPrice()).isEqualTo(foundProduct.getPrice());
        assertThat(savedProduct.getName()).isEqualTo(foundProduct.getName());
    }

    @Test
    void findById() throws Exception {
        Product savedProduct = productDao.save(new Product("상품명", new BigDecimal(1000)));
        Product foundProduct = productDao.findById(savedProduct.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedProduct.getId()).isEqualTo(foundProduct.getId());
        assertThat(savedProduct.getPrice()).isEqualTo(foundProduct.getPrice());
        assertThat(savedProduct.getName()).isEqualTo(foundProduct.getName());
    }

    @Test
    void findAll() {
        productDao.save(new Product("상품명", new BigDecimal(1000)));
        productDao.save(new Product("상품명", new BigDecimal(1000)));
        assertThat(productDao.findAll()).hasSize(2);
    }
}
