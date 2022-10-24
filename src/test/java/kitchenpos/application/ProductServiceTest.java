package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다")
    void create() {
        final Product product = new Product();
        product.setId(1L);
        product.setName("피자");
        product.setPrice(BigDecimal.valueOf(20000L));

        final Product createProduct = productService.create(product);

        assertThat(createProduct.getName()).isEqualTo("피자");
    }

    @Test
    @DisplayName("상품 전체를 조회한다")
    void list() {
        assertThat(productService.list())
                .hasSizeGreaterThan(1);
    }
}
