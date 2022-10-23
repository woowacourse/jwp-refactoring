package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("페퍼로니 피자");
        product.setPrice(BigDecimal.valueOf(10000L));
    }

    @Test
    void create() {
        Product savedProduct = productService.create(product);

        assertThat(productDao.findById(savedProduct.getId())).isPresent();
    }

    @Test
    void list() {
        int beforeSize = productService.list().size();
        productService.create(product);

        assertThat(productService.list().size()).isEqualTo(beforeSize + 1);
    }
}
