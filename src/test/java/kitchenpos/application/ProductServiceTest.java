package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductServiceTest {

    @Autowired
    private DataSource dataSource;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = BeanAssembler.createProductService(dataSource);
    }

    @Test
    void createProduct() {
        // given
        Product product = new Product();
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(1000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void createProductWithNullPrice() {
        // given
        Product product = new Product();
        product.setName("상품");
        product.setPrice(null);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createProductWithZeroPrice() {
        // given
        Product product = new Product();
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(0));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void createProductWithNegativePrice() {
        // given
        Product product = new Product();
        product.setName("상품");
        product.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findProducts() {
        // given & when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(6);
    }
}
