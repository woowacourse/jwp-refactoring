package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Product;

@SpringBootTest
class JdbcTemplateProductDaoTest {
    @Autowired
    private JdbcTemplateProductDao productDao;

    @Test
    void save() {
        Product product = new Product();
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(1000));

        Product savedProduct = productDao.save(product);

        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger());
    }

    @Test
    void findById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(16000));

        Product foundProduct = productDao.findById(product.getId()).get();

        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        assertThat(foundProduct.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger());
    }

    @Test
    void findAll() {
        Product product = new Product();
        product.setName("product");
        product.setPrice(BigDecimal.valueOf(16000));

        List<Product> products = productDao.findAll();
        productDao.save(product);
        List<Product> savedProducts = productDao.findAll();

        assertThat(products.size()).isEqualTo(savedProducts.size() - 1);
    }
}
