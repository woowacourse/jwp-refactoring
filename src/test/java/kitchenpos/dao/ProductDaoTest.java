package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class ProductDaoTest {
    @Autowired
    private DataSource dataSource;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("상품을 저장한다.")
    public void save() {
        //given
        Product product = new Product();
        product.setName("product1");
        product.setPrice(new BigDecimal("100.00"));

        //when
        Product returnedProduct = productDao.save(product);

        //then
        assertThat(returnedProduct.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(returnedProduct.getName());
        assertThat(product.getPrice()).isEqualTo(returnedProduct.getPrice());
    }

    @Test
    @DisplayName("id로 상품을 조회한다.")
    public void findById() {
        //given
        Product product = new Product();
        product.setName("product1");
        product.setPrice(new BigDecimal("100.00"));
        Long savedId = productDao.save(product).getId();

        //when
        Optional<Product> returnedProduct = productDao.findById(savedId);

        //then
        assertThat(returnedProduct.isPresent()).isTrue();
        assertThat(returnedProduct.get().getId()).isEqualTo(savedId);
        assertThat(product.getName()).isEqualTo(returnedProduct.get().getName());
        assertThat(product.getPrice()).isEqualTo(returnedProduct.get().getPrice());
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    public void findAll() {
        //given
        final int originalSize = productDao.findAll().size();
        Product product1 = new Product();
        product1.setName("product1");
        product1.setPrice(new BigDecimal("100.00"));
        Product product2 = new Product();
        product2.setName("product2");
        product2.setPrice(new BigDecimal("200.00"));
        final Product savedProduct1 = productDao.save(product1);
        final Product savedProduct2 = productDao.save(product2);

        //when
        List<Product> products = productDao.findAll();

        //then
        assertThat(products.size()).isEqualTo(2 + originalSize);
        assertThat(products).contains(savedProduct1, savedProduct2);
    }
}
